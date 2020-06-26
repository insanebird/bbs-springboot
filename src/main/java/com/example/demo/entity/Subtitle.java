package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle extends CommonResult {
    private int id;
    private String content;
    private int author;
    private Date time;
    private int tid;
    private String dateTime;
    private String authorName;
    private int identity;
}
