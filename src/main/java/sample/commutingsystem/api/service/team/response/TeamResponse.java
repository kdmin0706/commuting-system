package sample.commutingsystem.api.service.team.response;

import lombok.Builder;
import lombok.Getter;
import sample.commutingsystem.domain.team.Team;

@Getter
public class TeamResponse {

  private String name;

  private String manager;

  private int memberCount;

  @Builder
  public TeamResponse(String name, String manager, int memberCount) {
    this.name = name;
    this.manager = manager;
    this.memberCount = memberCount;
  }

  public static TeamResponse of(Team team) {
    return TeamResponse.builder()
        .name(team.getName())
        .manager(team.getManager())
        .memberCount(team.getMemberCount())
        .build();
  }

}
