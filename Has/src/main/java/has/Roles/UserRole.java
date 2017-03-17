package has.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kaloi on 12/28/2016.
 */
@Entity(name = "ROLE")
@Getter
@Setter
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ROLE_ID")
    private Long id;

    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE_PERMISSION", joinColumns = {
            @JoinColumn(name = "USER_ROLE_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PERMISSION_ID",
                    nullable = false)})
    private List<RolePermission> permissions;

    @JsonIgnore
    public List<RolePermission> getPermissions() {
        return permissions;
    }
}
