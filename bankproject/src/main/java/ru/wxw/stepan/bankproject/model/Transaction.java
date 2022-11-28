package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.*;


import java.sql.Timestamp;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_creation")
    private Timestamp timestamp;
    @Column(name = "amount")
    private Float amount;
    @Column(name = "order_name")
    private String order;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account accountId;
    @Column(name = "cash_order_id")
    private Long cashOrderId;
    @Column(name = "client_transfer_id")
    private Long clientTransferId;
    @Column(name ="result")
    private String result;
}
