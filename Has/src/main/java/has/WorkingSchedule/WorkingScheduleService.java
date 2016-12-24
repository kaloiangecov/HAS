package has.WorkingSchedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Service
public class WorkingScheduleService {

    @Autowired
    private WorkingScheduleRepository repo;

    public WorkingSchedule save(WorkingSchedule schedule){

        return repo.save(schedule);
    }

    public List<WorkingSchedule> getAllSchedules(){

        return repo.findAll();
    }

    public WorkingSchedule findById(Long id)throws Exception{
        WorkingSchedule dbSchedule = repo.findOne(id);
        if(dbSchedule==null){
            throw new Exception("There is no schedule with such ID");
        }
        return dbSchedule;
    }

    public WorkingSchedule remove(Long id) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        if(dbSchedule == null){
            throw new Exception("There is no schedule with such ID");
        }
        repo.delete(dbSchedule);
        return dbSchedule;
    }

    public WorkingSchedule update(Long id, WorkingSchedule schedule) throws Exception {
        WorkingSchedule dbSchedule = repo.findOne(id);
        if(dbSchedule == null) {
            throw new Exception("There is no schedule with such ID");
        }
        dbSchedule.setStartDate(schedule.getStartDate());
        dbSchedule.setEndDate(schedule.getEndDate());
        dbSchedule.setShift(schedule.getShift());
        return repo.save(dbSchedule);
    }
}
