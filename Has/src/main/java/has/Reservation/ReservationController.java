package has.Reservation;

import has.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = "/reservation", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_RESERVATION')")
    public Reservation save(@RequestBody Reservation reservation, @AuthenticationPrincipal @Valid User user) {
        return reservationService.save(reservation, user);
    }

    @RequestMapping(value = "/reservations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_RESERVATION')")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_RESERVATION')")
    public Reservation findReservationById(@PathVariable Long id) throws Exception {
        return reservationService.findById(id);
    }

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_RESERVATION')")
    public Reservation removeReservationById(@PathVariable Long id) throws Exception {
        return reservationService.remove(id);
    }

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_RESERVATION')")
    public Reservation updateReservation(@PathVariable Long id, @RequestBody @Valid Reservation reservation, @AuthenticationPrincipal User user) throws Exception {
        return reservationService.update(id, reservation, user);
    }
}
