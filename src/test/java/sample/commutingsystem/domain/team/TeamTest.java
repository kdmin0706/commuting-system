package sample.commutingsystem.domain.team;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamTest {

  @Test
  @DisplayName("팀에 매니저가 있는 경우 true를 반환한다.")
  void hasManager() {
    // given
    Team team = Team.builder()
        .name("team")
        .manager("manager")
        .build();

    // when
    boolean result = team.hasManager();

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("팀에 매니저가 없는 경우 false를 반환한다.")
  void hasManagerWhenIsNotExist() {
    // given
    Team team = Team.builder()
        .name("team")
        .build();

    // when
    boolean result = team.hasManager();

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("팀에 속한 멤버가 증가하면 카운트를 증가시킨다.")
  void incrementMemberCount() {
    // given
    Team team = Team.builder()
        .name("team")
        .build();

    // when
    team.incrementMemberCount();

    // then
    assertThat(team.getMemberCount()).isEqualTo(1);
  }

}