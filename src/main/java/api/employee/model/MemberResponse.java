package api.employee.model;

import api.employee.domain.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponse {
    private String name;
    private String teamName;
    private String role;
    private LocalDate birthday;
    private LocalDate workStartDate;

    private MemberResponse(String name, String teamName, String role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        this.teamName = teamName;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public static MemberResponse fromMember(Member member) {
        return new MemberResponse(
                member.getName(), member.getTeamName(), member.getRoleName(),
                member.getBirthday(), member.getWorkStartDate());
    }
}
