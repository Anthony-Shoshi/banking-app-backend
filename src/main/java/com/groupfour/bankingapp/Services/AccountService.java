package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Account;
import com.groupfour.bankingapp.Models.AccountType;
import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;


@Service
public class AccountService {
    private final AccountRepository accountRepository;

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
                        account.getCustomer().getStatus(),
                        account.getAbsoluteLimit(),
                        account.getDailyLimit())
                      )
                .collect(Collectors.toList());
    }

    public void createAccount(Customer customer, ApproveSignupPutDTO approveSignupPutDTO) throws RuntimeException{
                create(customer, AccountType.CURRENT, approveSignupPutDTO.absoluteLimitForCurrent(), approveSignupPutDTO.dailyLimit());
                create(customer, AccountType.SAVING, approveSignupPutDTO.absoluteLimitForSaving() , approveSignupPutDTO.dailyLimit());
    }
    private void create(Customer customer, AccountType accountType, Double absoluteLimit, Double dailyLimit) {
        Account account = new Account();
        account.setCustomer(customer);
        account.setIBAN(generateUniqueIBAN());
        account.setBalance(0.00);
        account.setAccountType(accountType);
        account.setAbsoluteLimit(absoluteLimit);
        account.setIsActive(true);
        account.setDailyLimit(dailyLimit);
        account.setCurrency("€");
        accountRepository.save(account);
    }
    private String generateUniqueIBAN() {
        int generationAttempts = 0;
        String iban;
        do {
            generationAttempts++;
            if (generationAttempts > 100) {
                throw new RuntimeException("Failed to generate a unique IBAN.");
            }
            iban = generateIBAN();
        } while (ibanExists(iban));

        return iban;
    }

    private String generateIBAN() {
        String firstDigits = String.format("%02d", new Random().nextInt(100));
        String lastDigits = String.format("%08d", new Random().nextInt(100000000));
        return "NL" + firstDigits + "INHO0" + lastDigits;
    }

    private boolean ibanExists(String iban) {
        return accountRepository.existsByIBAN(iban);
    }


    public List<String> getIbansByCustomerName(String firstName, String lastName) {
        List<Account> accounts = accountRepository.findByCustomerFirstNameAndLastName(firstName, lastName);
        return accounts.stream().map(Account::getIBAN).collect(Collectors.toList());
    }

    public void updateDailyLimit(Long accountId, double dailyLimit) throws RuntimeException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        account.setDailyLimit(dailyLimit);
        accountRepository.save(account);
    }

}



