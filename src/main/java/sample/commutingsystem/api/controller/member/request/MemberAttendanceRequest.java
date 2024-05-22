package sample.commutingsystem.api.controller.member.request;

import java.time.YearMonth;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAttendanceRequest {

  private Long memberId;

  private YearMonth attendanceDate;

  @Builder
  public MemberAttendanceRequest(Long memberId, YearMonth attendanceDate) {
    this.memberId = memberId;
    this.attendanceDate = attendanceDate;
  }

}
