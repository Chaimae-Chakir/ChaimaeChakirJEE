package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
//log4j is a logging library for java
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    // Logger log= LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer {} ", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);  // customer est le propriétaire du compte
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount); // on sauvegarde le compte
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount); // on sauvegarde le compte
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        //return customerRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        //la programmation fonctionnelle(l'api streams a partir de java8)
        List<CustomerDTO> collect = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        /* la programmation impérative
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }*/
        return collect;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("Bank account not found")
        );
        if (bankAccount instanceof CurrentAccount)
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        else if (bankAccount instanceof SavingAccount)
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        else
            return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("Bank account not found")
        );
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Insufficient balance");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("Bank account not found")
        );
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String AccountIdSource, String AccountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(AccountIdSource, amount, "Transfer to " + AccountIdDestination);
        credit(AccountIdDestination, amount, "Transfer from " + AccountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> collect = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount)
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            else if (bankAccount instanceof SavingAccount)
                return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            else
                return null;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer {} ", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> collect = accountOperations.stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        return collect;
    }
    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null)
            throw new BankAccountNotFoundException("Bank account not found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }
}
