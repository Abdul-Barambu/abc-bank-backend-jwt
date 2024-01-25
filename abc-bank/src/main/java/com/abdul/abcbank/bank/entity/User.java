package com.abdul.abcbank.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    @Id
    @SequenceGenerator(name = "bank_users_sequence", sequenceName = "bank_users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String gender;
    private String address;
    private String state;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String email;
//    private String password;
    private String phoneNumber;
    private String status;
    @CreationTimestamp
    private String createdAt;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;
}
