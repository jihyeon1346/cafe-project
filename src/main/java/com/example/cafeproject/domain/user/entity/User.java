package com.example.cafeproject.domain.user.entity;

import com.example.cafeproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(length = 15)
    private String phone;

    public User(String nickname, String email, String password, String address, String phone) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }
}
