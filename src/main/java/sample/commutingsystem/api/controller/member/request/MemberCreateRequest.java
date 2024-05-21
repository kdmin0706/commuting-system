package sample.commutingsystem.api.controller.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.commutingsystem.domain.member.Member;
import sample.commutingsystem.domain.member.MemberRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequest {

  @NotBlank(message = "멤버 이름은 필수입니다.")
  private String name;

  private String teamName;

  @NotNull(message = "멤버 역할은 필수입니다.")
  private MemberRole memberRole;

  private LocalDate workStartDate;

  private LocalDate birthday;

  @Builder
  public MemberCreateRequest(String name, String teamName, MemberRole memberRole,
      LocalDate workStartDate, LocalDate birthday) {
    this.name = name;
    this.teamName = teamName;
    this.memberRole = memberRole;
    this.workStartDate = workStartDate;
    this.birthday = birthday;
  }

  public Member toEntity() {
    return Member.builder()
        .name(name)
        .memberRole(memberRole)
        .workStartDate(workStartDate)
        .birthday(birthday)
        .build();
  }

}
