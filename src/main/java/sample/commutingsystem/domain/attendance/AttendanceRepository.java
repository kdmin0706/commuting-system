package sample.commutingsystem.domain.attendance;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

  Optional<Attendance> findFirstByMemberIdOrderByStartTimeDesc(Long memberId);

}
