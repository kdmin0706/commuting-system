package sample.commutingsystem.api.service.attendance.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AttendanceDetail {
  private String date;
  private int workingMinutes;
  private boolean usingDayOff;

  @Builder
  public AttendanceDetail(String date, int workingMinutes, boolean usingDayOff) {
    this.date = date;
    this.workingMinutes = workingMinutes;
    this.usingDayOff = usingDayOff;
  }

  public static AttendanceDetail create(String date, int workingMinutes) {
    return AttendanceDetail.builder()
        .date(date)
        .workingMinutes(workingMinutes)
        .usingDayOff(workingMinutes == 0)
        .build();
  }

}
