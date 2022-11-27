package ru.wxw.stepan.bankproject.dto;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class TransactionDTO {
    private Long id;
    private Timestamp timestamp;
    private float amount;
    private String order;
    private Long accountId;
    private Long cashOrderId;
    private Long clientTransferId;
    private String result;
}
