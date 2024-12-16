package com.shepherd.sheps_project.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "users")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseModel{
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
//    private boolean isOauthSignIn;
//    @Column(name = "oauth_type")
//    @Enumerated(EnumType.STRING)
//    private OauthType oauthType;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;
    private boolean isEnabled;
}
