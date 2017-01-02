package has.PersonalData;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kaloi on 12/23/2016.
 */
@Getter
@Setter
@Entity
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private String egn;
    private String fullName;
    private String identityExpireDate;
    private String identityIssueDate;
    private String identityIssuedBy;
    private String identityNumber;
    private String phone;

    public PersonalData(){

    }

    public PersonalData(String address, String egn, String fullName,
                        String identityExpireDate, String identityIssueDate,
                        String identityIssuedBy, String identityNumber, String phone) {
        this.address = address;
        this.egn = egn;
        this.fullName = fullName;
        this.identityExpireDate = identityExpireDate;
        this.identityIssueDate = identityIssueDate;
        this.identityIssuedBy = identityIssuedBy;
        this.identityNumber = identityNumber;
        this.phone = phone;
    }
}
