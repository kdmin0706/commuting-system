package sample.commutingsystem.api.service.member.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import sample.commutingsystem.domain.member.Member;
import sample.commutingsystem.domain.member.MemberRole;

@Getter
public class MemberResponse {

  private String name;
  private String teamName;
  private MemberRole memberRole;
  private LocalDate workStartDate;
  private LocalDate birthday;

  @Builder
  public MemberResponse(String name, String teamName, MemberRole memberRole,
      LocalDate workStartDate,
      LocalDate birthday) {
    this.name = name;
    this.teamName = teamName;
    this.memberRole = memberRole;
    this.workStartDate = workStartDate;
    this.birthday = birthday;
  }

  public static MemberResponse of(Member member) {
    return MemberResponse.builder()
        .name(member.getName())
        .teamName(member.getTeam().getName())
        .memberRole(member.getMemberRole())
        .birthday(member.getBirthday())
        .workStartDate(member.getWorkStartDate())
        .build();
  }

}
