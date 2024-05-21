package sample.commutingsystem.api.service.team;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.commutingsystem.api.controller.team.request.TeamCreateRequest;
import sample.commutingsystem.api.service.team.response.TeamResponse;
import sample.commutingsystem.domain.team.Team;
import sample.commutingsystem.domain.team.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;

  public void createTeam(TeamCreateRequest request) {
    if (teamRepository.existsByName(request.getName())) {
      throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
    }

    Team team = request.toEntity();
    teamRepository.save(team);
  }

  public List<TeamResponse> getAllTeams() {
    List<Team> teams = teamRepository.findAll();

    return teams.stream()
        .map(TeamResponse::of)
        .toList();
  }

}

