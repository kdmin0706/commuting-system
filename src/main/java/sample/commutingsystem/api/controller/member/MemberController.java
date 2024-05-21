package sample.commutingsystem.api.controller.member;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sample.commutingsystem.api.controller.member.request.MemberCreateRequest;
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

}
