package ma.enset.bankaccountservice.web;

import ma.enset.bankaccountservice.dto.BankAccountRequestDTO;
import ma.enset.bankaccountservice.dto.BankAccountResponseDTO;
import ma.enset.bankaccountservice.entities.BankAccount;
import ma.enset.bankaccountservice.entities.Customer;
import ma.enset.bankaccountservice.repositories.BankAccountRepository;
import ma.enset.bankaccountservice.repositories.CustomerRepository;
import ma.enset.bankaccountservice.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BankAccountGraphQLController {
    @Autowired
    private BankAccountRepository  bankAccountRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private CustomerRepository customerRepository;
    @QueryMapping
    public List<BankAccount> accountsList(){
        return bankAccountRepository.findAll();
    }
    @QueryMapping
    public BankAccount BankAccountById(@Argument String id){
        return bankAccountRepository.findById(id)
                .orElseThrow(()->new RuntimeException(String.format("Account %s not found",id)));
    }

    @MutationMapping
    public BankAccountResponseDTO addAccount(@Argument BankAccountRequestDTO bankAccount){
        return bankAccountService.addAccount(bankAccount);
    }
    @MutationMapping
    public BankAccountResponseDTO updateAccount(@Argument String id,@Argument BankAccountRequestDTO bankAccount){
        return bankAccountService.updateAccount(id,bankAccount);
    }
    @MutationMapping
    public void deleteAccount(@Argument String id){
         bankAccountRepository.deleteById(id);
    }
    @QueryMapping
    public List<Customer> customers(){
        return customerRepository.findAll();
    }
}
/*record BankAccountDTO(Double balance,String type,String currency){
}*/
