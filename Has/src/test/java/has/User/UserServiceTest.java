package has.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chokleet on 20.1.2017 г..
 */
public class UserServiceTest {

    @InjectMocks
    UserService service;
    @Mock
    UserRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<User> users = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        users.add(user1);
        users.add(user2);
        users.add(user3);
        Mockito.when(repo.findAll()).thenReturn(users);
        assertEquals(service.getAllUsers(), users);
        assertNotNull(service.getAllUsers());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        User user1 = new User();
        Mockito.when(repo.findOne(23456L)).thenReturn(user1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        User user1 = new User();
        User user2 = new User();
        Mockito.when(repo.findOne(23456L)).thenReturn(user1);
        Mockito.when(repo.save(user2)).thenReturn(user2);
        assertEquals(service.update(23456L, user2), user2);
    }
}
