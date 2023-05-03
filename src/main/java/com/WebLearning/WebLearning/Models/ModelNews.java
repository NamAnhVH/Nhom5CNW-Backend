package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "news")
public class ModelNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true)
    private String title;
    @Column
    private String time;
    @Column
    private String description;
    @Column
    private String detail;
}
