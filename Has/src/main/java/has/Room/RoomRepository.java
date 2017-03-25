package has.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByNumber(Integer number);

    Page<Room> findByNumber(Integer number, Pageable pageRequest);

    @Query("SELECT r FROM has.Room.Room r WHERE (r.bedsSingle + r.bedsDouble*2) >= :numberAdults AND r.pets = :pets AND r.minibar = :minibar AND r NOT IN (SELECT res.room FROM has.Reservation.Reservation res WHERE (res.startDate BETWEEN :startDate AND :endDate) OR (res.endDate BETWEEN :startDate AND :endDate))")
    List<Room> findInSite(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults, @Param("pets") boolean pets, @Param("minibar") boolean minibar);

    @Query("SELECT r FROM has.Room.Room r WHERE (r.bedsSingle + r.bedsDouble*2) >= :numberAdults AND r.pets = :pets AND r.minibar = :minibar AND r NOT IN (SELECT res.room FROM has.Reservation.Reservation res WHERE res.id <> :existingId AND ((res.startDate BETWEEN :startDate AND :endDate) OR (res.endDate BETWEEN :startDate AND :endDate)))")
    List<Room> findInSiteForEdit(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults, @Param("pets") boolean pets, @Param("minibar") boolean minibar, @Param("existingId") Long existingId);

    @Query("SELECT r FROM has.Room.Room r WHERE (r.bedsSingle + r.bedsDouble*2) >= :numberAdults AND r.children = :children AND r.pets = :pets AND r.minibar = :minibar AND r NOT IN (SELECT res.room FROM has.Reservation.Reservation res WHERE (res.startDate BETWEEN :startDate AND :endDate) OR (res.endDate BETWEEN :startDate AND :endDate))")
    List<Room> findInSiteWithChildren(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults, @Param("children") boolean children, @Param("pets") boolean pets, @Param("minibar") boolean minibar);

    @Query("SELECT r FROM has.Room.Room r WHERE (r.bedsSingle + r.bedsDouble*2) >= :numberAdults AND r.children = :children AND r.pets = :pets AND r.minibar = :minibar AND r NOT IN (SELECT res.room FROM has.Reservation.Reservation res WHERE res.id <> :existingId AND ((res.startDate BETWEEN :startDate AND :endDate) OR (res.endDate BETWEEN :startDate AND :endDate)))")
    List<Room> findInSiteWithChildrenForEdit(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults, @Param("children") boolean children, @Param("pets") boolean pets, @Param("minibar") boolean minibar, @Param("existingId") Long existingId);
}
