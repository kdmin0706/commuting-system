package sample.commutingsystem.api.service.attendance.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberAttendanceResponse {

  private List<AttendanceDetail> details;
  private int sum;

  @Builder
  public MemberAttendanceResponse(List<AttendanceDetail> details, int sum) {
    this.details = details;
    this.sum = sum;
  }

  public static MemberAttendanceResponse of(List<AttendanceDetail> details, int sum) {
    return MemberAttendanceResponse.builder()
        .details(details)
        .sum(sum)
        .build();
  }

}
