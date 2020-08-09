package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@CrossOrigin
	@GetMapping()
	public List<CustomerDTO> findAllCustomersDTO() {
		return customerService.findAllCustomersDTO();
	}
	
	@CrossOrigin
	@GetMapping("/full")
	public List<Customer> findAllCustomers() {
		return customerService.findAllCustomers();
	}


	@CrossOrigin
	@PostMapping()
	public Customer saveNewCustomer(@RequestBody Customer customer) {
		return customerService.saveNewCustomer(customer);
	}

	@CrossOrigin
	@GetMapping("by-customer-id/{customerId}")
	public Customer findCustomerById(@PathVariable("customerId") Long customerId) {
		return customerService.findCustomerById(customerId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{customerId}")
	public void deleteCompany(@PathVariable("customerId") Long customerId) {
		customerService.deleteCustomer(customerId);
	}


}
