package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CompanyService companyService;

	public List<CustomerDTO> findAllCustomers() {
		List<CustomerDTO> dtos = new ArrayList<CustomerDTO>();
		List<Customer> customers = customerRepo.findAll();
		customers.stream().forEach(customer -> {
			CustomerDTO dto = new CustomerDTO(customer.getCustomerId(), customer.getCustomerLabel(),
					customer.getCustomerAddress(), customer.getCustomerUniqueIdentifier(),
					customer.getCustomerManagerName(), customer.getCustomerContactNumber(), customer.getCreatedAt(),
					customer.getUpdatedAt());
			dtos.add(dto);
		});

		return dtos;
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
