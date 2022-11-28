package ru.wxw.stepan.bankproject.dto;

import lombok.Data;

@Data
public class TransactionTransferDTO {
    Long numberSend;
    Long numberCome;
    float amount;
    String password;
}
