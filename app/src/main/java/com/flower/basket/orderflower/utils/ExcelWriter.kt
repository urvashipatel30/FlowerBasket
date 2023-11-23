package com.flower.basket.orderflower.utils

class ExcelWriter {
    /*fun writeJsonToExcel(context: Context, jsonData: String, fileName: String) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet1")

        // Parse JSON data
        val jsonArray = JSONArray(jsonData)

        var rowIndex = 0
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val row: Row = sheet.createRow(rowIndex++)

            var cellIndex = 0
            for (key in jsonObject.keys()) {
                val cell: Cell = row.createCell(cellIndex++)
                cell.setCellValue(jsonObject.getString(key))
            }
        }

        // Save the workbook to a file
        val excelFilePath = context.getExternalFilesDir(null)?.absolutePath + "/$fileName.xlsx"
        val fileOut = FileOutputStream(excelFilePath)
        workbook.write(fileOut)
        fileOut.close()

        // Display a message or perform any further actions as needed
    }*/
}