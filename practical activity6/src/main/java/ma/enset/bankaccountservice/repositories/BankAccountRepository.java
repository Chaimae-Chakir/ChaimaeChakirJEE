package ma.enset.bankaccountservice.repositories;

import ma.enset.bankaccountservice.entities.BankAccount;
import ma.enset.bankaccountservice.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
   @RestResource(path="/ByType")
   List<BankAccount> findByType(@Param("t") AccountType accountType);
}
