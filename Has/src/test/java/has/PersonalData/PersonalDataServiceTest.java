package has.PersonalData;

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
public class PersonalDataServiceTest {

    @InjectMocks
    PersonalDataService service;
    @Mock
    PersonalDataRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<PersonalData> data = new LinkedList<>();
        PersonalData data1 = new PersonalData();
        PersonalData data2 = new PersonalData();
        PersonalData data3 = new PersonalData();

        data.add(data1);
        data.add(data2);
        data.add(data3);
        Mockito.when(repo.findAll()).thenReturn(data);
        assertEquals(service.getAllPersonalData(), data);
        assertNotNull(service.getAllPersonalData());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        PersonalData data1 = new PersonalData();
        Mockito.when(repo.findOne(23456L)).thenReturn(data1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        PersonalData data1 = new PersonalData();
        PersonalData data2 = new PersonalData();
        Mockito.when(repo.findOne(23456L)).thenReturn(data1);
        Mockito.when(repo.save(data1)).thenReturn(data2);
        assertEquals(service.update(23456L, data2), data2);
    }
}
