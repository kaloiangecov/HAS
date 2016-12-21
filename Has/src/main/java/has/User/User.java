package has.User;

import has.Employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by kaloi on 12/17/2016.
 */
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String lastLogin;
    private String password;
    private String regDate;
    private String role;
    private String username;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
//    @JsonIgnore
    //TODO: adding json ignore to the declaration prevents the object from being posted,
    // the setter getter should be overwritten and anoteded with json ignore to make it work properly
    private Employee employee;

    public User() {

    }

    public User(String lastLogin, String password, String regDate, String role, String username, Employee employee) {
        this.lastLogin = lastLogin;
        this.password = password;
        this.regDate = regDate;
        this.role = role;
        this.username = username;
        this.employee = employee;
    }

    public User(String lastLogin, String password, String regDate, String role, String username) {
        this.lastLogin = lastLogin;
        this.password = password;
        this.regDate = regDate;
        this.role = role;
        this.username = username;
    }

}
