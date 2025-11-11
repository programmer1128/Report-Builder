package com.report_builder;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.IOException;
import java.util.*;

public class ReportPartitioner
{
    
     private final ReportGeometry geometry;
     private final List<List<String>> masterData;
     private final Map<Integer, Float> precalculatedRowHeights;
     private final String title;
    
     private final float AVAILABLE_HEIGHT;
    
     public ReportPartitioner(ReportGeometry geometry, List<List<String>> masterData, String title) 
     {
         this.geometry = geometry;
         this.masterData = masterData;
         this.title = title;
        
         // This relies on ReportGeometry being defined to handle the input choice
         this.precalculatedRowHeights = geometry.RowHeights(masterData);
         this.AVAILABLE_HEIGHT = geometry.getStartYCoordinate() - geometry.getEndYCoordinate();
     }

     

     private void drawVerticalLines(PdfCanvas canvas) 
     {
         int cols = geometry.getColumnsPerPage();
         for (int i = 0; i <= cols; i++) 
         {
             float current_x_coordinate = geometry.getStartXcoordinate() + (i * geometry.getColumnWidth());
             canvas.moveTo(current_x_coordinate, geometry.getStartYCoordinate());
             canvas.lineTo(current_x_coordinate, geometry.getEndYCoordinate());
         }
         canvas.stroke();
     }

     private void drawHorizontalLine(PdfCanvas canvas, float yCoordinate) 
     {
         canvas.moveTo(geometry.getStartXcoordinate(), yCoordinate)
              .lineTo(geometry.getEndXCoordinate(), yCoordinate)
              .stroke();
     }
    
     private void drawCellContent(PdfCanvas canvas, Document document, PdfDocument pdf, String content, float x, float y, float width, float height) throws IOException 
     {
        
        // Use high-level Paragraph for accurate wrapping and placement
         Paragraph p = new Paragraph(content)
            .setFont(geometry.getReportFont())
            .setFontSize(geometry.getFontSize())
            .setMargin(0)
            .setPadding(0)
            .setMultipliedLeading(geometry.getLineSpacing());

         float textYBaseline = y - geometry.getVerticalPadding();
        
         document.showTextAligned(
             p, 
             x + geometry.getVerticalPadding(), 
             textYBaseline, 
             pdf.getPageNumber(pdf.getLastPage()), 
             TextAlignment.LEFT, 
             com.itextpdf.layout.properties.VerticalAlignment.TOP, 
             0
         );
     }
    
    // 
     private List<Integer> findVerticalPageBreak() 
     {
         
         List<Integer> pageBreakSequence = new ArrayList<>();
         float currentSum = 0;
         int totalRows = masterData.size();

         // Loop through all row indices sequentially
         for (int i = 0; i < totalRows; i++) 
         {
            
             float requiredRowHeight = precalculatedRowHeights.get(i);
             
             // CRITICAL CHECK: Does this ENTIRE row fit?
             if (currentSum + requiredRowHeight > this.AVAILABLE_HEIGHT)
             {
                
                 // This row (i) does not fit. The break occurs before it.
                 // Store the index 'i' as the start of the next page block.
                 pageBreakSequence.add(i); 
                
                 // 2. Reset the sum for the new page block (The new page starts empty)
                 currentSum = 0; 
                
                 // 3. Reset the loop counter to re-check row 'i' against the empty page.
                 i--; 
             } 
             else 
             {
                 // Row fits: Accumulate height
                 currentSum += requiredRowHeight;
             }
         }
        
         // Add the total size as the final break point to ensure the last block is processed
         pageBreakSequence.add(totalRows); 

         return pageBreakSequence;
     }


  
     //method to generat the report
     public void generateReport(String destinationFile) throws IOException 
     {
        
         // Set up the PDF objects
         PdfWriter writer = new PdfWriter(destinationFile);
         PdfDocument pdf = new PdfDocument(writer);
         pdf.setDefaultPageSize(geometry.getPageSize());

         Document document = new Document(pdf); 
        
         int totalColumns = masterData.isEmpty() || masterData.get(0).isEmpty() ? 0 : masterData.get(0).size();
         int numColGroups = (totalColumns + geometry.getColumnsPerPage() - 1) / geometry.getColumnsPerPage();
        
         //  Calculate vertical page breaks in the order of original rows
         List<Integer> breakIndices = findVerticalPageBreak();
        
         // 2. Process the list of break points to define drawing blocks
         int blockStartRow = 0; // The starting row index for the current vertical block

         // Loop through the break indices, which define the end of each vertical block
         for (int blockEndRow : breakIndices) 
         {
            
             //Iterate Horizontally (Column Paging) for this fixed row block
             for (int colGroupIndex = 0; colGroupIndex < numColGroups; colGroupIndex++) 
             {
                
                 //set the page
                 pdf.addNewPage(); 
                 PdfCanvas canvas = new PdfCanvas(pdf.getLastPage());
                 canvas.setLineWidth(0.5f);
                
                 float currentY = geometry.getStartYCoordinate(); // Start drawing at the top margin
                
                 // draw grid
                 drawVerticalLines(canvas);
                 drawHorizontalLine(canvas, geometry.getStartYCoordinate()); // Top boundary
                
                 // --- DEFINE COLUMN RANGE FOR THIS PAGE ---
                 int startCol = colGroupIndex * geometry.getColumnsPerPage();
                 int endCol = Math.min(startCol + geometry.getColumnsPerPage(), totalColumns);

                 // --- ROW DRAWING LOOP for the specific block (blockStartRow to blockEndRow) ---
                 for (int currentRowIndex = blockStartRow; currentRowIndex < blockEndRow; currentRowIndex++) 
                 {
                    
                     List<String> currentRow = masterData.get(currentRowIndex);
                    
                     float rowHeight = precalculatedRowHeights.get(currentRowIndex);
                     float nextY = currentY - rowHeight; 
                    
                     // Place Text Content
                     for (int j = startCol; j < endCol; j++) 
                     {
                         // Ensure we don't try to access data beyond the row's actual size (ragged data)
                         String cellContent = (j < currentRow.size()) ? currentRow.get(j) : ""; 
                         float colStart = geometry.getStartXcoordinate() + ((j - startCol) * geometry.getColumnWidth());
                        
                         drawCellContent(canvas, document, pdf, cellContent, colStart, currentY, geometry.getColumnWidth(), rowHeight);
                     }
                    
                     //Draw Bottom Separator Line
                     drawHorizontalLine(canvas, nextY); 
                    
                     //Advance Y ponter
                     currentY = nextY;
                 }
                
                 //drawing row boundary at the bottom
                 drawHorizontalLine(canvas, geometry.getEndYCoordinate());
                
                 //placing title for the page
                 Paragraph titleBlock = new Paragraph( title + " | Cols " + (startCol + 1) + "-" + endCol + " | Rows " + (blockStartRow + 1) + "-" + blockEndRow)
                     .setFont(geometry.getReportFont())
                     .setFontSize(8f);
                
                 document.showTextAligned(titleBlock, geometry.getStartXcoordinate(), geometry.getStartYCoordinate() + 10, pdf.getPageNumber(pdf.getLastPage()), TextAlignment.LEFT, com.itextpdf.layout.properties.VerticalAlignment.TOP, 0);

             } // End Horizontal Loop
            
             //Advance the Vertical Tracker for the next block
             blockStartRow = blockEndRow;
         }  // End Vertical Block Loop
        
         //closing the document for saving
         document.close();
    }
}