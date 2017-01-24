package has.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByNumber(Integer number);

    @Query("select r from Room r where r.status = :status and r.roomClass = :roomClass and r.bedsSingle = :bedsSingle and r.bedsDouble = :bedsDouble and r.children = :children and r.pets = :pets and r.minibar = :minibar")
    Page<Room> searchByFilters(
            @Param("status") Integer status,
            @Param("roomClass") Integer roomClass,
            @Param("bedsSingle") Integer bedsSingle,
            @Param("bedsDouble") Integer bedsDouble,
            @Param("children") Boolean children,
            @Param("pets") Boolean pets,
            @Param("minibar") Boolean minibar,
            Pageable pageRequest);
}
