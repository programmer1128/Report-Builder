/*
 * Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.
 * GitHub: https://github.com/programmer1128
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */


package com.report_builder;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class DrawPdf 
{
     Scanner sc = new Scanner(System.in);


     DrawPdf(float Margin,int columns_per_page,List<List<String>> data,String destination) throws IOException
     { // 1. Initialize Geometry (Configuration & Metrics)
         ReportGeometry geometry = new ReportGeometry(Margin,columns_per_page,data,destination);
         
         // 2. Initialize Partitioner (Execution Engine)
         // The number of columns per page is fixed at 5 in ReportGeometry, so we don't pass it here.
         ReportPartitioner partitioner = new ReportPartitioner(geometry, data, "Report"); // Use a placeholder title
         
         // 3. CRITICAL STEP: Execute the report generation
         partitioner.generateReport(destination);
         
         System.out.println("Report generation complete. File saved to: " + destination);
     }
     
     

     //this method is used to find 


     
}
