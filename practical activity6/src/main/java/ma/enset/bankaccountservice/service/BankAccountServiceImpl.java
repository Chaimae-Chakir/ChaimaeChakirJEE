package ma.enset.bankaccountservice.service;

import jakarta.transaction.Transactional;
import ma.enset.bankaccountservice.dto.BankAccountRequestDTO;
import ma.enset.bankaccountservice.dto.BankAccountResponseDTO;
import ma.enset.bankaccountservice.entities.BankAccount;
import ma.enset.bankaccountservice.mappers.AccountMapper;
import ma.enset.bankaccountservice.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
   @Autowired
    private AccountMapper accountMapper;
    @Override
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO bankAccountDTO) {
        //BankAccount bankAccount=accountMapper.fromBankAccountRequestDTO(bankAccountDTO);
        BankAccount bankAccount=BankAccount.builder()
                .id(UUID.randomUUID().toString())
                .createdAT(new Date())
                .balance(bankAccountDTO.getBalance())
                .type(bankAccountDTO.getType())
                .currency(bankAccountDTO.getCurrency())
                .build();
        BankAccount savedBankAccount= bankAccountRepository.save(bankAccount);
        BankAccountResponseDTO bankAccountResponseDTO = accountMapper.fromBankAccount(savedBankAccount);
        return bankAccountResponseDTO;
    }

    @Override
    public BankAccountResponseDTO updateAccount(String id,BankAccountRequestDTO bankAccountDTO) {
        //BankAccount bankAccount=accountMapper.fromBankAccountRequestDTO(bankAccountDTO);
        BankAccount bankAccount=BankAccount.builder()
                .id(id)
                .createdAT(new Date())
                .balance(bankAccountDTO.getBalance())
                .type(bankAccountDTO.getType())
                .currency(bankAccountDTO.getCurrency())
                .build();
        BankAccount savedBankAccount= bankAccountRepository.save(bankAccount);
        BankAccountResponseDTO bankAccountResponseDTO = accountMapper.fromBankAccount(savedBankAccount);
        return bankAccountResponseDTO;
    }
}
