package sample.commutingsystem.domain.team;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class TeamRepositoryTest {

  @Autowired
  private TeamRepository teamRepository;

  @AfterEach
  void tearDown() {
    teamRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("해당 이름을 가진 팀을 찾는다.")
  void findByName() {
    // given
    String teamName = "Team1";
    teamRepository.save(createTeam(teamName));

    // when
    Optional<Team> optional = teamRepository.findByName(teamName);

    // then
    assertThat(optional).isPresent();
    optional.ifPresent(
        t -> assertThat(t.getName()).isEqualTo(teamName)
    );
  }

  @Test
  @DisplayName("해당 이름을 가진 팀을 찾지 못한 경우 값은 비어있다.")
  void findByNameWhenIsEmpty() {
    // given
    String teamName = "Team1";

    // when
    Optional<Team> optional = teamRepository.findByName(teamName);

    // then
    assertThat(optional).isEmpty();
  }

  @Test
  @DisplayName("해당 이름을 가진 팀의 이름이 있는 경우 true를 반환한다.")
  void existsByName() {
    // given
    String teamName = "Team1";
    teamRepository.save(createTeam(teamName));

    // when
    boolean exists = teamRepository.existsByName(teamName);

    // then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("해당 이름을 가진 팀의 이름이 있는 경우 false를 반환한다.")
  void existsByNameWhenIsNotExist() {
    // given
    String teamName = "Team1";

    // when
    boolean exists = teamRepository.existsByName(teamName);

    // then
    assertThat(exists).isFalse();
  }

  private Team createTeam(String name) {
    return Team.builder()
        .name(name)
        .manager(null)
        .memberCount(1)
        .build();
  }

}