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
	public List<CustomerDTO> findAllCustomers() {
		return customerService.findAllCustomers();
	}

	@CrossOrigin
	@PostMapping("/company/{companyId}")
	public Customer saveNewCustomer(@RequestBody Customer customer, @PathVariable("companyId") Long companyId) {
		return customerService.saveNewCustomerToGvenCompany(customer, companyId);
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
