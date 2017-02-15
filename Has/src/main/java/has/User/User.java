package has.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import has.Roles.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Getter
@Setter
@Entity(name = "T_USER")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long id;

    private String lastLogin;

    @NotNull
    @Size(min = 6, max = 16)
//    @Pattern(regexp = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}")
    private String password;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String regDate;

    @NotNull
    @Size(min = 3, max = 16)
    private String username;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "USER_ROLE_ID",
                    nullable = false)})
    private UserRole userRole;

    public User() {

    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.addAll((Collection<? extends GrantedAuthority>) userRole.getPermissions());
        return authList;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public UserRole getUserRole() {
        return userRole;
    }


    @JsonProperty
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
