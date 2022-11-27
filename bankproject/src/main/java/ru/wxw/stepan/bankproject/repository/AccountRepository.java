package ru.wxw.stepan.bankproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wxw.stepan.bankproject.model.Account;
import ru.wxw.stepan.bankproject.model.Client;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> getAccountByClientId(Client client);
    Account getAccountByNumber(Long number);
}
