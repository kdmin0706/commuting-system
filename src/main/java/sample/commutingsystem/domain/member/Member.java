package sample.commutingsystem.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import sample.commutingsystem.domain.team.Team;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  private String name;

  @Enumerated(EnumType.STRING)
  private MemberRole memberRole;

  private LocalDate workStartDate;

  private LocalDate birthday;

  @Builder
  public Member(Team team, String name, MemberRole memberRole, LocalDate workStartDate,
      LocalDate birthday) {
    this.team = team;
    this.name = name;
    this.memberRole = memberRole;
    this.workStartDate = workStartDate;
    this.birthday = birthday;
  }

  public void joinTeam(Team team) {
    this.team = team;
  }

  public boolean isManager() {
    return this.memberRole.equals(MemberRole.MANAGER);
  }

}
