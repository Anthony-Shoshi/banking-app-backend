package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Account;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<AccountsGetDTO> getAllAccountDetails(){
        return accountRepository.findAll().stream()
                .map(account -> new AccountsGetDTO(
                        account.getAccountId(),
                        account.getCustomer().getCustomerId(),
                        account.getCustomer().getUser().getFirstName(),
                        account.getIBAN(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getStatus(),
                        account.getAbsoluteLimit(),
                        account.getDailyLimit())
                      )
                .collect(Collectors.toList());
    }
}

