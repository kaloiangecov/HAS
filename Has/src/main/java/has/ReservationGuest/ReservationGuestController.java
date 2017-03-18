package has.ReservationGuest;

import freemarker.template.TemplateException;
import has.Utils.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 1/31/2017.
 */
@RestController
public class ReservationGuestController {

    @Autowired
    private ReservationGuestService reservationGuestService;

    @RequestMapping(value = "/reservation-guest", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_RESERVATION_GUEST')")
    public ReservationGuest save(@RequestBody ReservationGuest reservationGuest) throws IOException, TemplateException {
        return reservationGuestService.save(reservationGuest);
    }

    @RequestMapping(value = "/reservation-guest", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_RESERVATION_GUEST')")
    public List<ReservationGuest> getAllReservations() {
        return reservationGuestService.getAllReservationGuestConnections();
    }

    @RequestMapping(value = "/reservation-guest/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_RESERVATION_GUEST')")
    public ReservationGuest findReservationById(@PathVariable Long id) throws Exception {
        return reservationGuestService.findById(id);
    }

    @RequestMapping(value = "/reservation-guest/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_RESERVATION_GUEST')")
    public ReservationGuest removeReservationById(@PathVariable Long id) throws Exception {
        return reservationGuestService.remove(id);
    }

    @RequestMapping(value = "/reservation-guest/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_RESERVATION_GUEST')")
    public ReservationGuest updateReservationGuest(@PathVariable Long id,
                                                   @RequestBody @Valid ReservationGuest reservationGuest) throws Exception {
        return reservationGuestService.update(id, reservationGuest);
    }

    @RequestMapping(value = "/reservations-guest/guest/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_RESERVATION_GUEST')")
    public DataTableResult getGuestHistory(@PathVariable Long id, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<ReservationGuest> guests = reservationGuestService.getClientHistory(
                id,
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0]
        );

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                guests.getTotalElements(),
                guests.getTotalElements(),
                guests.getContent());
    }
}
