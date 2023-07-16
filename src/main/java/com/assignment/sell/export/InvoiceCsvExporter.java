package com.assignment.sell.export;

import com.assignment.Exporter;
import com.assignment.entity.Invoice;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class InvoiceCsvExporter extends Exporter {

    public void export(HttpServletResponse response, List<Invoice> invoices) {

        super.responseHeader(response, "text/csv", ".csv", "invoices_");

        String[] headers = {};
        String[] fieldInvoice = {};
    }
}
