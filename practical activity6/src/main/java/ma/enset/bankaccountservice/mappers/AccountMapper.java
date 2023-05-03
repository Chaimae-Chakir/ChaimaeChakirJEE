package ma.enset.bankaccountservice.mappers;

import ma.enset.bankaccountservice.dto.BankAccountResponseDTO;
import ma.enset.bankaccountservice.entities.BankAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public BankAccountResponseDTO fromBankAccount(BankAccount bankAccount){
        BankAccountResponseDTO bankAccountResponseDTO=new BankAccountResponseDTO();
        BeanUtils.copyProperties(bankAccount,bankAccountResponseDTO);
        return bankAccountResponseDTO;
    }
    public BankAccount fromBankAccount(BankAccountResponseDTO bankAccountResponseDTO){
        BankAccount bankAccount=new BankAccount();
        BeanUtils.copyProperties(bankAccountResponseDTO,bankAccount);
        return bankAccount;
    }
}
