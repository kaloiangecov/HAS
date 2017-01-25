package has.User;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kaloi on 12/28/2016.
 */
@Entity(name = "ROLE")
@Getter
@Setter
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ROLE_ID")
    private Long id;

    @Column(name = "ROLENAME")
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ROLE_PERMISSION", joinColumns = {
            @JoinColumn(name = "USER_ROLE_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PERMISSION_ID",
                    nullable = false, updatable = false)})
    private List<RolePermission> permissions;

    public UserRole() {
    }

    public UserRole(String roleName, List<RolePermission> permissions) {
        this.roleName = roleName;
        this.permissions = permissions;
    }

    //@JsonIgnore
    public List<RolePermission> getPermissions() {
        return permissions;
    }
}
