package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends CommonResult {
    private int id;
    private String content;
    private int author;
    private Date time;
    private int sid;
    private String dateTime;
    private String authorName;
    private String targetName;
    private int tid;
    private int target;
    private int identity;
}
