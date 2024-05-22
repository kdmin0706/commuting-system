package sample.commutingsystem.domain.attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.commutingsystem.domain.member.MemberRole.MEMBER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.commutingsystem.domain.member.Member;
import sample.commutingsystem.domain.member.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
class AttendanceRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private AttendanceRepository attendanceRepository;

  @Test
  @DisplayName("등록된 직원의 가장 최근에 출근한 날짜를 조회한다.")
  void findFirstByMemberIdOrderByStartTimeDesc() {
    // given
    Member member = createMember("member");
    memberRepository.save(member);

    Attendance attendance1 = createAttendance(member, LocalDateTime.of(2024, 1, 1, 9, 0));
    Attendance attendance2 = createAttendance(member, LocalDateTime.of(2024, 1, 2, 9, 0));
    Attendance attendance3 = createAttendance(member, LocalDateTime.of(2024, 1, 3, 9, 0));
    attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3));

    // when
    Optional<Attendance> attendance
        = attendanceRepository.findFirstByMemberIdOrderByStartTimeDesc(member.getId());

    // then
    assertThat(attendance).isPresent();
    assertThat(attendance.get().getStartTime()).isEqualTo(attendance3.getStartTime());
  }

  @Test
  @Transactional
  @DisplayName("등록된 직원의 일정 기간의 출근 시간을 조회한다.")
  void findAllByMemberIdAndStartTimeBetween() {
    // given
    Member member = createMember("member");
    memberRepository.save(member);

    Attendance attendance1 = createAttendance(member, LocalDateTime.of(2024, 4, 30, 9, 0));
    Attendance attendance2 = createAttendance(member, LocalDateTime.of(2024, 5, 1, 9, 0));
    Attendance attendance3 = createAttendance(member, LocalDateTime.of(2024, 5, 3, 9, 0));
    attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3));

    LocalDate start = LocalDate.of(2024, 5, 1);
    LocalDate end = LocalDate.of(2024, 5, 10);

    // when
    List<Attendance> attendances = attendanceRepository.findAllByMemberIdAndStartTimeBetween(
        member.getId(), start.atStartOfDay(), end.atStartOfDay());

    // then
    assertThat(attendances).hasSize(2)
        .extracting(attendance -> attendance.getMember().getName(), Attendance::getStartTime)
        .containsExactlyInAnyOrder(
            tuple(member.getName(), LocalDateTime.of(2024, 5, 1, 9, 0)),
            tuple(member.getName(), LocalDateTime.of(2024, 5, 3, 9, 0))
        );
  }


  private Attendance createAttendance(Member member, LocalDateTime startTime) {
    return Attendance.builder()
        .member(member)
        .startTime(startTime)
        .build();
  }

  private Member createMember(String name) {
    return Member.builder()
        .name(name)
        .memberRole(MEMBER)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024,5,21))
        .build();
  }

}