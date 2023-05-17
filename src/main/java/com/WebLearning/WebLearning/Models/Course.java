package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column
    private String name;
    @Lob
    @Column
    private byte[] image;
    @Column
    private String time;
    @Column(columnDefinition = "TEXT")
    private String introduction;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private String courseType;
    @Column
    private boolean approved;
    @Column
    private boolean locked;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;
}
