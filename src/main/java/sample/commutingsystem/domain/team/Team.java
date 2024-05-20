package sample.commutingsystem.domain.team;


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

  private String name;

  private String manager;

  private int memberCount;

  @Builder
  public Team(String name, String manager, int memberCount) {
    this.name = name;
    this.manager = manager;
    this.memberCount = memberCount;
  }

}
