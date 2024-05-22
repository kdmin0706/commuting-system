package sample.commutingsystem.domain.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.commutingsystem.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  @Builder
  public Attendance(Member member, LocalDateTime startTime, LocalDateTime endTime) {
    this.member = member;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static Attendance create(Member member) {
    return Attendance.builder()
        .member(member)
        .startTime(LocalDateTime.now())
        .build();
  }

  public void updateEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

}
