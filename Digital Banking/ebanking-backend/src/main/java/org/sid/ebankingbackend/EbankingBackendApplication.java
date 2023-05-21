package org.sid.ebankingbackend;


import org.sid.ebankingbackend.dtos.BankAccountDTO;
import org.sid.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.dtos.SavingBankAccountDTO;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner start(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(90000, Math.random() * 10000, customer.getId());
                    bankAccountService.saveSavingBankAccount( 90000, Math.random() * 10000, customer.getId());
                }catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount : bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccount instanceof CurrentBankAccountDTO) {
                        accountId = ((CurrentBankAccountDTO) bankAccount).getId();
                    } else {
                        accountId = ((SavingBankAccountDTO) bankAccount).getId();
                    }
                    //bankAccountService.debit(accountId, Math.random() * 10000, "Debit");
                    //bankAccountService.credit(accountId, Math.random() * 10000, "Credit");
                    bankAccountService.debit(accountId, 100, "Debit");
                    bankAccountService.credit(accountId,  1000, "Credit");
                }

            }
        };
    }

    /*CommandLineRunner CommandLineRunner(BankAccountRepository bankAccountRepository) {
        return args -> {
            BankAccount bankAccount1 = bankAccountRepository.findById("4f0a5499-79c1-42cb-bd14-0738ce0f3fa7").orElse(null);
            if (bankAccount1 != null) {
                System.out.println("***********************************");
                System.out.println(bankAccount1.getId());
                System.out.println(bankAccount1.getBalance());
                System.out.println(bankAccount1.getStatus());
                System.out.println(bankAccount1.getCreatedAt());
                System.out.println(bankAccount1.getCustomer().getName());
                System.out.println(bankAccount1.getClass().getName());
                if (bankAccount1 instanceof CurrentAccount) {
                    System.out.println("Over Draft " + ((CurrentAccount) bankAccount1).getOverDraft());
                } else if (bankAccount1 instanceof SavingAccount) {
                    System.out.println("Rate " + ((SavingAccount) bankAccount1).getInterestRate());
                }
                bankAccount1.getAccountOperations().forEach(accountOperation -> {
                    System.out.println("------------------------------");
                    System.out.println(accountOperation.getId());
                    System.out.println(accountOperation.getAmount());
                    System.out.println(accountOperation.getOperationDate());
                    System.out.println(accountOperation.getType());
                });
            }
        };
    }*/

    //@Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository, CustomerRepository
            customerRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setBankAccount(bankAccount);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperationRepository.save(accountOperation);
                }

            });

        };
    }
}
