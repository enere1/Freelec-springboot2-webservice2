package com.leesungon.book.springboot.domain.posts.user;


import com.leesungon.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String pictrue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email , String pictrue, Role role) {
        this.name = name;
        this.email = email;
        this.pictrue = pictrue;
        this.role = role;
    }

    public User update(String name, String pictrue){
        this.name = name;
        this.pictrue = pictrue;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }





}
