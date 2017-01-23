package has.Room;

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
public class RoomServiceTest {

    @InjectMocks
    RoomService service;
    @Mock
    RoomRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Room> rooms = new LinkedList<>();
        Room room001 = new Room();
        Room room002 = new Room();
        Room room003 = new Room();

        rooms.add(room001);
        rooms.add(room002);
        rooms.add(room003);
        Mockito.when(repo.findAll()).thenReturn(rooms);
        assertEquals(service.getAllRooms(), rooms);
        assertNotNull(service.getAllRooms());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Room room001 = new Room();
        Mockito.when(repo.findOne(23456L)).thenReturn(room001);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Room room001 = new Room();
       Room room002 = new Room();
        Mockito.when(repo.findOne(23456L)).thenReturn(room001);
        Mockito.when(repo.save(room001)).thenReturn(room002);
        assertEquals(service.update(23456L, room002), room002);
    }
}
