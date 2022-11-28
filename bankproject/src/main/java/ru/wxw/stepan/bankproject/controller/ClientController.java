package ru.wxw.stepan.bankproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.wxw.stepan.bankproject.dto.AccountDTO;
import ru.wxw.stepan.bankproject.dto.ClientDTO;
import ru.wxw.stepan.bankproject.service.AccountService;
import ru.wxw.stepan.bankproject.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final AccountService accountService;


    @Autowired
    public ClientController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> getAllClientDTO() {
        List<ClientDTO> clientDTOList = clientService.getAll();
        return clientDTOList != null
                ? new ResponseEntity<>(clientDTOList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable("id") Long id) {
        ClientDTO clientDTO = clientService.findById(id);
        return clientDTO != null
                ? new ResponseEntity<>(clientDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountByClientId(@PathVariable("id") Long id) {
        List<AccountDTO> accountDTOS = accountService.getAccountByClientId(id);
        return accountDTOS.size() != 0
                ? new ResponseEntity<>(accountDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
