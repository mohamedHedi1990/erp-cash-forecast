package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.repositories.ContactRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;

	public Customer saveCustomer(Customer customer) {
		return this.customerRepo.save(customer);
	}
	
	public List<Customer> getAllCustomers() {
		return this.customerRepo.findAll();
	}
	public List<Customer> getAllCustomersOrderByDenomination() {
		return this.customerRepo.findAllByOrderByCustomerLabel();
	}

	public Customer getCustomerById(Long customerId) {
		return this.customerRepo.findOne(customerId);
	}

	public void deleteCustomer(Long customerId) {
		 Customer customer=this.customerRepo.findOne(customerId);
		 customer.getCustomerContacts().forEach(c -> {
           this.contactRepository.delete(c);
		 });
         this.customerInvoiceRepository.findByCustomer(customer).forEach(CI->{
         	customerInvoiceRepository.delete(CI);
		 });
		 this.customerRepo.delete(customerId);
	}
}
