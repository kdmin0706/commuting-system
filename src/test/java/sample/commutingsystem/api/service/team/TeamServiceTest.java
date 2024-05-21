package sample.commutingsystem.api.service.team;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.commutingsystem.api.controller.team.request.TeamCreateRequest;
import sample.commutingsystem.api.service.team.response.TeamResponse;
import sample.commutingsystem.domain.team.Team;
import sample.commutingsystem.domain.team.TeamRepository;

@SpringBootTest
@ActiveProfiles("test")
class TeamServiceTest {

  @Autowired
  private TeamService teamService;

  @Autowired
  private TeamRepository teamRepository;

  @AfterEach
  void tearDown() {
    teamRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("팀을 생성한다.")
  void createTeam() {
    // given
    TeamCreateRequest request = TeamCreateRequest.builder()
        .name("teamName")
        .build();

    // when
    teamService.createTeam(request);

    // then
    List<Team> teams = teamRepository.findAll();
    assertThat(teams).hasSize(1)
        .extracting("name")
        .contains("teamName");
  }

  @Test
  @DisplayName("팀을 생성할 때 동일한 이름이 있는 경우 예외 처리한다.")
  void createTeamWhenExistName() {
    // given
    Team team = createTeam("teamName");
    teamRepository.save(team);

    TeamCreateRequest request = TeamCreateRequest.builder()
        .name("teamName")
        .build();

    // when // then
    assertThatThrownBy(() -> teamService.createTeam(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 존재하는 팀 이름입니다.");
  }

  @Test
  @DisplayName("모든 팀을 조회한다.")
  void getAllTeams() {
    // given
    Team team1 = createTeam("team1");
    Team team2 = createTeam("team2");
    Team team3 = createTeam("team3");

    teamRepository.saveAll(List.of(team1, team2, team3));

    // when
    List<TeamResponse> responses = teamService.getAllTeams();

    // then
    assertThat(responses).hasSize(3)
        .extracting("name", "manager", "memberCount")
        .containsExactlyInAnyOrder(
            tuple("team1", null, 0),
            tuple("team2", null, 0),
            tuple("team3", null, 0)
        );
  }

  private Team createTeam(String name) {
    return Team.builder()
        .name(name)
        .build();
  }

}