package has.PersonalData;

import has.Exceptions.IdentityNumberAlreadyExists;
import has.Utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 1/2/2017.
 */
@Service
public class PersonalDataService {

    @Autowired
    private PersonalDataRepository repo;

    public PersonalData save(PersonalData personalData) throws Exception {
        if (repo.findByEgn(personalData.getEgn()) != null) {
            throw new IdentityNumberAlreadyExists(personalData.getEgn());
        }
        if (!Validator.isValidIssueDate(personalData.getIdentityIssueDate(), personalData.getIdentityExpireDate())) {
            throw new Exception("Invalid issue date");
        }
        return repo.save(personalData);
    }

    public List<PersonalData> getAllPersonalData() {
        return repo.findAll();
    }

    public PersonalData findById(Long id) throws Exception {
        PersonalData personalData = repo.findOne(id);
        validateIdNotNull(personalData);
        return personalData;
    }

    public PersonalData remove(Long id) throws Exception {
        PersonalData personalData = repo.findOne(id);
        validateIdNotNull(personalData);
        repo.delete(personalData);
        return personalData;
    }

    public PersonalData update(Long id, PersonalData personalData) throws Exception {
        PersonalData dbPersonalData = repo.findOne(id);
        validateIdNotNull(dbPersonalData);

        dbPersonalData.setIdentityNumber(personalData.getIdentityNumber());
        dbPersonalData.setIdentityIssuedBy(personalData.getIdentityIssuedBy());
        dbPersonalData.setAddress(personalData.getAddress());
        dbPersonalData.setEgn(personalData.getEgn());
        dbPersonalData.setFullName(personalData.getFullName());
        dbPersonalData.setIdentityExpireDate(personalData.getIdentityExpireDate());
        dbPersonalData.setIdentityIssueDate(personalData.getIdentityIssueDate());
        dbPersonalData.setPhone(personalData.getPhone());

        return repo.save(dbPersonalData);
    }

    private void validateIdNotNull(PersonalData personalData) throws Exception {
        if (personalData == null) {
            throw new Exception("There is no personal-data with such ID");
        }
    }
}
