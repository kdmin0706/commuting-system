package sample.commutingsystem.api.controller.team;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.commutingsystem.api.controller.team.request.TeamCreateRequest;
import sample.commutingsystem.api.service.team.TeamService;
import sample.commutingsystem.api.service.team.response.TeamResponse;

@RestController
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/api/v1/team")
  public void createTeam(@Valid @RequestBody TeamCreateRequest request) {
    teamService.createTeam(request);
  }

  @GetMapping("/api/v1/team")
  public List<TeamResponse> getAllTeams() {
    return teamService.getAllTeams();
  }

}
