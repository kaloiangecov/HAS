package has.Guest;

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
public class GuestServiceTest {

    @InjectMocks
    GuestService service;
    @Mock
    GuestRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Guest> guests = new LinkedList<>();
        Guest g1 = new Guest();
        Guest g2 = new Guest();
        Guest g3 = new Guest();

        guests.add(g1);
        guests.add(g2);
        guests.add(g3);
        Mockito.when(repo.findAll()).thenReturn(guests);
        assertEquals(service.getAllGuests(), guests);
        assertNotNull(service.getAllGuests());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Guest g1 = new Guest();
        Mockito.when(repo.findOne(23456L)).thenReturn(g1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Guest g1 = new Guest();
        Guest g2 = new Guest();
        Mockito.when(repo.findOne(23456L)).thenReturn(g1);
        Mockito.when(repo.save(g1)).thenReturn(g2);
        assertEquals(service.update(23456L, g2), g2);
    }
}
