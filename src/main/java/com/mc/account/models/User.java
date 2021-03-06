package com.mc.account.models;

import com.mc.security.jwt.blacklist.JwtRevokedToken;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * @author Wenyu
 * @since 2/11/17
 */
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    private boolean enabled;

    @DateTimeFormat
    @NotNull
    private Date dateCreated;

    @NotNull
    private String externalId = UUID.randomUUID().toString();

    @OneToMany(targetEntity = JwtRevokedToken.class, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<JwtRevokedToken> jwtRevokedTokens;

    public User() {

    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<JwtRevokedToken> getJwtRevokedTokens() {
        return this.jwtRevokedTokens;
    }

    public void setJwtRevokedTokens(Set<JwtRevokedToken> jwtRevokedTokens) {
        this.jwtRevokedTokens = jwtRevokedTokens;
    }
}
