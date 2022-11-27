package ru.wxw.stepan.bankproject.dto;

import lombok.Data;

@Data
public class TransactionSelfDTO {
    Long numberSend;
    Long numberCome;
    float amount;
    String password;
}
