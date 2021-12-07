package com.example.sqljpasqlserver.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "password")
    private String password;


    @Column(name = "avatar")
    private String avatar;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-M-yyyy")
    private Date birthday;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_user_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "role", nullable = false, columnDefinition = "varchar(255) default 'USER'")
    private Set<Role> roles=new HashSet<>();






}
