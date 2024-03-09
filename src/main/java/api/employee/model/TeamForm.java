package api.employee.model;

import api.employee.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TeamForm {
    private String name;
    private Integer leavePolicy;

    public TeamForm(String name, Integer leavePolicy) {
        this.name = name;
        this.leavePolicy = leavePolicy;
    }

    public Team convertToTeam() {
        String name = this.name;
        if (this.name.isBlank()) {
            name = null;
        }
        return Team.builder()
                .name(name)
                .leavePolicy(this.getLeavePolicy())
                .build();
    }
}
