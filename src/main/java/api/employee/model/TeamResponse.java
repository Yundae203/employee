package api.employee.model;

import api.employee.domain.Team;
import lombok.Getter;

@Getter
public class TeamResponse {
    private String name;
    private String manager;
    private Integer memberCount;

    private TeamResponse(String name, String manager, Integer memberCount) {
        this.name = name;
        this.manager = manager;
        this.memberCount = memberCount;
    }
    public static TeamResponse fromTeam(Team team) {
        return new TeamResponse(team.getName(), team.getManager(), team.getMemberCount());
    }
}
