package sample.commutingsystem.domain.AnnualLeave;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long> {

  List<AnnualLeave> findAllByMemberId(Long memberId);
}
