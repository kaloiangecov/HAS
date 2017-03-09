package has.WorkingSchedule;

import has.Utils.Validator;
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

    public WorkingSchedule save(WorkingSchedule schedule) throws Exception {
        if (!Validator.isValidStartEndDate(schedule.getStartDate(), schedule.getEndDate())) {
            throw new Exception("Invalid date");
        }

        validateOneShiftPerDay(schedule);
        return repo.save(schedule);
    }

    public List<WorkingSchedule> getAllSchedules() {
        return repo.findAll();
    }

    public Page<WorkingSchedule> searchSchedule(int start, int length, String sortColumn, String sortDirection, String startDate, String endDate, Long roleID) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        Page<WorkingSchedule> schedulePage = repo.findByRangeAndRole(startDate, endDate, roleID, request);

        return schedulePage;
    }

    public WorkingSchedule findById(Long id) throws Exception {
        WorkingSchedule workingSchedule = repo.findOne(id);
        validateIdNotNull(workingSchedule);

        return workingSchedule;
    }

    public WorkingSchedule remove(Long id) throws Exception {
        WorkingSchedule workingSchedule = repo.findOne(id);
        validateIdNotNull(workingSchedule);

        repo.delete(workingSchedule);
        return workingSchedule;
    }

    public WorkingSchedule update(Long id, WorkingSchedule schedule) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        validateIdNotNull(dbSchedule);
        validateOneShiftPerDay(dbSchedule);

        dbSchedule.setStartDate(schedule.getStartDate());
        dbSchedule.setEndDate(schedule.getEndDate());
        dbSchedule.setShift(schedule.getShift());
        dbSchedule.setEmployee(schedule.getEmployee());

        return repo.save(dbSchedule);
    }

    private void validateIdNotNull(WorkingSchedule workingSchedule) throws Exception {
        if (workingSchedule == null) {
            throw new Exception("There is no schedule with such ID");
        }
    }

    private void validateOneShiftPerDay(WorkingSchedule workingSchedule) throws Exception {
        WorkingSchedule dbWorkingSchedule =
                repo.findByEmployeeIdAndStartDate(
                        workingSchedule.getEmployee().getId(),
                        workingSchedule.getStartDate());
        if (dbWorkingSchedule != null && dbWorkingSchedule != workingSchedule) {
            throw new Exception("This employee has already got shift on this day: " + workingSchedule.getStartDate());
        }

    }
}
