package has.Guest;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private String EGN;
    private String email;
    private String fullName;
    private String idExpireDate;
    private String idIssueDate;
    private String idIssuedBy;
    private String idNumber;
    private String phone;
    private String status;
//    private User user;
//    public User m_User;
//    public ReservationGuest m_ReservationGuest;

    public Guest(){

    }
}
