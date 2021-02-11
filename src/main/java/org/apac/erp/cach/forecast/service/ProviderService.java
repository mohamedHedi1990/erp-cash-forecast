package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.repositories.ContactRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepo;

	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepository;

	public Provider saveProvider(Provider provider) {
		return this.providerRepo.save(provider);
	}
	
	public List<Provider> getAllProviders() {
		return this.providerRepo.findAllByOrderByProviderLabel();
	}
	
	public Provider getProviderById(Long providerId) {
		return this.providerRepo.findOne(providerId);
	}

	public void deleteProvider(Long providerId) {
		Provider provider=this.providerRepo.findOne(providerId);
		provider.getProviderContacts().forEach(c -> {
			this.contactRepository.delete(c);
		});
		this.providerInvoiceRepository.findByProvider(provider).forEach(PI->{
			this.providerInvoiceRepository.delete(PI);
		});
		 this.providerRepo.delete(providerId);
		
	}

	public void importProvidersFromExcelFile(MultipartFile file) throws IOException {
		ArrayList<Provider> providers = new ArrayList<>();
		XSSFWorkbook workbook = null;

		workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastr = worksheet.getLastRowNum();
		for (int j = 2; j <= lastr; j++) {
			XSSFRow row = worksheet.getRow(j);
			if (row != null) {
				String providerLabel = "";
				String providerManager = "";
				String providerManagerContactPhone = "";
				String providerContactPoste = "Non Défini";
				String providerContactName = "Non Défini";
				String providerTel1 = "";
				String providerTel2 = "";
				String providerAdresse = "";
				String providerMail = "";
				String providerUniqueIdentifier = "";
				if (row.getCell(0) == null || (row.getCell(0).getStringCellValue().equals(""))) {
					continue;
				}
				if (row.getCell(0) != null && !(row.getCell(0).getStringCellValue().equals(""))) {
					providerLabel = row.getCell(0).getStringCellValue();
				}
				if (row.getCell(1) != null && !(row.getCell(1).getStringCellValue().equals(""))) {
					providerManager = row.getCell(1).getStringCellValue();
				}
				if (row.getCell(2) != null) {
					row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
					providerManagerContactPhone = row.getCell(2).getStringCellValue();
				}
				if (row.getCell(3) != null) {
					row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
					providerTel1 = row.getCell(3).getStringCellValue();
				}
				if (row.getCell(4) != null) {
					row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
					providerTel2 = row.getCell(4).getStringCellValue();
				}
				if (row.getCell(5) != null ) {
					row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
					providerAdresse = row.getCell(5).getStringCellValue();
				}
				if (row.getCell(6) != null ) {
					row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
					providerMail = row.getCell(6).getStringCellValue();
				}
				if (row.getCell(7) != null ) {
					row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
					providerUniqueIdentifier = row.getCell(7).getStringCellValue();
				}

				Provider provider = this.providerRepo.findByProviderLabel(providerLabel);
				if (provider == null) {
					provider=new Provider();
				}
				provider.setProviderLabel(providerLabel);
				provider.setProviderUniqueIdentifier(providerUniqueIdentifier);
				provider.setProviderTel(providerTel1);
				provider.setProviderEmail(providerMail);
				provider.setProviderAddress(providerAdresse);
				provider.setProviderManagerName(providerManager);

				List<Contact> contacts = new ArrayList<Contact>();

				if(providerManagerContactPhone != null && !providerManagerContactPhone.isEmpty()) {
					Contact contactManager = new Contact();
					contactManager.setContactName(providerManager);
					contactManager.setContactTel(providerManagerContactPhone);
					contactManager.setContactPost("Responsable");
					contacts.add(contactManager);
				}
				if(providerTel2 != null  && !providerTel2.isEmpty()) {
					Contact contact1 = new Contact();
					contact1.setContactName(providerContactName);
					contact1.setContactPost(providerContactPoste);
					contact1.setContactTel(providerTel2);
					contacts.add(contact1);
				}

				provider.setProviderContacts(contacts);
				providers.add(provider);
			}
		}
		this.providerRepo.save(providers);
	}


}
