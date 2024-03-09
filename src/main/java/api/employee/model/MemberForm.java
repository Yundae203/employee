package api.employee.model;

import api.employee.domain.Member;
import api.employee.domain.Role;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Getter
public class MemberForm {
    private Long teamId;
    private String name;
    private String role;
    private LocalDate birthday;
    private LocalDate workStartDate;

    public MemberForm(Long teamId, String name, String role, LocalDate birthday, LocalDate workStartDate) {
        this.teamId = teamId;
        this.name = name;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public Role getRole() {
        return Role.valueOf(this.role.toUpperCase());
    }

    public Member convertToMember() {
        Assert.notNull(role, "Role can't be Null.");
        Assert.notNull(workStartDate, "WorkStartDate can't be Null.");
        return Member.builder()
                .name(name)
                .birthday(birthday)
                .workStartDate(workStartDate)
                .build();
    }
}
