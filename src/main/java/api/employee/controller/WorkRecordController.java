package api.employee.controller;

import api.employee.model.MemberWorkTime;
import api.employee.model.workRecordResponse.WorkRecordResponse;
import api.employee.service.AttendanceManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WorkRecordController {

    private final AttendanceManageService attendanceManageService;

    @GetMapping("/member/{memberId}/attendance-records")
    public WorkRecordResponse findMemberAttendanceRecord(@PathVariable(value = "memberId") Long memberId,
                                                         @RequestParam(name = "year") Integer year,
                                                         @RequestParam(name = "month") int month) {
        return attendanceManageService.memberMonthAttendanceRecord(memberId, year, month);
    }

    @GetMapping("/members/over-time")
    public List<MemberWorkTime> findMembersOverTime(@RequestParam(name = "year") Integer year,
                                                    @RequestParam(name = "month") Integer month) {
        return attendanceManageService.getMembersOverTime(year, month);
    }

}
