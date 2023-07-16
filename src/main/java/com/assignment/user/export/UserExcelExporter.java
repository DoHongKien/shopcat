package com.assignment.user.export;

import com.assignment.Exporter;
import com.assignment.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends Exporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        this.workbook = new XSSFWorkbook();
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {

        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(cellStyle);
    }

    private void writeHeader() {
        sheet = workbook.createSheet("User");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "ID", cellStyle);
        createCell(row, 1, "Tên", cellStyle);
        createCell(row, 2, "Ngày sinh", cellStyle);
        createCell(row, 3, "Số điện thoại", cellStyle);
        createCell(row, 4, "Địa chỉ", cellStyle);
        createCell(row, 5, "Email", cellStyle);
        createCell(row, 6, "Vai trò", cellStyle);
        createCell(row, 7, "Trạng thái", cellStyle);
    }

    private void writeData(List<User> users) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = new XSSFFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for (User user : users) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getName(), cellStyle);
            createCell(row, columnIndex++, user.getDob().toString(), cellStyle);
            createCell(row, columnIndex++, user.getPhone(), cellStyle);
            createCell(row, columnIndex++, user.getAddress(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columnIndex++, user.isStatus(), cellStyle);
        }
    }

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.responseHeader(response, "application/octet-stream", ".xlsx", "users_");

        writeHeader();
        writeData(users);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
