package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article extends CommonResult {
    private int id;
    private String title;
    private int author;
    private String content;
    private Date time;
    private int level;
    private int tag;
    private String tagName;
    private String authorName;
    private String dateTime;
    private int identity;
}
