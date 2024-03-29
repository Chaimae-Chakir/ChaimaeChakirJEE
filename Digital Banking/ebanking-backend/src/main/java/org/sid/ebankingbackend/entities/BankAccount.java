package org.sid.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
//@Inheritance(strategy = InheritanceType.SiNGLE_TABLE) plus pratique
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) dans ce cas on commente @DiscriminatorColumn
// @Inheritance(strategy = InheritanceType.JOINED) dans ce cas on commente @DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",  length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAccount {

    @Id
    private String id;
    private double balance;
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperationList;
}
