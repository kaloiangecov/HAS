package has.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public Room save(Room room) throws Exception {
        validateRoomNumber(room);
        return repo.save(room);
    }

    public List<Room> getAllRooms() {
        return repo.findAll();
    }

    public List<Room> getActiveRooms() {
        return repo.findByStatusLessThan(3);
    }

    public Page<Room> searchRooms(int start, int length, String sortColumn, String sortDirection, int number, Integer roomClass) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        if (roomClass != null && roomClass != -1) {
            if (number > 0)
                return repo.findByNumberAndRoomClass(number, roomClass, request);
            else
                return repo.findByRoomClass(roomClass, request);
        } else {
            if (number > 0)
                return repo.findByNumber(number, request);
            else
                return repo.findAll(request);
        }
    }

    public Room findById(Long id) throws Exception {
        Room room = repo.findOne(id);
        validateIdNotNull(room);
        return room;
    }

    public Room remove(Long id) throws Exception {
        Room room = repo.findOne(id);
        validateIdNotNull(room);
        repo.delete(room);
        return room;
    }

    public Room update(Long id, Room room) throws Exception {
        Room dbRoom = repo.findOne(id);
        validateIdNotNull(dbRoom);

        Room dbRoom2 = repo.findByNumber(room.getNumber());
        if (dbRoom2 != null && dbRoom2.getId() != id) {
            throw new Exception("Room with number " + room.getNumber() + " already exists!");
        }

        dbRoom.setNumber(room.getNumber());
        dbRoom.setRoomClass(room.getRoomClass());
        dbRoom.setStatus(room.getStatus());
        dbRoom.setBedsDouble(room.getBedsDouble());
        dbRoom.setBedsSingle(room.getBedsSingle());
        dbRoom.setChildren(room.isChildren());
        dbRoom.setPets(room.isPets());
        dbRoom.setMinibar(room.isMinibar());
        return repo.save(dbRoom);
    }

    private void validateIdNotNull(Room room) throws Exception {
        if (room == null) {
            throw new Exception("There is no room with such ID");
        }
    }

    private void validateRoomNumber(Room room) throws Exception {
        Room dbRoom = repo.findByNumber(room.getNumber());
        if (dbRoom != null) {
            throw new Exception("Room with number " + room.getNumber() + " already exists!");
        }
    }

    public Room changeStatus(Long id, Integer status) throws Exception {
        Room room = repo.findOne(id);
        room.setStatus(status);
        return repo.save(room);
    }
}
