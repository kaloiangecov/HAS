package has.Reservation;

import has.User.User;
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
 * Created by Chokleet on 20.1.2017 г..
 */
public class ReservationServiceTest {

    @InjectMocks
    ReservationService service;
    @Mock
    ReservationRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Reservation> reservations = new LinkedList<>();
        Reservation res1 = new Reservation();
        Reservation res2 = new Reservation();
        Reservation res3 = new Reservation();

        reservations.add(res1);
        reservations.add(res2);
        reservations.add(res3);
        Mockito.when(repo.findAll()).thenReturn(reservations);
        assertEquals(service.getAllReservations(), reservations);
        assertNotNull(service.getAllReservations());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Reservation res1 = new Reservation();
        Mockito.when(repo.findOne(23456L)).thenReturn(res1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Reservation res1 = new Reservation();
        Reservation res2 = new Reservation();
        User user1 = new User();
        Mockito.when(repo.findOne(23456L)).thenReturn(res1);
        Mockito.when(repo.save(res2)).thenReturn(res2);
        assertEquals(service.update(23456L, res2, user1), res2);
    }
}
