package has.Task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kaloi on 3/13/2017.
 */
public class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTasks() {
        List<Task> tasks = new LinkedList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Mockito.when(repo.findAll()).thenReturn(tasks);
        assertEquals(taskService.getAllTasks(), tasks);
        assertNotNull(taskService.getAllTasks());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        taskService.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Task task = new Task();
        Mockito.when(repo.findOne(23456L)).thenReturn(task);
        assertNotNull(taskService.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        taskService.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Task task = new Task();
        Task task1 = new Task();
        Mockito.when(repo.findOne(23456L)).thenReturn(task);
        Mockito.when(repo.save(task1)).thenReturn(task1);
        assertEquals(taskService.update(23456L, task1), task1);
    }
}
