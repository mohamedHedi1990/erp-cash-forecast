package org.apac.erp.cach.forecast;

import org.apac.erp.cach.forecast.config.FileStorageProperties;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.dtos.GeneralLedgerDto;
import org.apac.erp.cach.forecast.enumeration.ERole;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.enumeration.RsTypeSaisie;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.apac.erp.cach.forecast.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ErpCachForecastApplication implements CommandLineRunner {
	
	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepository;
	
	@Autowired
	private CustomerAttachedInvoicesRepository customerAttachedInvocieRepo;
	
	@Autowired
	private ProviderAttachedInvoicesRepository providerAttachedInvoiceRepo;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
	private PaymentRuleRepository paymentRuleRepository;

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
		
		this.editInitialSoldes();
	}

	private void editInitialSoldes() {
		List<Customer> customerList=customerRepository.findAllByOrderByCustomerLabel();
		for (Customer customer : customerList) {
			List<GeneralLedgerDto> generalLedgerDtos=new ArrayList<>();
				double progressiveAmmount=0;
				Date endDate=customer.getCreatedAt();
				List<CustomerInvoice> customerInvoices = customerInvoiceRepository.findByCustomerAndInvoiceDateBeforeOrderByInvoiceDate(customer,endDate);
				List<PaymentRule> paymentRules=paymentRuleRepository.findByCustomer(customer).stream().filter(paymentRule -> {
							return  (((paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.EFFET_ESCOMPTE) &&
									((paymentRule.getPaymentRuleEffetEscompteDate() != null && paymentRule.getPaymentRuleEffetEscompteDate().before(endDate))||
										(paymentRule.getPaymentRuleEffetEscompteDate() == null && paymentRule.getPaymentRuleDeadlineDate().before(endDate)))))
									|| (paymentRule.getPaymentRulePaymentMethod() != PaymentMethod.EFFET_ESCOMPTE && paymentRule.getPaymentRuleDeadlineDate().before(endDate));
						}
				).collect(Collectors.toList());
				int i=0;
				int j=0;
				while(i <= customerInvoices.size()-1){
					CustomerInvoice customerInvoice=customerInvoices.get(i);
					GeneralLedgerDto generalLedgerInvoice=new GeneralLedgerDto();
					generalLedgerInvoice.setDebit(customerInvoice.getInvoiceTotalAmount());
					generalLedgerInvoice.setDebitS(customerInvoice.getInvoiceTotalAmountS());
					generalLedgerInvoice.setDate(customerInvoice.getInvoiceDate());
					generalLedgerInvoice.setLabel(customer.getCustomerLabel() + " " + customerInvoice.getInvoiceNumber());
					GeneralLedgerDto generalLedgerRs=new GeneralLedgerDto();
					generalLedgerRs.setLabel("RS " + customer.getCustomerLabel() + " " + customerInvoice.getInvoiceNumber());
					generalLedgerRs.setDate(customerInvoice.getInvoiceDate());
					if(customerInvoice.getInvoiceRsType() == RsTypeSaisie.VALUE){
						generalLedgerRs.setCredit(customerInvoice.getInvoiceRs());
						generalLedgerRs.setCreditS(Utils.convertAmountToStringWithSeperator(customerInvoice.getInvoiceRs()));
					}else if(customerInvoice.getInvoiceRsType() == RsTypeSaisie.POURCENTAGE){
						generalLedgerRs.setCredit((customerInvoice.getInvoiceNet()*customerInvoice.getInvoiceRs())/100);
						generalLedgerRs.setCreditS(Utils.convertAmountToStringWithSeperator((customerInvoice.getInvoiceNet()*customerInvoice.getInvoiceRs())/100));
					}
					generalLedgerDtos.add(generalLedgerRs);
					generalLedgerDtos.add(generalLedgerInvoice);
					i++;
				}

				while(j<=paymentRules.size() - 1 ){
					PaymentRule paymentRule = paymentRules.get(j);
					GeneralLedgerDto generalLedgerPR = new GeneralLedgerDto();
					if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.EFFET_ESCOMPTE ){
						generalLedgerPR.setDate(paymentRule.getPaymentRuleEffetEscompteDate());
					}else{
						generalLedgerPR.setDate(paymentRule.getPaymentRuleDeadlineDate());
					}
					generalLedgerPR.setCredit(paymentRule.getPaymentRuleAmount());
					generalLedgerPR.setCreditS(paymentRule.getPaymentRuleAmountS());
					switch(paymentRule.getPaymentRulePaymentMethod()){
						case CHEQUE: generalLedgerPR.setLabel("Encaissement cheque");
							break;
						case ESPECE: generalLedgerPR.setLabel("Paymenet espece");
							break;
						case TRAITE: generalLedgerPR.setLabel("Traite");
							break;
						case VIREMENT: generalLedgerPR.setLabel("Virement");
							break;
						case EFFET_ESCOMPTE: generalLedgerPR.setLabel("Effet escompte");
							break;
						case COMISSION_BANCAIRE: generalLedgerPR.setLabel("Comission Bancaire");
							break;
					}
					generalLedgerDtos.add(generalLedgerPR);
					j++;
				}
				generalLedgerDtos.sort(Comparator.comparing(GeneralLedgerDto::getDate));
				for (GeneralLedgerDto generalLedgerDto : generalLedgerDtos) {
					progressiveAmmount=progressiveAmmount - generalLedgerDto.getCredit() + generalLedgerDto.getDebit();
				}
			customer.setCustomerInitialSold(progressiveAmmount);
			customer.setCustomerInitialSoldS(Utils.convertAmountToStringWithSeperator(progressiveAmmount));
			customerRepository.save(customer);
		}
	}

}