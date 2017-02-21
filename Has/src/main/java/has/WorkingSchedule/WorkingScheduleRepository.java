package has.WorkingSchedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    Page<WorkingSchedule> findByStartDateGreaterThanAndEndDateLessThan(String startDate, String endDate, Pageable request);

    @Query("select ws from has.WorkingSchedule.WorkingSchedule ws where ws.startDate >= :startDate and ws.endDate <= :endDate and ws.employee.user.userRole.id = :roleId")
    Page<WorkingSchedule> findByRangeAndRole(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("roleId") long roleId, Pageable request);

    WorkingSchedule findByEmployeeIdAndStartDate(Long id, String startDate);
}
