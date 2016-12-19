package has.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 12/19/2016.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
