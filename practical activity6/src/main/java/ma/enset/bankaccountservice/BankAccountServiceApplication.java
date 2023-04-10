package ma.enset.bankaccountservice;

import ma.enset.bankaccountservice.entities.BankAccount;
import ma.enset.bankaccountservice.enums.AccountType;
import ma.enset.bankaccountservice.repositories.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class BankAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountServiceApplication.class, args);
    }
        CommandLineRunner start(BankAccountRepository bankAccountRepository){
           return args -> {
               for (int i = 0; i <10 ; i++) {
                   BankAccount bankAccount=BankAccount.builder()
                           .id(UUID.randomUUID().toString())
                           .type(Math.random()>0.5? AccountType.CURRENT_ACCOUNT:AccountType.SAVING_ACCOUNT)
                           .balance(10000+Math.random()*90000)
                           .createdAT(new Date())
                           .currency("MAD")
                           .build();
                   bankAccountRepository.save(bankAccount);
               }
           };
        }

}
