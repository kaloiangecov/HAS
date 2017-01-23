package has.Room;

import has.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public Room save(@RequestBody @Valid Room room) {
        return roomService.save(room);
    }

    @RequestMapping(value = "/rooms", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @RequestMapping(value = "/searchrooms", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public @ResponseBody
    DataTableResult searchRooms(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Page<Room> rooms = roomService.searchRooms(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0])
        );

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                rooms.getTotalElements(),
                rooms.getTotalElements(),
                rooms.getContent());
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room findRoomByNumber(@PathVariable Long id) throws Exception {
        return roomService.findById(id);
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room removeRoomById(@PathVariable Long id) throws Exception {
        return roomService.remove(id);
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room updateRoom(@PathVariable Long id, @RequestBody @Valid Room room) throws Exception {
        return roomService.update(id, room);
    }
}
