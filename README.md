## 📊 Java Report Builder – Dynamic PDF Table Generator

## Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.

This project and its source code are proprietary and confidential. Unauthorized copying of this repository or any of its files, via any medium, is strictly prohibited. No license is granted to use, modify, or distribute this code.


A from-scratch, enterprise-style report generation engine in Java that reads structured data from Excel files and renders paginated, column-aware PDF reports with dynamic layouts.





## 🚀 Overview

This project is designed to simulate how real-world reporting systems work in enterprise environments.

It takes raw tabular data from Excel and transforms it into a well-structured, multi-page PDF report, handling:

Dynamic table rendering
Automatic pagination (rows + columns)
Adaptive layout based on page size
Text wrapping and row height calculation


## Key Features
📥 Excel Data Ingestion
Reads multiple sheets from Excel
Converts data into a structured List<List<String>>
Handles missing/null cells gracefully

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

JasperReports
Crystal Reports
Internal enterprise reporting systems
