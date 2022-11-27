package ru.wxw.stepan.bankproject.dto;

import lombok.Data;


import java.sql.Timestamp;
@Data
public class AccountDTO {
    private Long number;
    private float amount;
    private String type;
    private Timestamp timestamp;
    private Timestamp timestampPeriod;
}
