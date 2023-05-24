package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "teacherProfiles")
public class TeacherProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column
    private String fullname;
    @Lob
    @Column
    private byte[] avatar;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;


}
