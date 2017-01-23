package has.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kaloi on 12/28/2016.
 */
@Entity
@Getter
@Setter
public class RolePermission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERMISSION_ID")
    private Long id;

    private String permission;

    public RolePermission(String permission) {
        this.permission = permission;
    }

    public RolePermission() {
    }

    //@ManyToMany(fetch = FetchType.EAGER, mappedBy = "permissions", cascade = CascadeType.ALL)
    //private List<UserRole> role;

    @Override
    public String getAuthority() {
        return permission;
    }
}
