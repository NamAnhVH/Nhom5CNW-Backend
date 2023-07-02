package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private LocalDateTime time;
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
    @JoinColumn(name = "teacherProfileId", nullable = false)
    private TeacherProfile teacher;


}
