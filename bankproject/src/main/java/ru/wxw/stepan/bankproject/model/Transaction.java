package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_creation")
    @CreationTimestamp
    private Timestamp timestamp;
    @Column(name = "amount")
    private float amount;
    @Column(name = "order")
    private TypeOrder order;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "cash_order_id")
    private Long cashOrderId;
    @Column(name = "client_transfer_id")
    private Long clientTransferId;

}
