package has.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Employee employee;

    public User() {

    }

    public User(Long id, String lastLogin, String password, String regDate, String role, String username) {
        this.id = id;
        this.lastLogin = lastLogin;
        this.password = password;
        this.regDate = regDate;
        this.role = role;
        this.username = username;
    }

}
