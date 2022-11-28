package ru.wxw.stepan.bankproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wxw.stepan.bankproject.dto.CashOrderDTO;
import ru.wxw.stepan.bankproject.service.CashOrderService;

import java.util.List;

@RestController
@RequestMapping("/cash_order")
public class CashOrderController {
    private final CashOrderService cashOrderService;

    public CashOrderController(CashOrderService cashOrderService) {
        this.cashOrderService = cashOrderService;
    }

    @GetMapping("/{number}")
    public ResponseEntity<List<CashOrderDTO>> getById(@PathVariable("number") Long number) {
        List<CashOrderDTO> cashOrderDTOS = cashOrderService.getCashOrdersByNumber(number);
        return cashOrderDTOS != null
                ? new ResponseEntity<>(cashOrderDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
