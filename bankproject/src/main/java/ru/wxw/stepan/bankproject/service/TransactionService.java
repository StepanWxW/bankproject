package ru.wxw.stepan.bankproject.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wxw.stepan.bankproject.dto.TransactionCashOrderDTO;
import ru.wxw.stepan.bankproject.dto.TransactionDTO;
import ru.wxw.stepan.bankproject.dto.TransactionTransferDTO;
import ru.wxw.stepan.bankproject.model.Account;
import ru.wxw.stepan.bankproject.model.CashOrder;
import ru.wxw.stepan.bankproject.model.Client;
import ru.wxw.stepan.bankproject.model.Transaction;
import ru.wxw.stepan.bankproject.repository.AccountRepository;
import ru.wxw.stepan.bankproject.repository.CashOrderRepository;
import ru.wxw.stepan.bankproject.repository.TransactionRepository;
import ru.wxw.stepan.bankproject.util.StringStore;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CashOrderRepository cashOrderRepository;

    @Autowired
    public TransactionService(ModelMapper modelMapper, TransactionRepository transactionRepository, AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CashOrderRepository cashOrderRepository) {
        this.modelMapper = modelMapper;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.cashOrderRepository = cashOrderRepository;
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
    public TransactionDTO cashOrder(TransactionCashOrderDTO transactionCashOrderDTO) {
        String order = transactionCashOrderDTO.getOrder();
        if (order == null) return null;
        Account account = accountRepository.getAccountByNumber(transactionCashOrderDTO.getNumber());
        if(order.equals("ADD")){
            account.setAmount(account.getAmount()+transactionCashOrderDTO.getAmount());
            return saveCashOrderAndTransaction(order, transactionCashOrderDTO, account, StringStore.OK);
        }
        if(order.equals("WITHDRAW")){
            if(passwordIsCorrect(transactionCashOrderDTO.getPassword(), account)){
                float amount = account.getAmount() - transactionCashOrderDTO.getAmount();
                if (amount < 0) {
                    return saveCashOrderAndTransaction(order, transactionCashOrderDTO, account, StringStore.NOMONEY);
                }
                account.setAmount(amount);
                return saveCashOrderAndTransaction(order, transactionCashOrderDTO, account, StringStore.OK);
            }
            return saveCashOrderAndTransaction(order,transactionCashOrderDTO,account, StringStore.BADPASSWORD);
        }
        return null;

    }
    @Transactional
    public List<TransactionDTO> transactionSelf(TransactionTransferDTO transactionTransferDTO) {
        Transaction transactionSend;
        Transaction transactionCome;
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        Account accountSend = accountRepository.getAccountByNumber(transactionTransferDTO.getNumberSend());
        if (accountSend == null) return transactionDTOS;

        if (passwordIsCorrect(transactionTransferDTO.getPassword(), accountSend)) {
            Account accountCome = accountRepository.getAccountByNumber(transactionTransferDTO.getNumberCome());
            if (accountCome == null) return transactionDTOS;

            if (checkAccountForOneClient(accountSend.getClientId(), accountCome.getClientId())) {
                float amount = accountSend.getAmount() - transactionTransferDTO.getAmount();
                if (amount < 0) {
                    return saveTransaction(transactionTransferDTO, accountSend, StringStore.NOMONEY, transactionDTOS);
                }
                accountSend.setAmount(amount);
                accountCome.setAmount(accountCome.getAmount() + transactionTransferDTO.getAmount());

                transactionSend = set(transactionTransferDTO.getAmount(), StringStore.TRANSFER, accountSend, accountSend.getClientId().getId(), StringStore.OK);
                transactionCome = set(transactionTransferDTO.getAmount(), StringStore.ADD, accountCome, accountSend.getClientId().getId(), StringStore.OK);

                transactionRepository.save(transactionSend);
                transactionRepository.save(transactionCome);

                transactionDTOS.add(modelMapper.map(transactionCome, TransactionDTO.class));
                transactionDTOS.add(modelMapper.map(transactionSend, TransactionDTO.class));
                return transactionDTOS;
            } else {
                return saveTransaction(transactionTransferDTO, accountSend, StringStore.DIFFERENTACCOUNT, transactionDTOS);
            }
        }
        return saveTransaction(transactionTransferDTO, accountSend, StringStore.BADPASSWORD, transactionDTOS);
    }

    @Transactional
    public List<TransactionDTO> transactionToClient(TransactionTransferDTO transactionTransferDTO) {
        Transaction transactionSend;
        Transaction transactionCome;
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        Account accountSend = accountRepository.getAccountByNumber(transactionTransferDTO.getNumberSend());
        if (accountSend == null) return transactionDTOS;

        if (passwordIsCorrect(transactionTransferDTO.getPassword(), accountSend)) {
            Account accountCome = accountRepository.getAccountByNumber(transactionTransferDTO.getNumberCome());
            if (accountCome == null) return transactionDTOS;

            float amount = accountSend.getAmount() - transactionTransferDTO.getAmount();
            if (amount < 0) {
                return saveTransaction(transactionTransferDTO, accountSend, StringStore.NOMONEY, transactionDTOS);
            }
            accountSend.setAmount(amount);
            accountCome.setAmount(accountCome.getAmount() + transactionTransferDTO.getAmount());

            transactionSend = set(transactionTransferDTO.getAmount(), StringStore.TRANSFER, accountSend, accountSend.getClientId().getId(), StringStore.OK);
            transactionCome = set(transactionTransferDTO.getAmount(), StringStore.ADD, accountCome, accountSend.getClientId().getId(), StringStore.OK);

            transactionRepository.save(transactionSend);
            transactionRepository.save(transactionCome);

            transactionDTOS.add(modelMapper.map(transactionCome, TransactionDTO.class));
            transactionDTOS.add(modelMapper.map(transactionSend, TransactionDTO.class));
            return transactionDTOS;

        }
        return saveTransaction(transactionTransferDTO, accountSend, StringStore.BADPASSWORD, transactionDTOS);
    }

    private List<TransactionDTO> saveTransaction(TransactionTransferDTO transactionTransferDTO, Account accountSend, String nomoney, List<TransactionDTO> transactionDTOS) {
        Transaction transactionSend = set(transactionTransferDTO.getAmount(), StringStore.TRANSFER, accountSend, accountSend.getClientId().getId(), nomoney);
        transactionRepository.save(transactionSend);
        transactionDTOS.add(modelMapper.map(transactionSend, TransactionDTO.class));
        return transactionDTOS;
    }

    private TransactionDTO saveCashOrderAndTransaction(String order, TransactionCashOrderDTO transactionCashOrderDTO, Account account, String ok) {
        CashOrder cashOrder = setCashOrder(order, transactionCashOrderDTO.getAmount(), account.getNumber(), ok);
        cashOrder = cashOrderRepository.save(cashOrder);
        Transaction transaction = setCashTransactional(transactionCashOrderDTO.getAmount(), order, account, cashOrder.getId(), ok);
        transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    private boolean passwordIsCorrect(String password, Account account) {
        Client client = account.getClientId();
        String passwordClient = client.getPassword();
        return bCryptPasswordEncoder.matches(password, passwordClient);
    }

    private boolean checkAccountForOneClient(Client clientSend, Client clientCome) {
        return clientSend.equals(clientCome);
    }
    private CashOrder setCashOrder(String order, float amount, Long id, String result){
        CashOrder cashOrder = new CashOrder();
        cashOrder.setOrder(order);
        cashOrder.setAccountId(id);
        cashOrder.setAmount(amount);
        cashOrder.setResult(result);
        cashOrder.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return cashOrder;
    }
    private Transaction setCashTransactional(float amount, String order, Account account, Long cashOrderId, String result) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transaction.setAmount(amount);
        transaction.setOrder(order);
        transaction.setAccountId(account);
        transaction.setCashOrderId(cashOrderId);
        transaction.setResult(result);
        return transaction;
    }
    private Transaction set(float amount, String order, Account account, Long clientTransferId, String result) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transaction.setAmount(amount);
        transaction.setOrder(order);
        transaction.setAccountId(account);
        transaction.setClientTransferId(clientTransferId);
        transaction.setResult(result);
        return transaction;
    }



}

