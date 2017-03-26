package has.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssigneeId(Long id);

    List<Task> findByAssigneeIdAndTimePlacedStartingWith(Long id, String date);

    List<Task> findByAssigneeIdAndStatusNotOrderByTimePlaced(Long id, Integer status);

    List<Task> findByPriorityAndTimePlacedStartingWith(int priority, String date);

    Page<Task> findByStatusLessThanAndAssigneePersonalDataFullNameContaining(int status, String assignee, Pageable request);
}
