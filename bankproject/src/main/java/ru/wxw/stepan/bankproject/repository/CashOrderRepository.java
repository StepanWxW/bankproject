package ru.wxw.stepan.bankproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wxw.stepan.bankproject.model.CashOrder;

import java.util.List;


@Repository
public interface CashOrderRepository extends JpaRepository<CashOrder, Long> {
        List<CashOrder> getCashOrdersByAccountId(Long id);
}
