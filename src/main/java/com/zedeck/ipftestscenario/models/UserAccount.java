package com.zedeck.ipftestscenario.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "user_accounts")
@SQLDelete(sql = "UPDATE user_accounts SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class UserAccount extends BaseEntity implements Serializable {

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String username;

    @Column(name = "userType")
    private String userType;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Basic(optional = true)
    @Column(name = "token_created_at")
    private LocalDateTime tokenCreatedAt = LocalDateTime.now();

    @JsonIgnore
    @Basic(optional = true)
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @JsonIgnore
    private int loginAttempts = 0;

    @JsonIgnore
    private LocalDateTime lastLoginAttempt;

    @JsonIgnore
    @Column(name = "refresh_token")
    private String refreshToken;

    @JsonIgnore
    @Column(name = "refresh_token_created_at")
    private LocalDateTime refreshTokenCreatedAt;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    public String fullName() {
        return  this.firstName  +" " + this.lastName ;
    }


    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        if (this.userType ==  null) {
            switch (this.userType) {
                case "EMPLOYEE":
                    permissions.add("EMPLOYEE_PERMISSION");
                    break;
                case "SUPER_ADMIN":
                    permissions.add("SUPER_ADMIN_PERMISSION");
                    break;
                // Add more cases if needed
            }
        }

        return permissions;
    }


}
