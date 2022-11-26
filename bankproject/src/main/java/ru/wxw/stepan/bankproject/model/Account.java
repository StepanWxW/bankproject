package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client clientId;
    @Column(name = "number")
    private Long number;
    @Column(name = "amount")
    private float amount;
    @Column(name = "type")
    private AccountType type;
    @Column(name = "time_open")
    private Timestamp timestamp;
    @Column(name = "validity_period")
    private Timestamp timestampPeriod;
}
