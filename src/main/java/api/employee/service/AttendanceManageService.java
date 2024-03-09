package api.employee.service;

import api.employee.domain.AttendanceStatus;
import api.employee.domain.Member;
import api.employee.domain.Team;
import api.employee.domain.WorkTime;
import api.employee.integration.externalApi.RestDayCalculator;
import api.employee.model.MemberWorkTime;
import api.employee.model.workRecordResponse.Detail;
import api.employee.model.workRecordResponse.WorkRecordResponse;
import api.employee.repository.AttendanceStatusRepository;
import api.employee.service.domain.MemberService;
import api.employee.service.domain.AttendanceStatusService;
import api.employee.visitor.AttendanceVisitor;
import api.employee.visitor.Visitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 직원의 근무 상태와 관련된 정보를 관리합니다.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceManageService {

    private final MemberService memberService;
    private final AttendanceStatusService attendanceStatusService;
    private final RestDayCalculator restDayCalculator;

    private final AttendanceStatusRepository attendanceStatusRepository;

    /**
     * 직원의 출근 시간을 기록합니다.
     * @param memberId 대상 직원 ID
     * @param attendanceDate 기록일
     * @param startTime 출근 시간
     */
    @Transactional
    public void recordMemberArrivalTime(Long memberId, LocalDate attendanceDate, LocalTime startTime) {
        Member member = memberService.getMemberReference(memberId);
        attendanceStatusService.recordArrivalTime(member, attendanceDate, startTime);
    }

    /**
     * 요청한 달의 특정 직원의 모든 근무 상태를 찾습니다.
     * @param memberId 대상 직원 ID
     * @param month 요청 달
     * @return 요청 달의 해당 직원의 근무 상태 기록
     */
    public WorkRecordResponse memberMonthAttendanceRecord(Long memberId, int year, int month) {
        List<AttendanceStatus> attendanceStatusList = attendanceStatusService.findAllAttendanceStatus(memberId, year, month);

        // Visitor 패턴
        List<Detail> detail = new ArrayList<>();
        Visitor visitor = new AttendanceVisitor();
        // attendanceStatusList 순회하며 Element 별로 로직 처리
        for (AttendanceStatus attendanceStatus : attendanceStatusList) {
            detail.add(attendanceStatus.accept(visitor));
        }
        return new WorkRecordResponse(detail);
    }

    /**
     * 신청일과 팀의 연차 정책을 비교하여 연차 사용 가능 여부를 판단합니다.
     * 사용 가능하다면, 자신의 남은 연차를 감소시키고 사용합니다.
     * @param memberId 대상 직원 ID
     * @param startDay 연차 사용일
     * @param endDay 연차 종료일
     * @param reason 연사 사용 이유
     */
    @Transactional
    public void useLeave(Long memberId, LocalDate startDay, LocalDate endDay, String reason) {
        Member member = memberService.findOneMemberWithTeam(memberId);
        Team team = member.getTeam();

        // 연차 신청일과 오늘과의 차이 계산
        int remainingDay = (int) ChronoUnit.DAYS.between(LocalDate.now(), startDay);

        // 팀의 연차 정책과 신청일 비교
        if (remainingDay < team.getLeavePolicy()) {
            throw new IllegalArgumentException("The leave request was submitted too late according to the team's leave policy.");
        }

        // 남은 연차가 충분한 지 확인
        Long leavePeriod = ChronoUnit.DAYS.between(startDay, endDay) + 1;
        if (member.notEnoughLeave(leavePeriod)) {
            throw new IllegalArgumentException("Your leave is not enough.");
        }
        
        for (int i = 0; i<leavePeriod; i++) {
            member.usingLeave(); // 연차 사용
            attendanceStatusService.recordLeave(member, startDay, reason); // 연차 기록
            startDay = startDay.plusDays(1);
        }
    }

    /**
     * 요청 달의 초과근무 기준을 확인한 뒤, 
     * 모든 직원의 근무 시간과 비교하여 각 직원별 초과근무 시간을 반환합니다.
     * @param year 요청 년도
     * @param month 요청 달
     * @return 모든 직원의 초과 근무 시간(분)
     */
    public List<MemberWorkTime> getMembersOverTime(Integer year, Integer month) {
        // 요청 달의 초과근무 기준 파악
        Long overTimeStandard = restDayCalculator.getOverTimeStandard(year, month);
        WorkTime overTime = WorkTime.minute(overTimeStandard);
        log.info("overTimeMinuet = {}", overTimeStandard);

        // Stream으로 순회하며 초과근무 기준과 직원의 총 근무 시간을 비교한 뒤, 초과근무 시간을 반환
        return attendanceStatusRepository.findAllMemberWorkTimeByYearMonth(year, month).stream()
                .map(memberWorkTime -> memberWorkTime.calculateOverTime(overTime))
                .toList();
    }
}
