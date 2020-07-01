package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	public List<Customer> findAllCustomers() {
		return customerRepo.findAll();
	}

	public Customer saveNewCustomer(Customer customer) {
		return customerRepo.save(customer);

	}

	public Customer findCustomerById(Long customerId) {
		return customerRepo.findOne(customerId);
	}

	public void deleteCustomer(Long customerId) {
		customerRepo.delete(customerId);
	}

}
