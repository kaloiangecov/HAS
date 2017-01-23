package has.Request;

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
public class RequestServiceTest {

    @InjectMocks
    RequestService service;
    @Mock
    RequestRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Request> requests = new LinkedList<>();
        Request req1 = new Request();
        Request req2 = new Request();
        Request req3 = new Request();

        requests.add(req1);
        requests.add(req2);
        requests.add(req3);
        Mockito.when(repo.findAll()).thenReturn(requests);
        assertEquals(service.getAllRequests(), requests);
        assertNotNull(service.getAllRequests());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Request req1 = new Request();
        Mockito.when(repo.findOne(23456L)).thenReturn(req1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Request req1 = new Request();
        Request req2 = new Request();
        Mockito.when(repo.findOne(23456L)).thenReturn(req1);
        Mockito.when(repo.save(req2)).thenReturn(req2);
        assertEquals(service.update(23456L, req2), req2);
    }
}
