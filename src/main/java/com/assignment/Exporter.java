package com.assignment;

import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exporter {

    public void responseHeader(HttpServletResponse response,
                               String contextType,
                               String extension,
                               String prefix) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = prefix + timestamp + extension;

        response.setContentType(contextType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.addHeader(headerKey, headerValue);
    }
}
