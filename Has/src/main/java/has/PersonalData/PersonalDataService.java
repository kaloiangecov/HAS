package has.PersonalData;

import has.Exceptions.IdentityNumberAlreadyExists;
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

    public PersonalData save(PersonalData personalData) throws IdentityNumberAlreadyExists {
        if (repo.findByEgn(personalData.getEgn()) != null) {
            throw new IdentityNumberAlreadyExists(personalData.getEgn());
        }
        return repo.save(personalData);
    }

    public List<PersonalData> getAllPersonalData() {
        return repo.findAll();
    }

    public PersonalData findById(Long id) throws Exception {
        PersonalData personalData = repo.findOne(id);
        if (personalData == null) {
            throw new Exception("There is no personal-data with such ID");
        }
        return personalData;
    }

    public PersonalData remove(Long id) throws Exception {
        PersonalData personalData = repo.findOne(id);
        if (personalData == null) {
            throw new Exception("There is no personal-data with such ID");
        }
        repo.delete(personalData);
        return personalData;
    }

    public PersonalData update(Long id, PersonalData personalData) throws Exception {
        PersonalData dbPersonalData = repo.findOne(id);
        if (dbPersonalData == null) {
            throw new Exception("There is no personal-data with such ID");
        }
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
}
