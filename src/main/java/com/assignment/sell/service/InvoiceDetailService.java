package com.assignment.sell.service;

import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.entity.InvoiceDetail;
import com.assignment.sell.repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Service
public class InvoiceDetailService implements IInvoiceDetailService{

    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public List<InvoiceDetail> findAllInvoiceDetail() {
        return invoiceDetailRepository.findAll();
    }

    @Override
    public InvoiceDetail findByIdInvoiceDetail(Integer id) {
        return invoiceDetailRepository.findById(id).get();
    }

    @Override
    public InvoiceDetail saveInvoiceDetail(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }

    @Override
    public void deleteInvoiceDetail(Integer id) {
        invoiceDetailRepository.deleteById(id);
    }

    @Override
    public List<InvoiceStatisticDto> findProductTopSellingDay(java.util.Date date) {
        return invoiceDetailRepository.findTopSellingProductsByPaymentDate(Date.valueOf(date.toString()));
    }

    @Override
    public List<InvoiceStatisticDto> findProductTopSellingWeek(String dateDay) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        if(dateDay != null) {
            calendar.setTime(dateFormat.parse(dateDay));
        }
        calendar.add(Calendar.DATE, -7);
        java.util.Date date = calendar.getTime();
        Date sqlDate = new Date(date.getTime());

        return invoiceDetailRepository.findTopSellingProducts(sqlDate);
    }

    @Override
    public List<InvoiceStatisticDto> findProductTopSellingMonth(Integer month) {

        if(month == null) {
            return invoiceDetailRepository.findTopSelling(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        }
        return invoiceDetailRepository.findTopSelling(month, LocalDate.now().getYear());
    }
}
