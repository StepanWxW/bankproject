package ru.wxw.stepan.bankproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.wxw.stepan.bankproject.dto.TransactionCashOrderDTO;
import ru.wxw.stepan.bankproject.dto.TransactionDTO;
import ru.wxw.stepan.bankproject.dto.TransactionTransferDTO;
import ru.wxw.stepan.bankproject.model.Transaction;
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
        return transactionDTOS.size() != 0
                ? new ResponseEntity<>(transactionDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/self")
    public ResponseEntity<List<TransactionDTO>> transactionSelf(
            @RequestBody TransactionTransferDTO transactionTransferDTO){
        List<TransactionDTO> transactionDTOS = transactionService.transactionSelf(transactionTransferDTO);
        return transactionDTOS.size() != 2
                ? new ResponseEntity<>(transactionDTOS, HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(transactionDTOS, HttpStatus.OK);
    }
    @PostMapping("/toClient")
    public ResponseEntity<List<TransactionDTO>> transactionToClient(
            @RequestBody TransactionTransferDTO transactionTransferDTO){
        List<TransactionDTO> transactionDTOS = transactionService.transactionToClient(transactionTransferDTO);
        return transactionDTOS.size() != 2
                ? new ResponseEntity<>(transactionDTOS, HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(transactionDTOS, HttpStatus.OK);
    }
    @PostMapping("/cashOrder")
    public ResponseEntity<TransactionDTO> cashOrder(
            @RequestBody TransactionCashOrderDTO transactionCashOrderDTO){
        TransactionDTO transactionDTO = transactionService.cashOrder(transactionCashOrderDTO);
        return transactionDTO != null
                ? new ResponseEntity<>(transactionDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
