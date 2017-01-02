package has.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kaloi on 12/28/2016.
 */
@Entity
@Getter
@Setter
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ROLE_ID")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userRole")
    private List<User> user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "PERMISSION_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "USER_ROLE_ID",
                    nullable = false, updatable = false) })
    private List<RolePermission> permissions;

    public UserRole() {
    }

    private String roleName;

    public UserRole(String roleName, List<RolePermission> permissions) {
        this.roleName = roleName;
        this.permissions = permissions;
    }

    @JsonIgnore
    public List<User> getUser() {
        return user;
    }
}
