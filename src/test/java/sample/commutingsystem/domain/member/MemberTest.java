package sample.commutingsystem.domain.member;

import static org.assertj.core.api.Assertions.*;
import static sample.commutingsystem.domain.member.MemberRole.MANAGER;
import static sample.commutingsystem.domain.member.MemberRole.MEMBER;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  @DisplayName("현재 사용자가 매니저인 경우 true를 반환한다.")
  void isManager() {
    // given
    Member member = createMember(MANAGER);

    // when
    boolean result = member.isManager();

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("현재 사용자가 멤버인 경우 false를 반환한다.")
  void isManagerNotTrue() {
    // given
    Member member = createMember(MEMBER);

    // when
    boolean result = member.isManager();

    // then
    assertThat(result).isFalse();
  }

  private Member createMember(MemberRole memberRole) {
    return Member.builder()
        .name("member")
        .memberRole(memberRole)
        .workStartDate(LocalDate.now())
        .birthday(LocalDate.of(2024,5,21))
        .build();
  }

}