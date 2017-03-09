package has.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kaloi on 12/28/2016.
 */
@Entity(name = "PERMISSION")
@Getter
@Setter
public class RolePermission implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERMISSION_ID")
    private Long id;

    private String permission;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return permission;
    }
}
