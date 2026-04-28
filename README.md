## 📊 Java Report Builder – Dynamic PDF Table Generator

## Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.

This project and its source code are proprietary and confidential. Unauthorized copying of this repository or any of its files, via any medium, is strictly prohibited. No license is granted to use, modify, or distribute this code.


A from-scratch, enterprise-style report generation engine in Java that reads structured data from Excel files and renders paginated, column-aware PDF reports with dynamic layouts.





🚀 Overview

This project is designed to simulate how real-world reporting systems work in enterprise environments.

It takes raw tabular data from Excel and transforms it into a well-structured, multi-page PDF report, handling:

Dynamic table rendering
Automatic pagination (rows + columns)
Adaptive layout based on page size
Text wrapping and row height calculation


Key Features
📥 Excel Data Ingestion
Reads multiple sheets from Excel
Converts data into a structured List<List<String>>
Handles missing/null cells gracefully

📄 Dynamic PDF Generation
Generates reports using iText
Supports multiple page sizes:
A3, A4, A5, A6
Configurable margins and layout

📐 Intelligent Layout Engine
Calculates:
Column width
Row height (based on text wrapping)
Usable page area
Ensures consistent formatting across pages

🔄 Smart Pagination
Vertical Pagination (rows split across pages)
Horizontal Pagination (columns split across pages)
Avoids row breaking across pages
Maintains readable structure

🧩 Modular Architecture

The system is cleanly divided into components:

Component	Responsibility
ExcelReader-	Reads and parses Excel data
ReportGeometry-	Handles layout calculations
ReportPartitioner-Splits data into pages
DrawPdf	Orchestrates- report generation
Main-	Entry point



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

▶️ How to Run
1. Clone the Repository
```bash
git clone https://github.com/your-username/report-builder.git
cd report-builder
```
3. Add Dependencies

Make sure you include:

Apache POI
iText 7

(If using Maven, add dependencies in pom.xml)

3. Run the Application
```bash
javac com/report_builder/*.java
java com.report_builder.Main
```
5. Provide Input
Enter path to Excel file
Choose page size (A3/A4/A5/A6)
6. Output
PDF will be generated as:

output_report_frame.pdf

📸 Example Workflow
Input Excel file:
Employee Data.xlsx
System:
Reads sheet
Calculates layout
Splits into pages

Output:

Multi-page structured PDF report
🧪 Design Highlights (Important)
✔ Row Height Calculation
Based on longest cell content
Uses font metrics for accurate wrapping
✔ Page Break Algorithm
Greedy accumulation of row heights
Ensures:
No overflow
Clean page boundaries
✔ Column Partitioning
Fixed number of columns per page
Handles wide datasets elegantly
📈 Future Improvements
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
