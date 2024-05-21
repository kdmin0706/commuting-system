package sample.commutingsystem.api.service.member;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
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

    Attendance attendance = Attendance.builder()
        .member(member)
        .startTime(LocalDateTime.now())
        .build();

    attendanceRepository.save(attendance);
  }

  @Transactional
  public void endWorking(Long memberId) {
    Attendance attendance = attendanceRepository.findFirstByMemberIdOrderByStartTimeDesc(memberId)
        .orElseThrow(() -> new IllegalArgumentException("출근한 기록이 없습니다."));

    attendance.updateEndTime(LocalDateTime.now());
  }

}
