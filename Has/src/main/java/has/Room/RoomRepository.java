package has.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByNumber(Integer number);
}
