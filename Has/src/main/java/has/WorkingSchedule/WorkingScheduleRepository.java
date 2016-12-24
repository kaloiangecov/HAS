package has.WorkingSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
}
