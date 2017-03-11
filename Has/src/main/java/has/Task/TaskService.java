package has.Task;

import has.Employee.Employee;
import has.Task.Task;
import has.Task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Service
public class TaskService  {

    private static final int UNRESOLVED = 3;

    @Autowired
    private TaskRepository repo;

    public Task save(Task task, String assigner) {
        task.setAssigner(assigner);
        return repo.save(task);
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
        return repo.save(dbTask);
    }

    public List<Task> getEmployeesTasks(Employee employee) {
        return repo.findByAssigneeId(employee.getId());
    }
    public List<Task> getEmployeesUnresolvedTasks(Employee employee) {
        return repo.findByAssigneeIdAndStatusNot(employee.getId(),UNRESOLVED);
    }

    public Task changeStatus(Long id, Integer status){
        Task task = repo.findOne(id);
        task.setStatus(status);
        return repo.save(task);
    }

    private void validateIdNotNull(Task task) throws Exception {
        if (task == null) {
            throw new Exception("There is no task with such ID");
        }
    }
}
