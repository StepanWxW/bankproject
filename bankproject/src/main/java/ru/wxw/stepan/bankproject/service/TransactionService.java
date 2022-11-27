package ru.wxw.stepan.bankproject.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wxw.stepan.bankproject.dto.TransactionDTO;
import ru.wxw.stepan.bankproject.dto.TransactionSelfDTO;
import ru.wxw.stepan.bankproject.model.Account;
import ru.wxw.stepan.bankproject.model.Client;
import ru.wxw.stepan.bankproject.model.Transaction;
import ru.wxw.stepan.bankproject.repository.AccountRepository;
import ru.wxw.stepan.bankproject.repository.ClientRepository;
import ru.wxw.stepan.bankproject.repository.TransactionRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public TransactionService(ModelMapper modelMapper, TransactionRepository transactionRepository, AccountRepository accountRepository, ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.modelMapper = modelMapper;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionByNumber(Long number) {
        Account account = accountRepository.getAccountByNumber(number);
        List<Transaction> transactions = transactionRepository.getTransactionByAccountId(account);
        return transactions.stream()
                .map(p -> modelMapper.map(p, TransactionDTO.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<TransactionDTO> transactionSelf(TransactionSelfDTO transactionSelfDTO) {
        Account accountSend = accountRepository.getAccountByNumber(transactionSelfDTO.getNumberSend());

        if(passwordIsCorrect(transactionSelfDTO, accountSend)){
            Account accountCome = accountRepository.getAccountByNumber(transactionSelfDTO.getNumberSend());
            if(accountCome.getClientId().equals(accountSend.getClientId())){
                float amount = accountSend.getAmount();

            }


            Transaction transactionSend = new Transaction();
        }
        return null;
    }

    private boolean passwordIsCorrect(TransactionSelfDTO transactionSelfDTO, Account account) {
        Client client = account.getClientId();
        String passwordClient = client.getPassword();
        return bCryptPasswordEncoder.matches(transactionSelfDTO.getPassword(), passwordClient);
    }
}
