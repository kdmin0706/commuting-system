package sample.commutingsystem.api.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.commutingsystem.domain.member.MemberRole.MANAGER;
import static sample.commutingsystem.domain.member.MemberRole.MEMBER;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
import sample.commutingsystem.api.service.member.response.MemberResponse;
import sample.commutingsystem.domain.attendance.Attendance;
import sample.commutingsystem.domain.attendance.AttendanceRepository;
import sample.commutingsystem.domain.member.Member;
import sample.commutingsystem.domain.member.MemberRepository;
import sample.commutingsystem.domain.team.Team;
import sample.commutingsystem.domain.team.TeamRepository;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private AttendanceRepository attendanceRepository;

  @AfterEach
  void tearDown() {
    attendanceRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    teamRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("신규 멤버를 등록한다.")
  void createMember() {
    // given
    LocalDate startDate = LocalDate.now();
    String teamName = "Team1";

    teamRepository.save(createTeam(teamName, null));

    MemberCreateRequest request = MemberCreateRequest.builder()
        .name("멤버1")
        .teamName(teamName)
        .memberRole(MEMBER)
        .workStartDate(startDate)
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when
    memberService.createMember(request);

    // then
    Member savedMember = memberRepository.findAll().get(0); // 첫 번째 멤버를 가져옴

    assertThat(savedMember)
        .extracting("name", "memberRole", "workStartDate", "birthday")
        .contains("멤버1", MEMBER, startDate, LocalDate.of(2024, 3, 2));
  }

  @Test
  @DisplayName("신규 멤버를 등록할 때 팀이 없는 경우 예외 처리한다.")
  void createMemberWhenIsNotExistTeam() {
    // given
    LocalDate startDate = LocalDate.now();

    MemberCreateRequest request = MemberCreateRequest.builder()
        .name("멤버1")
        .memberRole(MEMBER)
        .workStartDate(startDate)
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when // then
    assertThatThrownBy(() -> memberService.createMember(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 팀은 존재하지 않습니다.");
  }

  @Test
  @DisplayName("신규 멤버를 등록할 때 한 명의 매니저만 있을 수 있습니다.")
  void createMemberWhenOnlyOneManager() {
    // given
    String teamName = "team1";
    Team team = createTeam(teamName, "manager"); // 이 메서드가 팀에 매니저를 올바르게 설정하는지 확인
    teamRepository.save(team);

    MemberCreateRequest request = MemberCreateRequest.builder()
        .teamName(teamName) // teamName 설정
        .name("멤버2") // 멤버 이름 중복 방지
        .memberRole(MANAGER)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when // then
    assertThatThrownBy(() -> memberService.createMember(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("팀에는 한 명의 매니저만 있을 수 있습니다.");
  }

  @Test
  @DisplayName("모든 멤버를 조회한다.")
  void getMembers() {
    // given
    Team team1 = createTeam("team1", null);
    Team team2 = createTeam("team2", null);
    teamRepository.saveAll(List.of(team1, team2));

    Member member1 = createMember("member1", team1);
    Member member2 = createMember("member2", team2);
    memberRepository.saveAll(List.of(member1, member2));

    // when
    List<MemberResponse> responses = memberService.getMembers();

    // then
    assertThat(responses).hasSize(2)
        .extracting("name", "memberRole", "teamName")
        .containsExactlyInAnyOrder(
            tuple("member1", MEMBER, "team1"),
            tuple("member2", MEMBER, "team2")
        );
  }

  @Test
  @DisplayName("등록된 직원은 출근할 수 있다.")
  void startWorking() {
    // given
    Member member = createMember("member1", null);
    memberRepository.save(member);

    // when
    memberService.startWorking(member.getId());

    // then
    List<Attendance> attendances = attendanceRepository.findAll();
    assertThat(attendances).hasSize(1);
  }

  @Test
  @DisplayName("등록되지 않은 직원은 출근할 수 없다.")
  void startWorkingWhenIsNotRegister() {
    // given
    long memberId = 1L;

    // when // then
    assertThatThrownBy(() -> memberService.startWorking(memberId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("존재하지 않는 멤버입니다.");
  }


  private Member createMember(String name, Team team) {
    return Member.builder()
        .name(name)
        .team(team)
        .memberRole(MEMBER)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024,5,21))
        .build();
  }

  private Team createTeam(String teamName, String manager) {
    return Team.builder()
        .name(teamName)
        .manager(manager)
        .memberCount(1)
        .build();
  }

}