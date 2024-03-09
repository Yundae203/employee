package api.employee.model;

import api.employee.domain.Team;
import lombok.Getter;

@Getter
public class TeamUpdateForm {
    private Long teamId;
    private String name;
    private Integer leavePolicy;

    public void update(Team team) {
        if (this.name != null) {
            team.changeName(name);
        }
        if (this.leavePolicy != null) {
            team.changeLeavePolicy(leavePolicy);
        }
    }
}
