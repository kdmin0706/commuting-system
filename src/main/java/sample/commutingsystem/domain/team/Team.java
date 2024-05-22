package sample.commutingsystem.domain.team;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String manager;

  private int memberCount;

  private int AnnualLeaveRegisterPeriod;

  @Builder
  public Team(String name, String manager, int memberCount, int annualLeaveRegisterPeriod) {
    this.name = name;
    this.manager = manager;
    this.memberCount = memberCount;
    this.AnnualLeaveRegisterPeriod = annualLeaveRegisterPeriod;
  }

  public boolean hasManager() {
    return this.getManager() != null;
  }

  public void incrementMemberCount() {
    memberCount++;
  }

  public void updateManager(String manager) {
    this.manager = manager;
  }

}
