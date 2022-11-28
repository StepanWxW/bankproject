package ru.wxw.stepan.bankproject.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wxw.stepan.bankproject.dto.CashOrderDTO;
import ru.wxw.stepan.bankproject.model.Account;

import ru.wxw.stepan.bankproject.model.CashOrder;
import ru.wxw.stepan.bankproject.repository.AccountRepository;
import ru.wxw.stepan.bankproject.repository.CashOrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashOrderService {
    private final ModelMapper modelMapper;
    private final CashOrderRepository cashOrderRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public CashOrderService(ModelMapper modelMapper, CashOrderRepository cashOrderRepository, AccountRepository accountRepository) {
        this.modelMapper = modelMapper;
        this.cashOrderRepository = cashOrderRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public List<CashOrderDTO> getCashOrdersByNumber(Long number) {
        Account account = accountRepository.getAccountByNumber(number);
        if (account == null) return null;
        List<CashOrder> cashOrders = cashOrderRepository.getCashOrdersByAccountId(account.getNumber());
        return cashOrders.stream()
                .map(p -> modelMapper.map(p, CashOrderDTO.class))
                .collect(Collectors.toList());
    }
}
