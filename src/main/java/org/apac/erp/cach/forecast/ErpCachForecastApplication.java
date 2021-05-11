package org.apac.erp.cach.forecast;

import org.apac.erp.cach.forecast.config.FileStorageProperties;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.ERole;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.RsTypeSaisie;
import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.apac.erp.cach.forecast.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ErpCachForecastApplication implements CommandLineRunner {
	
	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;
	
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepository;
	
	@Autowired
	private CustomerAttachedInvoicesRepository customerAttachedInvocieRepo;
	
	@Autowired
	private ProviderAttachedInvoicesRepository providerAttachedInvoiceRepo;
    @Autowired
    private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ErpCachForecastApplication.class, args);
		System.out.println("------------ The ERP CASH FORECAST server was sucessuflly started ---");

	}

	@Override
	public void run(String... args) throws Exception {
		//Patch pour calculer le champs invoiceNetS pour les anciennes factures
		customerInvoiceRepository.findAll().stream().filter(invoice -> invoice.getInvoiceNet() != null).forEach(invoice -> {
			invoice.setInvoiceNetS(Utils.convertAmountToStringWithSeperator(invoice.getInvoiceNet()));
			if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceNet()) == 0) {
				invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			}
			this.customerInvoiceRepository.save(invoice);
		});
		providerInvoiceRepository.findAll().stream().filter(invoice -> invoice.getInvoiceNet() != null).forEach(invoice -> {
			invoice.setInvoiceNetS(Utils.convertAmountToStringWithSeperator(invoice.getInvoiceNet()));
			if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceNet()) == 0) {
				invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			}
			this.providerInvoiceRepository.save(invoice);
		});
		
		//patch pour ajouter un rs 0 pour les factures anciennes qui n'ont pas de rs
		customerInvoiceRepository.findAll().stream().filter(invoice -> invoice.getInvoiceRs() == null).forEach(invoice -> {
			invoice.setInvoiceRs(0.0);
			invoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
			invoice.setInvoiceNet(invoice.getInvoiceTotalAmount());
			invoice.setInvoiceNetS(Utils.convertAmountToStringWithSeperator(invoice.getInvoiceNet()));
			this.customerInvoiceRepository.save(invoice);
		});
		providerInvoiceRepository.findAll().stream().filter(invoice -> invoice.getInvoiceRs() == null).forEach(invoice -> {
			invoice.setInvoiceRs(0.0);
			invoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
			invoice.setInvoiceNet(invoice.getInvoiceTotalAmount());
			invoice.setInvoiceNetS(Utils.convertAmountToStringWithSeperator(invoice.getInvoiceNet()));
			this.providerInvoiceRepository.save(invoice);
		});
		if(!roleRepository.findByName(ERole.ADMINISTRATOR).isPresent())
		{
			Role role=new Role();
			role.setName(ERole.ADMINISTRATOR);
			roleRepository.save(role);
		}
		if(!roleRepository.findByName(ERole.CAISSIER).isPresent())
		{
			Role role=new Role();
			role.setName(ERole.CAISSIER);
			roleRepository.save(role);
		}
		if(!roleRepository.findByName(ERole.SIMPLE).isPresent())
		{
			Role role=new Role();
			role.setName(ERole.SIMPLE);
			roleRepository.save(role);
		}
		if(!roleRepository.findByName(ERole.GESTION_COMMERCIAL).isPresent())
		{
			Role role=new Role();
			role.setName(ERole.GESTION_COMMERCIAL);
			roleRepository.save(role);
		}
		
		
	}

}