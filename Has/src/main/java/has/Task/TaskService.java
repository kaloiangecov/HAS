package has.Task;

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

    @Autowired
    private TaskRepository repo;

    public Task save(Task task) {
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
        return repo.save(dbTask);
    }

    private void validateIdNotNull(Task task) throws Exception {
        if (task == null) {
            throw new Exception("There is no task with such ID");
        }
    }
}
