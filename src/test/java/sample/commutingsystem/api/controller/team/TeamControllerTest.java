package sample.commutingsystem.api.controller.team;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.commutingsystem.api.controller.team.request.TeamCreateRequest;
import sample.commutingsystem.api.service.team.TeamService;
import sample.commutingsystem.api.service.team.response.TeamResponse;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TeamService teamService;


  @Test
  @DisplayName("팀을 생성합니다.")
  void createTeam() throws Exception {
    // given
    TeamCreateRequest request = TeamCreateRequest.builder()
        .name("teamName")
        .build();

    // when // then
    mockMvc.perform(
        post("/api/v1/team")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  @DisplayName("팀을 생성할 때 팀 이름은 필수입니다.")
  void createTeamWithOutName() throws Exception {
    // given
    TeamCreateRequest request = TeamCreateRequest.builder()
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/team")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @DisplayName("전체 팀을 조회합니다.")
  void getAllTeams() throws Exception {
    // given
    List<TeamResponse> responses = List.of();
    given(teamService.getAllTeams()).willReturn(responses);

    // when // then
    mockMvc.perform(
            get("/api/v1/team")
        )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

}