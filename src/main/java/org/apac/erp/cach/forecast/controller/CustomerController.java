package org.apac.erp.cach.forecast.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@CrossOrigin
	@GetMapping()
	public List<Customer> getAllCustomers() {
		return this.customerService.getAllCustomers();
	}

	@CrossOrigin
	@PostMapping()
	public Customer saveCustomer(@RequestBody Customer customer) {
		return customerService.saveCustomer(customer);
	}

	@CrossOrigin
	@GetMapping("/{customerId}")
	public Customer getCustomerById(@PathVariable("customerId") Long customerId) {
		return this.customerService.getCustomerById(customerId);
	}
	@CrossOrigin
	@DeleteMapping("/{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Long customerId) {
		customerService.deleteCustomer(customerId);
	}

	@CrossOrigin
	@PostMapping(value = "/import",headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
	public void importCustomersFromExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
		customerService.importCustomersFromExcelFile(file);
	}

}
