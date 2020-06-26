package com.example.demo.entity;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class User {
    private int uid;
    private String name;
    private String pwd;
    private int level;
    private int exp;
    private int identity;
    private List<String> tagName;
}
