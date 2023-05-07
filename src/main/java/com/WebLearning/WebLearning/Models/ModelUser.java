package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class ModelUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column
    private String password;

    @Column
    private String fullname;
    @Column
    private String role;
    @Column
    private boolean approved;
    @Column
    private boolean locked;

//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<Image> images;
}
