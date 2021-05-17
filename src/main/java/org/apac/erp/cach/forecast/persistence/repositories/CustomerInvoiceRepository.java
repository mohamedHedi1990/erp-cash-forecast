package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {

	List<CustomerInvoice> findByCustomer(Customer customer);
	List<CustomerInvoice>findAllByCustomerAndInvoiceStatus(Customer customer, InvoiceStatus invoiceStatus);
	List<CustomerInvoice>findAllByOrderByInvoiceNumberDesc();
	List<CustomerInvoice>  findByInvoiceStatusAndInvoiceDeadlineDateBetween(InvoiceStatus invoiceStatus, Date startDate, Date endDate);
	List<CustomerInvoice>  findByInvoiceStatusAndInvoiceDeadlineDateBefore(InvoiceStatus invoiceStatus, Date startDate);
	List<CustomerInvoice>   findByInvoiceNumberIgnoreCase(String invoiceNumber);

    List<CustomerInvoice> findByCustomerAndInvoiceDateBetweenOrderByInvoiceDate(Customer customer, Date startDate, Date endDate);

    List<CustomerInvoice> findByCustomerAndInvoiceDateBeforeOrderByInvoiceDate(Customer customer, Date endDate);

}
