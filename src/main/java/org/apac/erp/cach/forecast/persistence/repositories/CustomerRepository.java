package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	 List<Customer>findAllByOrderByCustomerLabel();
	 Customer findByCustomerLabel(String customerLabel);

}
