package has.Task;

import has.Employee.Employee;
import has.Employee.EmployeeDTO;
import has.Employee.EmployeeService;
import has.Utils.TaskHandler;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Service
public class TaskService {

    private static final int UNRESOLVED = 3;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private EmployeeService employeeService;

    public Task save(Task task, String assigner) {
        task.setAssigner(assigner);
        task = validateTargetTime(task);
        return repo.save(task);
    }

    public Task save(Task task) {
        task = validateTargetTime(task);
        return repo.save(taskHandler.assignTask(task, taskHandler.findShift(new LocalTime())));
    }

    public List<Task> equalize() {
        return repo.save(taskHandler.equalizeTasks(taskHandler.findEmployeesOnShiftDTO(new LocalTime())));
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Task findById(Long id) throws Exception {
        Task Task = repo.findOne(id);
        validateIdNotNull(Task);
        return Task;
    }

    public Task remove(Long id) throws Exception {
        Task Task = repo.findOne(id);
        validateIdNotNull(Task);
        repo.delete(Task);
        return Task;
    }

    public Task update(Long id, Task task) throws Exception {
        Task dbTask = repo.findOne(id);
        validateIdNotNull(dbTask);
        dbTask.setTitle(task.getTitle());
        dbTask.setDescription(task.getDescription());
        dbTask.setPriority(task.getPriority());
        dbTask.setStartTime(task.getStartTime());
        dbTask.setFinishTime(task.getFinishTime());
        dbTask.setTimePlaced(task.getTimePlaced());
        dbTask.setStatus(task.getStatus());
        dbTask.setAssigner(task.getAssigner());
        dbTask.setAssignee(task.getAssignee());
        dbTask.setDuration(task.getDuration());
        return repo.save(dbTask);
    }

    public List<Task> getEmployeesTasks(Employee employee) {
        return repo.findByAssigneeId(employee.getId());
    }

    public List<Task> getEmployeesUnresolvedTasks(Employee employee) {
        return repo.findByAssigneeIdAndStatusNotOrderByTimePlaced(employee.getId(), UNRESOLVED);
    }

    public List<Task> getCurrentTasks(String time) {
        return repo.findByTargetTimeGreaterThan(time);
    }

    public Task changeStatus(Long id, Integer status) {
        Task task = repo.findOne(id);
        task.setStatus(status);
        if (status == 1) {
            task.setStartTime(new Date().toString());
        }
        return repo.save(task);
    }

    private void validateIdNotNull(Task task) throws Exception {
        if (task == null) {
            throw new Exception("There is no task with such ID");
        }
    }

    private Task validateTargetTime(Task task) {
        if (task.getTargetTime() != null) {
            task.setStartTime(task.getTargetTime());
        }
        return task;
    }

    public List<Task> organiseTasks(Long employeeId) throws Exception {
        EmployeeDTO employeeDTO = employeeService.transferEmployeeToDTO(employeeId);
        return repo.save(taskHandler.organiseTasks(employeeDTO));

    }
}
