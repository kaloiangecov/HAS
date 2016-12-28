package has.User;

import has.Employee.Employee;
import has.Guest.Guest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kaloi on 12/17/2016.
 */
@Getter
@Setter
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String lastLogin;
    private String password;
    private String email;
    private String regDate;
    private String role;
    private String username;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Employee employee;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Guest guest;

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

    public User(String lastLogin, String password, String email, String regDate, String role, String username, Employee employee, Guest guest) {
        this.lastLogin = lastLogin;
        this.password = password;
        this.email = email;
        this.regDate = regDate;
        this.role = role;
        this.username = username;
        this.employee = employee;
        this.guest = guest;
    }
}
