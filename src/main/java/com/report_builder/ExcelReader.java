package com.report_builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.File; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//this class is used to read data from the excel file
public class ExcelReader 
{
     //method to read the data from the excel file and return it as a List of rows
     
     //a file may contain multiple sheets hence we use the map so that every sheet is mapped
     //to the sheetname
     public Map<String,List<List<String>>> readExcelSheetData(String filePath)
     {
         Map<String,List<List<String>>> entireFile = new HashMap<>();
         DataFormatter dataFormatter = new DataFormatter();

         //create a excel workbook object
         try(Workbook workbook =  WorkbookFactory.create(new File(filePath))) 
         {
             int numberOfSheets=workbook.getNumberOfSheets();

             for(int i=0;i<numberOfSheets;i++)
             {
                 //loading the excel workbook sheet
                 Sheet sheet = workbook.getSheetAt(i);
                 List<List<String>> records = new ArrayList<>();

                 //we loop through all the rows and store them as a list of lists
                 for(Row row:sheet)
                 {
                     List<String> rowData = new ArrayList<>();
                     int lastColumnIndex= row.getLastCellNum();


                     //now we iterate through the entire column and store the data
                     for(int k=0;k<lastColumnIndex;k++)
                     {
                         Cell cell = row.getCell(k,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                     
                         String cellData = dataFormatter.formatCellValue(cell);

                         rowData.add(cellData);
                     }

                     records.add(rowData);
                 
                 }
                 entireFile.put(sheet.getSheetName(),records);
             }
             
         } 
         catch (IOException e) 
         {
             System.out.println("Failed to open or load the worksheet");
         }
         return entireFile;
     }
}
