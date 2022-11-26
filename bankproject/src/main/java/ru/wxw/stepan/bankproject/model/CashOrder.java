package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "cash_order")
public class CashOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order")
    private TypeOrder order;
    @Column(name = "amount")
    private float amount;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name ="result")
    private boolean result;
    @Column(name = "time_creation")
    @CreationTimestamp
    private Timestamp timestamp;
}
