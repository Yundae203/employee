package api.employee.service.domain;

import api.employee.domain.AttendanceStatus;
import api.employee.domain.Member;
import api.employee.domain.attendanceRecordType.LeaveRecord;
import api.employee.domain.attendanceRecordType.WorkRecord;
import api.employee.repository.AttendanceStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceStatusService {

    private final AttendanceStatusRepository attendanceRepository;

    /**
     * 직원의 출근 시간을 기록합니다.
     * @param member 대상 직원
     * @param attendanceDate 기록일
     * @param startTime 출근 시간
     */
    public void recordArrivalTime(Member member, LocalDate attendanceDate, LocalTime startTime) {
        WorkRecord workRecord = WorkRecord.recordStartTime(member, attendanceDate, startTime);
        attendanceRepository.save(workRecord);
    }

    /**
     * 직원의 퇴근 시간과 총 근무 시간을 기록합니다.
     * @exception IllegalArgumentException 출근 기록이 없을 경우 예외 발생.
     * @param memberId 대상 직원
     * @param attendanceDate 기록일
     * @param endTime 퇴근 시간
     */
    @Transactional
    public void recordDepartureTime(Long memberId, LocalDate attendanceDate, LocalTime endTime) {
        // 출근 기록 확인
        WorkRecord workRecord = attendanceRepository.findUnrecordedData(memberId, attendanceDate)
                .orElseThrow(() -> new IllegalArgumentException("No ArrivalTime Record."));
        // 퇴근 시간 및 총 근무 시간 기록
        workRecord.recordEndTime(endTime);
    }

    /**
     * 요청한 달의 특정 직원의 모든 근무 상태를 DB에서 조회합니다.
     * @exception IllegalArgumentException 조회 기록이 없을 경우 예외 발생.
     * @param memberId 대상 직원
     * @param month 대상 달
     * @return 요청 달의 해당 직원의 근무 상태 기록
     */
    public List<AttendanceStatus> findAllAttendanceStatus(Long memberId, int year, int month) {
        return attendanceRepository.findAllRecordByYearMonth(memberId, year, month)
                .orElseThrow(()-> new IllegalArgumentException("No result."));
    }

    /**
     * 허가된 연차를 기록합니다.
     * @param member 대상 직원
     * @param attendanceDate 기록일
     * @param reason 연차 사용 이유
     */
    public void recordLeave(Member member, LocalDate attendanceDate, String reason) {
        LeaveRecord record = LeaveRecord.reason(member, attendanceDate, reason);
        attendanceRepository.save(record);
    }
}
