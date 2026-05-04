## 📊 Java Report Builder – Dynamic PDF Table Generator

## Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.

This project and its source code are proprietary and confidential. Unauthorized copying of this repository or any of its files, via any medium, is strictly prohibited. No license is granted to use, modify, or distribute this code.


A from-scratch, enterprise-style report generation engine in Java that reads structured data from Excel files and renders paginated, column-aware PDF reports with dynamic layouts.


> *A robust Java-based enterprise reporting engine designed to uderstand the internal logic of document rendering. It ingests raw tabular data from Excel for testing or which can be fed from a database too and processes it through a custom-built geometry and pagination engine to generate highly structured, multi-page PDFs. By programmatically calculating text wrapping, adaptive row heights, and handling complex multi-axis pagination (both horizontal and vertical), this project provides low-level control over the document generation pipeline—serving as a lightweight, from-scratch alternative to heavyweights like JasperReports.*



## 🚀 Overview

This project is designed to simulate how real-world reporting systems work in enterprise environments.

It takes raw tabular data from Excel as testing (can include data from database too by adding an API) and transforms it into a well-structured, multi-page PDF report, handling:

Dynamic table rendering
Automatic pagination (rows + columns)
Adaptive layout based on page size
Text wrapping and row height calculation


# Core Logic
To understand how Report Builder handles dynamic layouts without relying on external frameworks, here is a look at the internal geometry and pagination logic.

1. Dynamic Row Height Calculation
The ReportGeometry class calculates the exact height required for each row to accommodate text wrapping. Instead of fixed heights, it finds the cell that requires the most vertical space and scales the entire row accordingly.

```java
// Inside ReportGeometry.java- calculates row height based on text wrapping
public float getRowHeight(String text) 
{
    if (text == null || text.trim().isEmpty()) 
    {
       return FONT_SIZE * LINE_SPACING; // Default to one line
    }
    
    float textWidth = this.reportFont.getWidth(text, FONT_SIZE);
    float textArea = this.column_width - 2; // Account for margins

    int requiredLines = (int) Math.ceil(textWidth / textArea);
    if (requiredLines == 0) 
    {
       requiredLines = 1;
    }
    
    return requiredLines * FONT_SIZE * LINE_SPACING;
}
```

2. Smart Vertical Pagination
A major challenge in report generation is ensuring that a row of data is not split horizontally across two pages. The ReportPartitioner.java solves this using the findVerticalPageBreak algorithm, which pre-calculates safe break points based on the available page height.

```java
// Inside ReportPartitioner.java: Determines exact row indices for page breaks
private List<Integer> findVerticalPageBreak() 
{
    List<Integer> pageBreakSequence = new ArrayList<>();
    float currentSum = 0;
    int totalRows = masterData.size();

    for (int i = 0; i < totalRows; i++) 
    {
       float requiredRowHeight = precalculatedRowHeights.get(i);
        
        //checking to see if entire row fits into the page
       if (currentSum + requiredRowHeight > this.AVAILABLE_HEIGHT) 
       {
          pageBreakSequence.add(i); // save the break point
          currentSum = 0; // reset height sum for the new page
          i--; // reevaluate this row for the top of the new page
       } 
       else 
       {
          currentSum += requiredRowHeight; // cumulative row height for a page
       }
    }
    
    pageBreakSequence.add(totalRows); //get the final block
    return pageBreakSequence;
}
```



## Key Features
1. Excel Data Ingestion
2. Reads multiple sheets from Excel
3. Converts data into a structured `List<List<String>>`
4. Handles missing/null cells gracefully

## 📄 Dynamic PDF Generation
Generates reports using iText
Supports multiple page sizes:
A3, A4, A5, A6
Configurable margins and layout

## 📐 Intelligent Layout Engine
Calculates:
Column width
Row height (based on text wrapping)
Usable page area
Ensures consistent formatting across pages

## 🔄 Smart Pagination
Vertical Pagination (rows split across pages)
Horizontal Pagination (columns split across pages)
Avoids row breaking across pages
Maintains readable structure

## 🧩 Modular Architecture

The system is cleanly divided into components:

Component	Responsibility
ExcelReader-	Reads and parses Excel data
ReportGeometry-	Handles layout calculations
ReportPartitioner-Splits data into pages
DrawPdf	Orchestrates- report generation
Main-	Entry point


## Technical Architecture
```
Architecture
Excel File
   ↓
ExcelReader
   ↓
Structured Data (List<List<String>>)
   ↓
ReportGeometry (layout calculations)
   ↓
ReportPartitioner (pagination engine)
   ↓
DrawPdf (rendering)
   ↓
PDF Output
```



Technologies Used
Java
Apache POI → Excel processing
iText 7 → PDF generation


## 📈 Future Improvements
 Styling support (colors, fonts, themes)
 Header/footer support
 Export formats (CSV, HTML)
 Streaming large datasets (memory optimization)
 GUI or web interface
 Support for nested tables / grouped reports
💡 Why This Project Matters

This project demonstrates:

Low-level control over rendering engines
Understanding of layout systems (like browser engines / reporting tools)
Handling of real-world constraints:
Pagination
Dynamic data
Memory vs layout tradeoffs

It is similar in spirit to:

- JasperReports
- Crystal Reports
- Internal enterprise reporting systems
