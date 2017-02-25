package has.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Repository
public interface TaskRepository extends JpaRepository<Task ,Long>{
}
