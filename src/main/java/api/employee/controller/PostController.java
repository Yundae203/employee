package api.employee.controller;

import api.employee.model.MemberForm;
import api.employee.model.TeamForm;
import api.employee.service.AttendanceManageService;
import api.employee.service.OrganizationService;
import api.employee.integration.externalApi.RestDayCalculator;
import api.employee.service.domain.TeamService;
import api.employee.service.domain.AttendanceStatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final TeamService teamService;
    private final OrganizationService organizationService;
    private final AttendanceManageService attendanceManageService;
    private final AttendanceStatusService attendanceStatusService;

    @PostConstruct
    public void setUp() {
        TeamForm teamForm = new TeamForm("개발팀", 7);
        teamService.saveTeam(teamForm);

        TeamForm teamForm2 = new TeamForm("홍보팀", 3);
        teamService.saveTeam(teamForm2);

        MemberForm memberForm =
                new MemberForm(1L, "홍길동", "MANAGER",
                        LocalDate.of(1998,1,16),
                        LocalDate.of(2024,2,28));
        organizationService.joinMember(memberForm);

        MemberForm memberForm2 =
                new MemberForm(1L, "강감찬", "MANAGER",
                        LocalDate.of(1998,1,16),
                        LocalDate.of(2023,2,28));
        organizationService.joinMember(memberForm2);

        MemberForm memberForm3 =
                new MemberForm(2L, "이순신", "MANAGER",
                        LocalDate.of(1998,1,16),
                        LocalDate.of(2023,2,28));
        organizationService.joinMember(memberForm3);

        attendanceManageService.recordMemberArrivalTime(1L, LocalDate.of(2024,3,1), LocalTime.of(8, 30, 0));
        attendanceStatusService.recordDepartureTime(1L, LocalDate.of(2024,3,1), LocalTime.of(18, 0, 0));

        for (int i=0; i<16; i++) {
            attendanceManageService.recordMemberArrivalTime(1L, LocalDate.of(2024, 3, 2), LocalTime.of(8, 20, 0));
            attendanceStatusService.recordDepartureTime(1L, LocalDate.of(2024, 3, 2), LocalTime.of(18, 0, 0));
        }

        attendanceManageService.recordMemberArrivalTime(2L, LocalDate.of(2024,3,2), LocalTime.of(8, 20, 0));
        attendanceStatusService.recordDepartureTime(2L, LocalDate.of(2024,3,2), LocalTime.of(18, 0, 0));

        attendanceManageService.recordMemberArrivalTime(3L, LocalDate.of(2024,3,2), LocalTime.of(8, 20, 0));
        attendanceStatusService.recordDepartureTime(3L, LocalDate.of(2024,3,2), LocalTime.of(18, 0, 0));

    }
}
