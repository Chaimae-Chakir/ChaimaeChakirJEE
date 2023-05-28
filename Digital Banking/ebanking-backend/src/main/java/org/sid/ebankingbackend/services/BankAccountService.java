package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers(int page);

    List<Customer> listCustomer();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountListOfCustomer(Long customerId);

    BankAccountsDTO getBankAccountList(int page);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountOperationHistory(String accountId);

    AccountHistoryDTO getAccoutHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException;

    BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO);
}
