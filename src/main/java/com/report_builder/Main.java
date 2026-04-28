/*
 * Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.
 * GitHub: https://github.com/programmer1128
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */



package com.report_builder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main 
{
     public Map<String, List<List<String>>> readData(String filePath)
     {
         ExcelReader reader = new ExcelReader();
         
         // Calls the corrected ExcelReader method
         return reader.readExcelSheetData(filePath);
     }
     
     public static void main(String[] args) 
     {
         Main obj1 = new Main();
         Scanner sc = new Scanner(System.in);
         Map<String, List<List<String>>> excelFile;
         
         System.out.println("--- Report Builder Console ---");
         System.out.println("Enter the path to  Excel file ");
         String filePath = sc.nextLine();
         
         // Define parameters for the PDF report:
         final float MARGIN = 20f;             // 10 points margin on all sides
         final int COLS_PER_PAGE = 5;          // fixed column count for PDF layout
         final String DEST_FILE = "output_report_frame.pdf";

         try {
             // 1. READ DATA FROM EXCEL
             excelFile = obj1.readData(filePath);

             if (excelFile.isEmpty()) {
                 System.out.println("No data found in the Excel file or file could not be opened.");
                 return;
             }

             // 2. PRINT DATA TO CONSOLE (Using your original print logic)
             for (String key : excelFile.keySet()) 
             {
                 System.out.println("\n===============================================");
                 System.out.println("Processing Sheet: " + key);
                 System.out.println("Total Rows: " + excelFile.get(key).size());
                 System.out.println("Max Columns: " + excelFile.get(key).get(0).size());
                 System.out.println("===============================================");
                 
                 List<List<String>> currentTable = excelFile.get("Sheet1");

                 // Simple Console Print (not padded, just showing the data structure)
                 for(int i = 0; i < currentTable.size(); i++)
                 {
                     List<String> row = currentTable.get(i);
                     System.out.print("[Row " + (i+1) + "]: ");
                     for(String cellValue : row)
                     {
                         // Truncate output for console readability
                         System.out.printf("%-10s | ", cellValue.length() > 8 ? cellValue.substring(0, 8) + "..." : cellValue);
                     }
                     System.out.println();
                 }

                 // 3. DRAW PDF FRAME FOR THIS SHEET
                 System.out.println("\nAttempting to generate PDF frame for sheet: " + key + "...");

                 // Initialize and draw the PDF frame using the DrawPdf class
                 // We use the first sheet's data to test the dimensions
                 new DrawPdf(MARGIN, COLS_PER_PAGE, currentTable, DEST_FILE);
                 
                 // Since the entire drawing logic is inside the constructor/drawPdf call, 
                 // we only do this once per test run, but you could loop through all sheets.
                 
                 System.out.println("SUCCESS: PDF frame generated at " + DEST_FILE);
                 System.out.println("Check the PDF file to verify the A4 page margins and 5-column grid.");
                 break; // Only processing the first sheet for the initial PDF test
             }

         } catch (IOException e) {
             System.err.println("An I/O error occurred during processing: " + e.getMessage());
         } finally {
             sc.close();
         }
     }
}
