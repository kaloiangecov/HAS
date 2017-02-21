package has.WorkingSchedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    Page<WorkingSchedule> findByStartDateGreaterThanAndEndDateLessThan(String startDate, String endDate, Pageable request);

    WorkingSchedule findByEmployeeIdAndStartDate(Long id, String startDate);
}
