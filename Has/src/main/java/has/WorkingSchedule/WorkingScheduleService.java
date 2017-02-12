package has.WorkingSchedule;

import has.Employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Service
public class WorkingScheduleService {

    @Autowired
    private WorkingScheduleRepository repo;

    public WorkingSchedule save(WorkingSchedule schedule) {

        return repo.save(schedule);
    }

    public List<WorkingSchedule> getAllSchedules() {
        List<WorkingSchedule> schedules = repo.findAll();

        for (WorkingSchedule schedule : schedules)
            schedule = removeRecursions(schedule);

        return schedules;
    }

    public Page<WorkingSchedule> searchSchedule(int start, int length, String startDate, String endDate, Long roleID) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");

        Page<WorkingSchedule> schedulePage = repo.findByStartDateGreaterThanAndEndDateLessThan(startDate, endDate, request);
        //Page<WorkingSchedule> schedulePage = repo.findAll(request);

        for (WorkingSchedule schedule : schedulePage)
            schedule = removeRecursions(schedule);

        return schedulePage;
    }

    public WorkingSchedule findById(Long id) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        if (dbSchedule == null) {
            throw new Exception("There is no schedule with such ID");
        }

        return removeRecursions(dbSchedule);
    }

    public WorkingSchedule remove(Long id) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        if (dbSchedule == null) {
            throw new Exception("There is no schedule with such ID");
        }
        repo.delete(dbSchedule);

        return removeRecursions(dbSchedule);
    }

    public WorkingSchedule update(Long id, WorkingSchedule schedule) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        if (dbSchedule == null) {
            throw new Exception("There is no schedule with such ID");
        }
        dbSchedule.setStartDate(schedule.getStartDate());
        dbSchedule.setEndDate(schedule.getEndDate());
        dbSchedule.setShift(schedule.getShift());
        dbSchedule.setEmployee(schedule.getEmployee());

        return removeRecursions(repo.save(dbSchedule));
    }

    private WorkingSchedule removeRecursions(WorkingSchedule schedule) {
        Employee employee = schedule.getEmployee();
        employee.setWorkingSchedules(null);
        schedule.setEmployee(employee);

        return schedule;
    }
}
