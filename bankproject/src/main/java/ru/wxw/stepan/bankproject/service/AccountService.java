package ru.wxw.stepan.bankproject.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wxw.stepan.bankproject.dto.AccountDTO;
import ru.wxw.stepan.bankproject.model.Client;
import ru.wxw.stepan.bankproject.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    @Autowired
    public AccountService(ModelMapper modelMapper, AccountRepository accountRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountByClientId(Long id){
        Client client = new Client();
        client.setId(id);
        return accountRepository.getAccountByClientId(client)
                .stream()
                .map(p -> modelMapper.map(p, AccountDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
