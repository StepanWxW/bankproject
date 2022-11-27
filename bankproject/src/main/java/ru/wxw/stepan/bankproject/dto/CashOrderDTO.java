package ru.wxw.stepan.bankproject.dto;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class CashOrderDTO {

    private String order;
    private float amount;
    private Long accountId;
    private String result;
    private Timestamp timestamp;
}
