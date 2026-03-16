package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.Session;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByMovieIdAndStatus(Long movieId, SessionStatus status);
    List<Session> findByRoomId(Long roomId);

    //verificar se existem sessões com horários iguais na mesma sala
    @Query("""
    SELECT COUNT(s) > 0 FROM Session s
    WHERE s.room.id = :roomId 
    AND s.status = 'SCHEDULED' 
    AND s.startTime < :endTime
    AND s.endTime > :startTime
""")
    boolean existsConflictingSession(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
