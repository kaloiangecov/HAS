package has.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public Room save(Room room){
        return repo.save(room);
    }

    public List<Room> getAllRooms(){
        return repo.findAll();
    }

    public Room findByNumber(Integer number)throws Exception{
        Room dbRoom = repo.findByNumber(number);
        if(dbRoom==null){
            throw new Exception("There is no room with such ID");
        }
        return dbRoom;
    }

    public Room findById(Long id)throws Exception{
        Room dbRoom = repo.findOne(id);
        if(dbRoom==null){
            throw new Exception("There is no room with such ID");
        }
        return dbRoom;
    }

    public Room remove(Long id) throws Exception {
        Room dbRoom = repo.findOne(id);
        if(dbRoom == null){
            throw new Exception("There is no room with such ID");
        }
        repo.delete(dbRoom);
        return dbRoom;
    }

    public Room update(Long id, Room room) throws Exception {
        Room dbRoom = repo.findOne(id);
        if(dbRoom == null) {
            throw new Exception("There is no room with such ID");
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
}
