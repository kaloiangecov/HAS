package has.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/room", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room save(@RequestBody Room room){
        return roomService.save(room);
    }

    @RequestMapping(value = "/rooms", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Room> getAllRooms(){
        return roomService.getAllRooms();
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room removeRoomById(@PathVariable Long id) throws Exception{
        return roomService.remove(id);
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room room) throws Exception {
        return roomService.update(id,room);
    }
}
