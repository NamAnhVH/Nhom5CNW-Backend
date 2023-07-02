package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "studentProfiles")
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column
    private String fullname;
    @Lob
    @Column
    private byte[] avatar;
    @Column
    private String gender;
    @Column
    private Date birthDate;
    @Column
    private String literacy;
    @Column
    private String address;
    @Column
    private String hobby;
    @Column
    private String achivement;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;


}
