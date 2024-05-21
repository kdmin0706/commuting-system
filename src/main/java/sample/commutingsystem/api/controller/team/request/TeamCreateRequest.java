package sample.commutingsystem.api.controller.team.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.commutingsystem.domain.team.Team;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateRequest {

  @NotBlank(message = "팀 이름은 필수입니다.")
  private String name;

  @Builder
  public TeamCreateRequest(String name) {
    this.name = name;
  }

  public Team toEntity() {
    return Team.builder()
        .name(name)
        .build();
  }

}
