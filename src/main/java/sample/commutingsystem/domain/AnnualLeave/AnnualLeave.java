package sample.commutingsystem.domain.AnnualLeave;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.commutingsystem.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnualLeave {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private LocalDate annualDate;

  @Builder
  public AnnualLeave(Member member, LocalDate annualDate) {
    this.member = member;
    this.annualDate = annualDate;
  }

  public static AnnualLeave create(Member member, LocalDate date) {
    return AnnualLeave.builder()
        .member(member)
        .annualDate(date)
        .build();
  }

}
