package com.assignment.product.export;

import com.assignment.Exporter;
import com.assignment.entity.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ProductPdfExporter extends Exporter {

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

        cell.setPhrase(new Phrase("So luong", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Gia tien", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nguon goc", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("The loai", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Trang thai", font));
        table.addCell(cell);
    }

    private void writeData(PdfPTable table, List<Product> products) {

        for(Product product: products) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(product.getOrigin());
            table.addCell(product.getCategory().getName());
            table.addCell(product.getStatus() ? "Đang bán" : "Ngừng bán");
        }
    }

    public void export(HttpServletResponse response, List<Product> products) throws IOException, DocumentException {

        super.responseHeader(response, "application/pdf", ".pdf", "products_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(BaseColor.BLUE);

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(11);
        table.setWidths(new float[]{1.0f, 2.0f, 1.0f, 1.5f, 2.0f, 2.0f, 1.5f});

        writeHeader(table);
        writeData(table, products);

        document.add(table);
        document.close();
    }
}
