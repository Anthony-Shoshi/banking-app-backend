package com.groupfour.bankingapp.Services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import com.groupfour.bankingapp.Config.BeanFactory;
import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TransactionService transactionService;
    @InjectMocks
    private UserService userService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private BeanFactory beanFactory;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, customerRepository, passwordEncoder, jwtTokenProvider);
        transactionService = new TransactionService(transactionRepository, accountRepository, beanFactory, userService, request);
    }
//    @Test
//    void testTransferMoney() {
//        // Mock data
//        String fromAccountIban = "from_iban";
//        String toAccountIban = "to_iban";
//        double transferAmount = 100.0;
//
//        User user = new User(
//                "john@example.com",
//                "password123",
//                "John",
//                "Doe",
//                "1234567890",
//                "123456789",
//                UserType.CUSTOMER,
//                Gender.MALE,
//                "1990-01-01"
//        );
//        user.setUserId(1L);  // Ensure the user has an ID
//
//        Customer customer = new Customer(user, CustomerStatus.APPROVED);
//        Account fromAccount = new Account(customer, fromAccountIban, 1000.0, 5000.0, AccountType.CURRENT, true, 1000.0, AccountStatus.ACTIVE, "USD");
//        Account toAccount = new Account(customer, toAccountIban, 2000.0, 5000.0, AccountType.SAVING, true, 1000.0, AccountStatus.ACTIVE, "USD");
//
//        String token = "mockedToken";
//        when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
//        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
//
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                String.valueOf(user.getUserId()),
//                "",
//                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
//        );
//
//        // Mock behavior to return an Authentication instance
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//        when(jwtTokenProvider.getAuthentication(token)).thenReturn(authentication);
//
//        // Ensure the UserRepository returns the user when looked up by ID
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//
//        // Mock behavior
//        when(accountRepository.findByIBAN(fromAccountIban)).thenReturn(fromAccount);
//        when(accountRepository.findByIBAN(toAccountIban)).thenReturn(toAccount);
//        when(userService.getCurrentLoggedInUser(request)).thenReturn(user);
//        doNothing().when(accountRepository).save(any(Account.class));
//        when(transactionRepository.save(any(BankTransaction.class))).thenReturn(new BankTransaction());
//
//        // Call the method under test
//        BankTransactionPostDTO result = transactionService.transferMoney(fromAccountIban, toAccountIban, transferAmount);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(fromAccountIban, result.fromAccountIban());
//        assertEquals(toAccountIban, result.toAccountIban());
//        assertEquals(transferAmount, result.transferAmount());
//
//        // Verify that necessary methods were called
//        verify(accountRepository).findByIBAN(fromAccountIban);
//        verify(accountRepository).findByIBAN(toAccountIban);
//        verify(accountRepository, times(2)).save(any(Account.class));
//        verify(transactionRepository).save(any(BankTransaction.class));
//    }
    @Test
    void testGetAllTransactions() {
        // Mock data
        List<BankTransaction> transactions = new ArrayList<>();
        User user = new User("john@example.com", "password123", "John", "Doe", "1234567890", "123456789", UserType.CUSTOMER, Gender.MALE, "1990-01-01");
        Customer customer = new Customer(user, CustomerStatus.APPROVED);
        Account fromAccount = new Account(customer, "from_iban", 1000.0, 5000.0, AccountType.CURRENT, true, 1000.0, AccountStatus.ACTIVE, "USD");
        Account toAccount = new Account(customer, "to_iban", 2000.0, 5000.0, AccountType.SAVING, true, 1000.0, AccountStatus.ACTIVE, "USD");
        BankTransaction transaction1 = new BankTransaction(TransactionType.DEPOSIT, UserType.CUSTOMER, user, fromAccount, toAccount, 500.0, LocalDateTime.now(), TransactionStatus.SUCCESS);
        BankTransaction transaction2 = new BankTransaction(TransactionType.WITHDRAW, UserType.EMPLOYEE, user, fromAccount, toAccount, 200.0, LocalDateTime.now(), TransactionStatus.FAILED);
        transactions.add(transaction1);
        transactions.add(transaction2);

        // Mock behavior of transactionRepository.findAll()
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Call the method under test
        List<BankTransactionDTO> result = transactionService.getAllTransactions();

        // Assertions
        assertEquals(2, result.size());
        assertEquals(TransactionType.DEPOSIT, result.get(0).type());
        assertEquals(UserType.CUSTOMER, result.get(0).initiatedBy());
        assertEquals("John", result.get(0).firstName());
        assertEquals("Doe", result.get(0).lastName());
        assertEquals("from_iban", result.get(0).fromAccountIban());
        assertEquals("to_iban", result.get(0).toAccountIban());
        assertEquals(500.0, result.get(0).transferAmount());
    }

    @Test
    void testGetTransactionsByCustomerId() {
        // Mock data
        Long customerId = 1L;
        List<BankTransaction> transactions = new ArrayList<>();
        User user = new User("john@example.com", "password123", "John", "Doe", "1234567890", "123456789", UserType.CUSTOMER, Gender.MALE, "1990-01-01");
        Customer customer = new Customer(user, CustomerStatus.APPROVED);
        Account fromAccount = new Account(customer, "from_iban", 1000.0, 5000.0, AccountType.CURRENT, true, 1000.0, AccountStatus.ACTIVE, "USD");
        Account toAccount = new Account(customer, "to_iban", 2000.0, 5000.0, AccountType.SAVING, true, 1000.0, AccountStatus.ACTIVE, "USD");
        BankTransaction transaction1 = new BankTransaction(TransactionType.DEPOSIT, UserType.CUSTOMER, user, fromAccount, toAccount, 500.0, LocalDateTime.now(), TransactionStatus.SUCCESS);
        BankTransaction transaction2 = new BankTransaction(TransactionType.WITHDRAW, UserType.EMPLOYEE, user, fromAccount, toAccount, 200.0, LocalDateTime.now(), TransactionStatus.SUCCESS);
        transactions.add(transaction1);
        transactions.add(transaction2);

        // Mock behavior of transactionRepository.findByFromAccountCustomerCustomerId(customerId)
        when(transactionRepository.findByFromAccountCustomerCustomerId(customerId)).thenReturn(transactions);

        // Call the method under test
        Optional<List<BankTransactionDTO>> result = transactionService.getTransactionsByCustomerId(customerId);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    //need a check
    @Test
    void testGetTotalTransferredAmountToday() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setIBAN("from_iban");
        List<BankTransaction> transactions = new ArrayList<>();
        transactions.add(new BankTransaction(TransactionType.DEPOSIT, UserType.CUSTOMER, new User(), fromAccount, new Account(), 500.0, LocalDateTime.now(), TransactionStatus.SUCCESS));
        transactions.add(new BankTransaction(TransactionType.WITHDRAW, UserType.CUSTOMER, new User(), fromAccount, new Account(), 200.0, LocalDateTime.now(), TransactionStatus.SUCCESS));
        transactions.add(new BankTransaction(TransactionType.DEPOSIT, UserType.CUSTOMER, new User(), fromAccount, new Account(), 300.0, LocalDateTime.now().minusDays(1), TransactionStatus.SUCCESS));

        // Mock behavior of transactionRepository.findByFromAccountAndCurrentTimeBetween
        when(transactionRepository.findByFromAccountAndCurrentTimeBetween(
                eq(fromAccount),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(transactions);

        // Call the method under test
        double totalTransferredAmountToday = transactionService.getTotalTransferredAmountToday(fromAccount);

        // Assertions
        assertEquals(1000.0, totalTransferredAmountToday); // 500 + 200 + 300
    }
    //need a check
    @Test
    void testValidateDailyLimitExceeds() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setDailyLimit(1000.0); // Daily limit is 1000
        List<BankTransaction> transactions = new ArrayList<>();
        // Assuming no transactions today (total transferred amount today is implicitly zero)
        when(transactionRepository.findByFromAccountAndCurrentTimeBetween(fromAccount, LocalDateTime.of(LocalDate.now(), LocalDateTime.MIN.toLocalTime()), LocalDateTime.of(LocalDate.now(), LocalDateTime.MAX.toLocalTime())))
                .thenReturn(transactions);

        // Call the method under test and assert exception is thrown
        assertThrows(IllegalArgumentException.class, () -> transactionService.validateDailyLimit(fromAccount, 1200.0)); // Trying to transfer 300, which would exceed the daily limit
    }


    @Test
    void testValidateDailyLimitNotExceeds() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setDailyLimit(1000.0); // Daily limit is 1000
        List<BankTransaction> transactions = new ArrayList<>();
        // Assuming total transferred amount today is already 800
        when(transactionRepository.findByFromAccountAndCurrentTimeBetween(fromAccount, LocalDateTime.of(LocalDate.now(), LocalDateTime.MIN.toLocalTime()), LocalDateTime.of(LocalDate.now(), LocalDateTime.MAX.toLocalTime())))
                .thenReturn(transactions);

        // Call the method under test and assert no exception is thrown
        transactionService.validateDailyLimit(fromAccount, 150.0); // Trying to transfer 150, which is within the daily limit
        // No exception means the daily limit validation passed
    }
    @Test
    void testGetAccountByIban() {
        // Mock data
        String iban = "from_iban";
        Account account = new Account();
        account.setIBAN(iban);

        // Mock behavior of accountRepository.findByIBAN
        when(accountRepository.findByIBAN(iban)).thenReturn(account);

        // Call the method under test
        Account result = transactionService.getAccountByIban(iban);

        // Assertions
        assertNotNull(result);
        assertEquals(iban, result.getIBAN());
    }

    @Test
    void testValidateAccounts() {
        // Mock data
        Account fromAccount = null;
        Account toAccount = new Account(); // Assume toAccount is not null

        // Call the method under test and assert exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.validateAccounts(fromAccount, toAccount));

        // Assertions
        assertEquals("Source or destination account not found", exception.getMessage());
    }
    @Test
    void testValidateSufficientFundsSufficient() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setBalance(1000.0); // Balance is 1000
        double transferAmount = 500.0; // Transfer amount is 500

        // Call the method under test (no exception should be thrown)
        assertDoesNotThrow(() -> transactionService.validateSufficientFunds(fromAccount, transferAmount));
    }

    @Test
    void testValidateSufficientFundsInsufficient() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setBalance(500.0); // Balance is 500
        double transferAmount = 1000.0; // Transfer amount is 1000

        // Call the method under test and assert exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.validateSufficientFunds(fromAccount, transferAmount));

        // Assertions
        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    void testExecuteTransfer() {
        // Mock data
        Account fromAccount = new Account();
        fromAccount.setBalance(1000.0); // Initial balance is 1000
        Account toAccount = new Account();
        toAccount.setBalance(500.0); // Initial balance is 500
        double transferAmount = 200.0; // Transfer amount is 200

        // Call the method under test
        transactionService.executeTransfer(fromAccount, toAccount, transferAmount);

        // Assertions
        assertEquals(800.0, fromAccount.getBalance()); // 1000 - 200
        assertEquals(700.0, toAccount.getBalance()); // 500 + 200

        //   Verify that accountRepository.save was called for both accounts
        Mockito.verify(accountRepository).save(fromAccount);
        Mockito.verify(accountRepository).save(toAccount);
    }



    @Test
    void testCreateTransactionFailedInitiatorNull() {
        // Mock data
        Account fromAccount = new Account();
        Account toAccount = new Account();
        double transferAmount = 100.0;

        when(userService.getCurrentLoggedInUser(request)).thenReturn(null);

        // Call the method under test
        BankTransaction transaction = transactionService.createTransaction(fromAccount, toAccount, transferAmount);

        // Assertions
        assertNull(transaction);
    }

    @Test
    void testCreateBankTransactionPostDTO() {
        // Mock data
        String fromAccountIban = "from_iban";
        String toAccountIban = "to_iban";
        double transferAmount = 100.0;

        // Call the method under test
        BankTransactionPostDTO transactionPostDTO = transactionService.createBankTransactionPostDTO(fromAccountIban, toAccountIban, transferAmount);

        // Assertions
        assertNotNull(transactionPostDTO);
        assertEquals(fromAccountIban, transactionPostDTO.fromAccountIban());
        assertEquals(toAccountIban, transactionPostDTO.toAccountIban());
        assertEquals(transferAmount, transactionPostDTO.transferAmount());
    }

}





