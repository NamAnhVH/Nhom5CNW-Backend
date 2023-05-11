package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

//    @Column
//    private String fullname;
    @Column
    private String role;
    @Column
    private boolean approved;
    @Column
    private boolean locked;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
//    private ModelProfile profile;


//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<Image> images;
}
