package com.report_builder;

import java.util.*;


import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.io.font.constants.StandardFonts;
import java.io.*;

public class ReportGeometry 
{
     Scanner sc = new Scanner(System.in);
     private float Margin;
     private int columns_per_page;
     private float start_x_coordinate;
     private float end_x_coordinate;
     private float usable_width;
     private float column_width;
     private float start_y_coordinate;
     private float end_y_coordinate;
     private List<List<String>> data;
     private String destination;
     private PdfFont reportFont;
     private PageSize selectedSize;
     private final float FONT_SIZE = 12f;    // Standard font size for report content
     private final float LINE_SPACING = 1.2f; // Multiplier for line height (1.2 * 9 = 10.8 pt)

     
     ReportGeometry(float Margin,int columns_per_page,List<List<String>> data,String destination) throws IOException
     {
         System.out.println("Enter the size of the paper\nA3 \n A4\n A5\n A6");
         String choice=sc.nextLine();
         float pageWidth=0;
         float pageHeight=0;
        
         switch(choice)
         {
             case "A4":
             selectedSize=PageSize.A4;
             pageWidth=selectedSize.getWidth();
             pageHeight=selectedSize.getHeight();
             break;

             case "A3":
             selectedSize=PageSize.A3;
             pageWidth=selectedSize.getWidth();
             pageHeight=selectedSize.getHeight();
             break;

             case "A5":
             selectedSize=PageSize.A5;
             pageWidth=selectedSize.getWidth();
             pageHeight=selectedSize.getHeight();
             break;

             case "A6":
             selectedSize=PageSize.A6;
             pageWidth=selectedSize.getWidth();
             pageHeight=selectedSize.getHeight();
             break;

             default:
             selectedSize=PageSize.A4;
             
         }

         this.Margin=Margin;
         this.columns_per_page=columns_per_page;
         this.data=data;
         this.start_x_coordinate=this.Margin;
         this.end_x_coordinate=pageWidth-this.Margin;
         this.usable_width=Math.abs(start_x_coordinate-end_x_coordinate);
         this.column_width=usable_width/columns_per_page;
         this.start_y_coordinate=pageHeight-this.Margin;
         this.end_y_coordinate=this.Margin;
         this.destination=destination;
         this.reportFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
     }   

     //getters for the fields
     float getMargin()
     {
         return this.Margin;
     }

     int getColumnsPerPage()
     {
         return this.columns_per_page;
     }

     //get data
     List<List<String>> getData()
     {
         return this.data;
     }

     float getStartXcoordinate()
     {
         return this.start_x_coordinate;
     }

     float getEndXCoordinate()
     {
         return this.end_x_coordinate; 
     }

     float getUsableWidth()
     {
         return this.usable_width;
     }

     float getColumnWidth()
     {
         return this.column_width;
     }

     float getStartYCoordinate()
     {
         return this.start_y_coordinate;
     }

     float getEndYCoordinate()
     {
         return this.end_y_coordinate;
     }

     PageSize getPageSize()
     {
         return this.selectedSize;
     }

     String getDestination()
     {
         return this.destination;
     }

     float getFontSize()
     {
         return this.FONT_SIZE;
     }

     float getLineSpacing()
     {
         return this.LINE_SPACING;
     }

     float getVerticalPadding()
     {
         return 0.5f;
     }


     PdfFont getReportFont()
     {
         return this.reportFont;
     }

     //this method returns a map of row heights with the row indices as 
     //key for drawing the rows
     public HashMap<Integer,Float> RowHeights(List<List<String>>tableData)
     {
         //every element in the tableData is a row. Now for each row we need 
         //to get the row height so that we can draw the rows properly

         HashMap<Integer,Float> rowHeights = new LinkedHashMap<>();

         int rows=tableData.size();

         for(int i=0;i<rows;i++)
         {
             //for each row we will find the string that will take the max width
             //as that string will require the max height and hence that will be 
             //the height of our row
             String maxWidthString=getMaxWidthText(tableData.get(i));

             //we will get the height of row for this string from the get row height method
             float rowHeight=getRowHeight(maxWidthString);

             //now we will set the RowHeight for this row
             rowHeights.put(i,rowHeight);

             //calculate the total sum of the heights for later use

         }

         return rowHeights;
     }

     public String getMaxWidthText(List<String> rowData)
     {
         String maxWidthData="";
         float maxWidth=Integer.MIN_VALUE;
         for(String data:rowData)
         {
             if(maxWidth<this.reportFont.getWidth(data,FONT_SIZE))
             {
                 maxWidth=this.reportFont.getWidth(data,FONT_SIZE);
                 maxWidthData=data;
             }
         }
         return maxWidthData;
     }

     //method to get the row height for a particular row
     //the row height will be the height of the maximum cell 
     public float getRowHeight(String text)
     {
         if(text==null||text.trim().isEmpty())
         {
             return FONT_SIZE*LINE_SPACING;
         }
        
         float textWidth=0;
         try
         {
             textWidth=this.reportFont.getWidth(text,FONT_SIZE);
         }
         catch(Exception e)
         {
             e.printStackTrace();
             return FONT_SIZE*LINE_SPACING;
         }

         //now even inside the column there has to be some gap left for the text to appear
         //between the marginf
         float textArea=this.column_width-2;

         int requiredLines= (int)Math.ceil(textWidth/textArea);

         if (requiredLines==0)
         {
             requiredLines=1;
         }
         return requiredLines*FONT_SIZE*LINE_SPACING;
     }


     
}

