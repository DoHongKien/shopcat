package com.assignment;


import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.entity.Invoice;
import com.assignment.entity.InvoiceDetail;
import com.assignment.entity.Product;
import com.assignment.entity.User;
import com.assignment.product.repository.IProductRepository;
import com.assignment.sell.repository.InvoiceDetailRepository;
import com.assignment.sell.repository.InvoiceRepository;
import com.assignment.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Test
    @Rollback(value = false)
    public void testAddInvoiceAndInvoiceDetail() {

        User user = userRepository.findById(1).get();
        Product product1 = productRepository.findById(11).get();
        Product product2 = productRepository.findById(12).get();

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setTotalAmount(BigDecimal.valueOf(10000));
        invoice.setStatus(true);

        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setQuantity(2);
        invoiceDetail.setPrice(BigDecimal.valueOf(6000));
        invoiceDetail.setStatus(true);
        invoiceDetail.setProduct(product1);

        InvoiceDetail invoiceDetail1 = new InvoiceDetail();
        invoiceDetail1.setQuantity(1);
        invoiceDetail1.setPrice(BigDecimal.valueOf(4000));
        invoiceDetail1.setStatus(true);
        invoiceDetail1.setProduct(product1);
        invoiceDetail1.setProduct(product2);

        product1.getInvoiceDetails().add(invoiceDetail);
        invoice.addInvoiceDetail(invoiceDetail);
        invoice.addInvoiceDetail(invoiceDetail1);

        invoiceRepository.save(invoice);
        invoiceDetailRepository.save(invoiceDetail);
        invoiceDetailRepository.save(invoiceDetail1);
    }

    @Test
    public void findTopSellingProductsByPaymentDate() {
        List<InvoiceStatisticDto> topSellingProductsByPaymentDate =
                invoiceDetailRepository.findTopSellingProductsByPaymentDate(Date.valueOf("2023-06-07"));

        for(InvoiceStatisticDto dto: topSellingProductsByPaymentDate) {
            System.out.println(dto.getId() + " | ");
        }
        assertThat(topSellingProductsByPaymentDate.size()).isGreaterThan(0);

    }

    @Test
    public void findTopSellingProducts() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        java.util.Date date = calendar.getTime();
        Date sqlDate = new Date(date.getTime());
        List<InvoiceStatisticDto> topSelling = invoiceDetailRepository.findTopSellingProducts(sqlDate);
        for(InvoiceStatisticDto dto: topSelling) {
            System.out.println(dto.getId() + " | ");
        }
        assertThat(topSelling).isNotNull();
    }

    @Test
    public void findTopSelling() {
        List<InvoiceStatisticDto> topSelling = invoiceDetailRepository.findTopSelling(6, 2023);

        assertThat(topSelling).isNotNull();
    }
}
