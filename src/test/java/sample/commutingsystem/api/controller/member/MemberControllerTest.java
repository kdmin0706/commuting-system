package sample.commutingsystem.api.controller.member;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.commutingsystem.domain.member.MemberRole.MEMBER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
import sample.commutingsystem.api.service.attendance.response.AttendanceDetail;
import sample.commutingsystem.api.service.attendance.response.MemberAttendanceResponse;
import sample.commutingsystem.api.service.member.MemberService;
import sample.commutingsystem.api.service.member.response.MemberResponse;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MemberService memberService;

  @Test
  @DisplayName("신규 멤버를 등록한다.")
  void createMember() throws Exception {
    // given
    MemberCreateRequest request = MemberCreateRequest.builder()
        .name("멤버1")
        .memberRole(MEMBER)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when // then
    mockMvc.perform(
        post("/api/v1/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  @DisplayName("신규 멤버를 등록할 때 멤버의 이름은 필수이다.")
  void createMemberWithOutName() throws Exception {
    // given
    MemberCreateRequest request = MemberCreateRequest.builder()
        .memberRole(MEMBER)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/member")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;

  }

  @Test
  @DisplayName("신규 멤버를 등록할 때 멤버의 역할은 필수이다.")
  void createMemberWithOutRole() throws Exception {
    // given
    MemberCreateRequest request = MemberCreateRequest.builder()
        .name("member")
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024, 3, 2))
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/member")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @DisplayName("직원의 정보를 조회한다.")
  void getMembers() throws Exception {
    // given
    List<MemberResponse> responses = List.of();
    given(memberService.getMembers()).willReturn(responses);

    // when // then
    mockMvc.perform(
            get("/api/v1/member")
        )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  @DisplayName("등록된 직원은 출근할 수 있다.")
  void startWorking() throws Exception {
    // given
    long memberId = 1L;
    doNothing().when(memberService).startWorking(memberId);

    // when // then
    mockMvc.perform(
        get("/api/v1/member/working/start")
            .param("memberId", Long.toString(memberId))
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  @DisplayName("출근한 직원은 퇴근을 할 수 있다.")
  void endWorking() throws Exception {
    // given
    long memberId = 1L;
    doNothing().when(memberService).endWorking(memberId);

    // when // then
    mockMvc.perform(
            get("/api/v1/member/working/end")
                .param("memberId", Long.toString(memberId))
        )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  @DisplayName("등록된 직원의 일정 기간의 출근 시간을 조회한다.")
  void getMemberAttendance() throws Exception {
    // given
    long memberId = 1L;
    String yearMonth = "2024-05";

    MemberAttendanceResponse response = MemberAttendanceResponse.builder()
        .details(List.of(
                AttendanceDetail.create("2024-05-01", 10),
                AttendanceDetail.create("2024-05-02", 10),
                AttendanceDetail.create("2024-05-03", 10)
            )
        )
        .sum(30)
        .build();

    when(memberService.getMemberAttendance(memberId, YearMonth.parse(yearMonth)))
        .thenReturn(response);

    // when // then
    mockMvc.perform(
            get("/api/v1/member/" + memberId + "/attendance/" + yearMonth)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.details[0].date").value("2024-05-01"))
        .andExpect(jsonPath("$.details[0].workingMinutes").value(10))
        .andExpect(jsonPath("$.sum").value(30))
    ;
  }

}