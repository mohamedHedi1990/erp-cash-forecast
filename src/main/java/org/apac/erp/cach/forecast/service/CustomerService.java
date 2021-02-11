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
import org.apache.poi.ss.usermodel.Cell;
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
				String customerManager = "";
				String customerManagerContactPhone = "";
				String customerContactName = "";
				String customerContactPoste = "";
				String customerTel1 = "";
				String customerTel2 = "";
				String customerAdresse = "";
				String customerMail = "";
				String customerUniqueIdentifier = "";
				if (row.getCell(0) == null || (row.getCell(0).getStringCellValue().equals(""))) {
					continue;
				}
				if (row.getCell(0) != null && !(row.getCell(0).getStringCellValue().equals(""))) {
					customerLabel = row.getCell(0).getStringCellValue();
				}
				if (row.getCell(1) != null && !(row.getCell(1).getStringCellValue().equals(""))) {
					customerManager = row.getCell(1).getStringCellValue();
				}
				if (row.getCell(2) != null) {
					row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
					customerManagerContactPhone = row.getCell(2).getStringCellValue();
				}
				if (row.getCell(3) != null && !(row.getCell(3).getStringCellValue().equals(""))) {
					customerContactName = row.getCell(3).getStringCellValue();
				}
				if (row.getCell(4) != null && !(row.getCell(4).getStringCellValue().equals(""))) {
					customerContactPoste = row.getCell(4).getStringCellValue();
				}
				if (row.getCell(5) != null) {
					 row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
					customerTel1 = row.getCell(5).getStringCellValue();
				}
				if (row.getCell(6) != null) {
					row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
					customerTel2 = row.getCell(6).getStringCellValue();
				}
				
				
				if (row.getCell(7) != null && !(row.getCell(7).getStringCellValue().equals(""))) {
					customerAdresse = row.getCell(7).getStringCellValue();
				}
				if (row.getCell(8) != null && !(row.getCell(8).getStringCellValue().equals(""))) {
					customerMail = row.getCell(8).getStringCellValue();
				}
				if (row.getCell(9) != null && !(row.getCell(9).getStringCellValue().equals(""))) {
					customerUniqueIdentifier = row.getCell(9).getStringCellValue();
				}

				Customer customer = this.customerRepo.findByCustomerLabel(customerLabel);
				if (customer == null) {
					customer=new Customer();
				}
				customer.setCustomerLabel(customerLabel);
				customer.setCustomerUniqueIdentifier(customerUniqueIdentifier);
				customer.setCustomerTel(customerTel1);
				//customer.setCustomerTel2(customerTel2);
				customer.setCustomerEmail(customerMail);
				customer.setCustomerAddress(customerAdresse);
				customer.setCustomerManagerName(customerManager);
				
				List<Contact> contacts = new ArrayList<Contact>();
				
				if(customerManagerContactPhone != null && !customerManagerContactPhone.isEmpty()) {
					Contact contactManager = new Contact();
					contactManager.setContactName(customerManager);
					contactManager.setContactTel(customerManagerContactPhone);
					contactManager.setContactPost("Responsable");
					contacts.add(contactManager);
				}
				
				if(customerContactPoste != null && !customerContactPoste.isEmpty()) {
					Contact contact = new Contact();
					contact.setContactName(customerContactName);
					contact.setContactTel(customerTel1);
					contact.setContactPost(customerContactPoste);
					contacts.add(contact);
					
					if(customerTel2 != null  && !customerTel2.isEmpty()) {
						Contact contact1 = new Contact();
						contact1.setContactName(customerContactName);
						contact1.setContactTel(customerTel2);
						contact1.setContactPost(customerContactPoste);
						contacts.add(contact1);
					}
				}
				customer.setCustomerContacts(contacts);
				customers.add(customer);
			}
		}
		this.customerRepo.save(customers);
	}
}
