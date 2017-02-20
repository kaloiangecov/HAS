package has.PersonalData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by kaloi on 12/23/2016.
 */
@Getter
@Setter
@Entity(name = "PERSONAL_DATA")
@Scope("session")
public class PersonalData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERSONAL_DATA_ID")
    private Long id;

    @NotNull
    @Size(min = 3, max = 200)
    private String address;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "[0-9]+")
    private String egn;

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "[a-zA-Z]+")
    private String fullName;

    @NotNull
    private String identityExpireDate;

    @NotNull
    private String identityIssueDate;

    @NotNull
    private String identityIssuedBy;

    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = "[0-9]+")
    private String identityNumber;

    @NotNull
    @Pattern(regexp = "[0-9+]+")
    private String phone;

    public PersonalData() {

    }
}
