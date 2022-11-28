package ru.wxw.stepan.bankproject.dto;

import lombok.Data;

@Data
public class TransactionCashOrderDTO {
    Long number;
    String order;
    float amount;
    String password;
}
