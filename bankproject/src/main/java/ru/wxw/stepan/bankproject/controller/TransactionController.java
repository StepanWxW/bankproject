package ru.wxw.stepan.bankproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.wxw.stepan.bankproject.dto.TransactionDTO;
import ru.wxw.stepan.bankproject.dto.TransactionSelfDTO;
import ru.wxw.stepan.bankproject.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping("/{number}")
    public ResponseEntity<List<TransactionDTO>> getById(@PathVariable("number") Long number) {
        List<TransactionDTO> transactionDTOS = transactionService.getTransactionByNumber(number);
        return transactionDTOS != null
                ? new ResponseEntity<>(transactionDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/self")
    public ResponseEntity<List<TransactionDTO>> transactionSelf(
            @RequestBody TransactionSelfDTO transactionSelfDTO){
        List<TransactionDTO> transactionDTOS = transactionService.transactionSelf(transactionSelfDTO);
        return null;
    }
}
