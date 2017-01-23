package has.PersonalData;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotNull
    @Size(min = 3, max = 100)
    private String address;
    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "[0-9]")
    private String egn;
    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "[a-zA-Z]")
    private String fullName;
    @NotNull
    private String identityExpireDate;
    @NotNull
    private String identityIssueDate;
    @NotNull
    private String identityIssuedBy;
    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = "[0-9]")
    private String identityNumber;
    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "[0-9]")
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
