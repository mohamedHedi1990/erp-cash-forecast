package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.repositories.ContactRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public void importCustomersFromExcelFile(MultipartFile file) throws IOException {
		ArrayList<Customer> customers = new ArrayList<>();
		XSSFWorkbook workbook = null;

		workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastr = worksheet.getLastRowNum();
		for (int j = 2; j <= lastr; j++) {
			XSSFRow row = worksheet.getRow(j);
			if (row != null) {
				String customerLabel = "";
				String customerUniqueIdentifier = "";
				String customerTel = "";
				String customerMail = "";
				String customerAdresse = "";
				String customerManager = "";
				if (row.getCell(0) == null || (row.getCell(0).getStringCellValue().equals(""))) {
					continue;
				}
				if (row.getCell(0) != null && !(row.getCell(0).getStringCellValue().equals(""))) {
					customerLabel = row.getCell(0).getStringCellValue();
				}
				if (row.getCell(1) != null && !(row.getCell(1).getStringCellValue().equals(""))) {
					customerUniqueIdentifier = row.getCell(1).getStringCellValue();
				}
				if (row.getCell(2) != null && !(row.getCell(2).getStringCellValue().equals(""))) {
					customerTel = row.getCell(2).getStringCellValue();
				}
				if (row.getCell(3) != null || !(row.getCell(3).getStringCellValue().equals(""))) {
					customerMail = row.getCell(3).getStringCellValue();
				}
				if (row.getCell(4) != null || !(row.getCell(4).getStringCellValue().equals(""))) {
					customerAdresse = row.getCell(4).getStringCellValue();
				}
				if (row.getCell(5) != null || !(row.getCell(5).getStringCellValue().equals(""))) {
					customerManager = row.getCell(5).getStringCellValue();
				}

				Customer customer = this.customerRepo.findByCustomerLabel(customerLabel);
				if (customer == null) {
					customer=new Customer();
				}
				customer.setCustomerLabel(customerLabel);
				customer.setCustomerUniqueIdentifier(customerUniqueIdentifier);
				customer.setCustomerTel(customerTel);
				customer.setCustomerEmail(customerMail);
				customer.setCustomerAddress(customerAdresse);
				customer.setCustomerManagerName(customerManager);
				customers.add(customer);
			}
		}
		this.customerRepo.save(customers);
	}
}
