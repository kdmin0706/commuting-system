package sample.commutingsystem.api.controller.member;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
import sample.commutingsystem.api.service.attendance.response.MemberAttendanceResponse;
import sample.commutingsystem.api.service.member.MemberService;
import sample.commutingsystem.api.service.member.response.MemberResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/api/v1/member")
  public void createMember(@Valid @RequestBody MemberCreateRequest request) {
    memberService.createMember(request);
  }

  @GetMapping("/api/v1/member")
  public List<MemberResponse> getMembers() {
    return memberService.getMembers();
  }

  @GetMapping("/api/v1/member/working/start")
  public void startWorking(@RequestParam Long memberId) {
    memberService.startWorking(memberId);
  }

  @GetMapping("/api/v1/member/working/end")
  public void endWorking(@RequestParam Long memberId) {
    memberService.endWorking(memberId);
  }

  @GetMapping("/api/v1/member/{memberId}/attendance/{yearMonth}")
  public MemberAttendanceResponse getMemberAttendance(
      @PathVariable Long memberId, @PathVariable YearMonth yearMonth) {
    return memberService.getMemberAttendance(memberId, yearMonth);
  }

  @GetMapping("/api/v1/member/AnnualLeaves")
  public void requestAnnualLeave(@RequestParam Long memberId, @RequestParam LocalDate date) {
    memberService.requestAnnualLeave(memberId, date);
  }

  @GetMapping("/api/v1/member/AnnualLeaves/count")
  public int getRemainingAnnualLeaves(@RequestParam Long memberId) {
    return memberService.getRemainingAnnualLeaves(memberId);
  }
}
