package com.assignment.product.export;

import com.assignment.Exporter;
import com.assignment.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class ProductCsvExporter extends Exporter {

    public void export(HttpServletResponse response, List<Product> cats) throws IOException {
        super.responseHeader(response, "text/csv", ".csv", "products_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] headers = {"Id", "Ten", "So luong", "Gia tien", "Nguon goc", "The loai", "Trang thai"};
        String[] fieldProduct = {"id", "name", "quantity", "price", "origin", "category", "status"};
        csvWriter.writeHeader(headers);

        for (Product product : cats) {
            csvWriter.write(product, fieldProduct);
        }
        csvWriter.close();
    }
}
