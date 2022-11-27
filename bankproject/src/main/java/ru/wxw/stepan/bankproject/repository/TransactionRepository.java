package ru.wxw.stepan.bankproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wxw.stepan.bankproject.model.Account;
import ru.wxw.stepan.bankproject.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> getTransactionByAccountId(Account account);
}
