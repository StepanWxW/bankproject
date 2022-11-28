package ru.wxw.stepan.bankproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private String type;
    @Column(name = "time_open")
    private Timestamp timestamp;
    @Column(name = "validity_period")
    private Timestamp timestampPeriod;
    @OneToMany(mappedBy = "accountId", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
