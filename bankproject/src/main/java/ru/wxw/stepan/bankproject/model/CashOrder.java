package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_order")
public class CashOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_name")
    private String order;
    @Column(name = "amount")
    private float amount;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name ="result")
    private String result;
    @Column(name = "time_creation")
    @CreationTimestamp
    private Timestamp timestamp;
}
