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
    private String imagePage;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String detail;
}
