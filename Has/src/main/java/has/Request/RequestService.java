package has.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class RequestService {

    @Autowired
    private RequestRepository repo;

    public Request save(Request request) {
        return repo.save(request);
    }

    public List<Request> getAllRequests() {
        return repo.findAll();
    }

    public Request findById(Long id) throws Exception {
        Request dbRequest = repo.findOne(id);
        if(dbRequest == null){
            throw new Exception("There is no request with such ID");
        }
        return dbRequest;
    }

    public Request remove(Long id) throws Exception {
        Request dbRequest = repo.findOne(id);
        if(dbRequest == null){
            throw new Exception("There is no request with such ID");
        }
        repo.delete(dbRequest);
        return dbRequest;
    }

    public Request update(Long id, Request request) throws Exception {
        Request dbRequest = repo.findOne(id);
        if(dbRequest == null){
            throw new Exception("There is no request with such ID");
        }
        dbRequest.setStatus(request.getStatus());
        dbRequest.setTimeFinished(request.getTimeFinished());
        dbRequest.setTimePlaced(request.getTimePlaced());
        dbRequest.setType(request.getType());

        return repo.save(dbRequest);
    }
}
