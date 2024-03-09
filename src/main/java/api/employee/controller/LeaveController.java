package api.employee.controller;


import api.employee.model.LeaveForm;
import api.employee.service.AttendanceManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LeaveController {

    private final AttendanceManageService attendanceManageService;

    @PostMapping("/member/{memberId}/leave")
    public void useLeave(@RequestBody LeaveForm leaveForm, @PathVariable(name = "memberId") Long memberId) {
        attendanceManageService.useLeave(memberId, leaveForm.getStartDay(), leaveForm.getEndDay(), leaveForm.getReason());
    }
}
