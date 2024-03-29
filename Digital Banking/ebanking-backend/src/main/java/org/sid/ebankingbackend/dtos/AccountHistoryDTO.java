package org.sid.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    public  String accountId;
    private double balance ;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    List<AccountOperationDTO> accountOperationDTOList;
}
