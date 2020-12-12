package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {

	List<CustomerInvoice> findByCustomer(Customer customer);
	List<CustomerInvoice>findAllByOrderByInvoiceDateDesc();

}
