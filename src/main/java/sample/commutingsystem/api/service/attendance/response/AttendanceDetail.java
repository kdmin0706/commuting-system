package sample.commutingsystem.api.service.attendance.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AttendanceDetail {
  private String date;
  private int workingMinutes;

  @Builder
  public AttendanceDetail(String date, int workingMinutes) {
    this.date = date;
    this.workingMinutes = workingMinutes;
  }

  public static AttendanceDetail create(String date, int workingMinutes) {
    return AttendanceDetail.builder()
        .date(date)
        .workingMinutes(workingMinutes)
        .build();
  }

}
