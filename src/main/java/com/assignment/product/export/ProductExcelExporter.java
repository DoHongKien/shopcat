package com.assignment.product.export;

import com.assignment.Exporter;
import com.assignment.entity.Product;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ProductExcelExporter extends Exporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ProductExcelExporter() {
        this.workbook = new XSSFWorkbook();
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {

        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(cellStyle);
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Invoice");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "ID", cellStyle);
        createCell(row, 1, "Tên", cellStyle);
        createCell(row, 2, "Số lượng", cellStyle);
        createCell(row, 3, "Giá tiền", cellStyle);
        createCell(row, 4, "Nguồn gốc", cellStyle);
        createCell(row, 5, "Thể loại", cellStyle);
        createCell(row, 6, "Trạng thái", cellStyle);
    }

    private void writeData(List<Product> products) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for (Product product : products) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, product.getId(), cellStyle);
            createCell(row, columnIndex++, product.getName(), cellStyle);
            createCell(row, columnIndex++, product.getQuantity(), cellStyle);
            createCell(row, columnIndex++, product.getPrice().doubleValue(), cellStyle);
            createCell(row, columnIndex++, product.getOrigin(), cellStyle);
            createCell(row, columnIndex++, product.getCategory().getName(), cellStyle);
            createCell(row, columnIndex++, product.getStatus() ? "Đang bán" : "Ngừng bán", cellStyle);
        }
    }

    public void export(HttpServletResponse response, List<Product> products) throws IOException {
        super.responseHeader(response, "application/octet-stream", ".xlsx", "products_");

        writeHeader();
        writeData(products);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
}
