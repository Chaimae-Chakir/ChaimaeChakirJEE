package ma.enset.bankaccountservice.service;

import ma.enset.bankaccountservice.dto.BankAccountRequestDTO;
import ma.enset.bankaccountservice.dto.BankAccountResponseDTO;

public interface BankAccountService {
    BankAccountResponseDTO addAccount(BankAccountRequestDTO bankAccountDTO);

    BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO bankAccountDTO);
}
