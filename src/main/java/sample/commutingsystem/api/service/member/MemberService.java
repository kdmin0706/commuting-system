package sample.commutingsystem.api.service.member;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
import sample.commutingsystem.api.service.attendance.response.AttendanceDetail;
import sample.commutingsystem.api.service.attendance.response.MemberAttendanceResponse;
import sample.commutingsystem.api.service.member.response.MemberResponse;
import sample.commutingsystem.domain.attendance.Attendance;
import sample.commutingsystem.domain.attendance.AttendanceRepository;
import sample.commutingsystem.domain.member.Member;
import sample.commutingsystem.domain.member.MemberRepository;
import sample.commutingsystem.domain.team.Team;
import sample.commutingsystem.domain.team.TeamRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final TeamRepository teamRepository;
  private final AttendanceRepository attendanceRepository;

  @Transactional
  public void createMember(MemberCreateRequest request) {
    Team team = teamRepository.findByName(request.getTeamName())
        .orElseThrow(() -> new IllegalArgumentException("해당 팀은 존재하지 않습니다."));

    Member member = request.toEntity();

    if (member.isManager() && team.hasManager()) {
      throw new IllegalArgumentException("팀에는 한 명의 매니저만 있을 수 있습니다.");
    }

    member.joinTeam(team);
    team.incrementMemberCount();
    if (member.isManager()) {
      team.updateManager(member.getName());
    }

    memberRepository.save(member);
  }

  public List<MemberResponse> getMembers() {
    List<Member> members = memberRepository.findAll();

    return members.stream()
        .map(MemberResponse::of)
        .toList();
  }

  @Transactional
  public void startWorking(Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

    validationAttendance(memberId);

    Attendance attendance = Attendance.create(member);
    attendanceRepository.save(attendance);
  }

  private void validationAttendance(Long memberId) {
    Optional<Attendance> optionalAttendance
        = attendanceRepository.findFirstByMemberIdOrderByStartTimeDesc(memberId);
    if (optionalAttendance.isEmpty()) {
      return;
    }

    Attendance lastAttendance = optionalAttendance.get();

    if (lastAttendance.getStartTime().toLocalDate().equals(LocalDate.now())) {
      if (lastAttendance.getEndTime() != null &&
          lastAttendance.getEndTime().toLocalDate().equals(LocalDate.now())) {
        throw new IllegalArgumentException("하루에 여러 번 출근하고 퇴근하는 것을 허용하지 않습니다.");
      }
      throw new IllegalArgumentException("이미 출근한 기록이 있습니다.");
    }

  }

  @Transactional
  public void endWorking(Long memberId) {
    Attendance attendance = attendanceRepository.findFirstByMemberIdOrderByStartTimeDesc(memberId)
        .orElseThrow(() -> new IllegalArgumentException("출근한 기록이 없습니다."));

    attendance.updateEndTime(LocalDateTime.now());
  }

  public MemberAttendanceResponse getMemberAttendance(Long memberId, YearMonth yearMonth) {
    LocalDate startOfMonth = yearMonth.atDay(1);
    LocalDate endOfMonth = yearMonth.atEndOfMonth();

    Map<LocalDate, Integer> attendanceMap = createAttendanceMapBy(memberId, startOfMonth, endOfMonth);
    List<AttendanceDetail> details = getAttendanceDetailsBy(startOfMonth, endOfMonth, attendanceMap);

    return MemberAttendanceResponse.of(
        details,
        details.stream()
            .map(AttendanceDetail::getWorkingMinutes)
            .mapToInt(Integer::intValue)
            .sum()
    );

  }

  private static List<AttendanceDetail> getAttendanceDetailsBy(LocalDate startOfMonth,
      LocalDate endOfMonth, Map<LocalDate, Integer> attendanceMap) {
    List<AttendanceDetail> details = new ArrayList<>();
    for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
      int workingMinutes = attendanceMap.getOrDefault(date, 0);

      details.add(AttendanceDetail.create(date.toString(), workingMinutes));
    }
    return details;
  }

  private Map<LocalDate, Integer> createAttendanceMapBy(Long memberId, LocalDate startOfMonth,
      LocalDate endOfMonth) {
    List<Attendance> attendances = attendanceRepository.findAllByMemberIdAndStartTimeBetween(
        memberId, startOfMonth.atStartOfDay(), endOfMonth.atStartOfDay());

    Map<LocalDate, Integer> attendanceMap = new HashMap<>();

    for (Attendance attendance : attendances) {
      if (attendance.getEndTime() != null) {
        LocalDate date = attendance.getStartTime().toLocalDate();
        int workingMinutes = (int) Duration.between(
            attendance.getStartTime(), attendance.getEndTime()).toMinutes();

        attendanceMap.put(date, attendanceMap.getOrDefault(date, 0) + workingMinutes);
      }
    }
    return attendanceMap;
  }

}
