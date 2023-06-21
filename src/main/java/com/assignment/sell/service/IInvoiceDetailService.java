package com.assignment.sell.service;

import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.entity.InvoiceDetail;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IInvoiceDetailService {

    List<InvoiceDetail> findAllInvoiceDetail();

    InvoiceDetail findByIdInvoiceDetail(Integer id);

    InvoiceDetail saveInvoiceDetail(InvoiceDetail invoiceDetail);

    void deleteInvoiceDetail(Integer id);

    List<InvoiceStatisticDto> findProductTopSellingDay(Date date);

    List<InvoiceStatisticDto> findProductTopSellingWeek(String dateDay) throws ParseException;

    List<InvoiceStatisticDto> findProductTopSellingMonth(Integer month);
}
