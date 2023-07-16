package com.assignment.user.export;

import com.assignment.Exporter;
import com.assignment.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends Exporter {

    private void writeHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tên", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ngày sinh", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("So dien thoai", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Dia chi", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Vai trò", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Trang thai", font));
        table.addCell(cell);
    }

    private void writeData(PdfPTable table, List<User> users) {

        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getName());
            table.addCell(user.getDob().toString());
            table.addCell(user.getPhone());
            table.addCell(user.getAddress());
            table.addCell(user.getEmail());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isStatus()));
        }
    }

    public void export(List<User> users, HttpServletResponse response) throws IOException, DocumentException {

        super.responseHeader(response, "application/pdf", ".pdf", "users_");

        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(BaseColor.BLUE);

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(20);
        table.setWidths(new float[]{1.0f, 2.0f, 2.0f, 2.0f, 3.0f, 2.0f, 6.0f, 1.5f});

        writeHeader(table);
        writeData(table, users);

        document.add(table);
        document.close();
    }
}
