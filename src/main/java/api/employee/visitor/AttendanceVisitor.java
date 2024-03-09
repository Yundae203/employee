package api.employee.visitor;

import api.employee.domain.attendanceRecordType.LeaveRecord;
import api.employee.domain.attendanceRecordType.WorkRecord;
import api.employee.model.workRecordResponse.Detail;

public class AttendanceVisitor implements Visitor{
    
    /**
     * 근무일과 근무 시간 반환
     * @param workRecord 근무 기록
     * @return 근무 기록
     */
    @Override
    public Detail visitWorkRecord(WorkRecord workRecord) {
        return new Detail(
                workRecord.getAttendanceDate(),
                workRecord.getWorkingMinute(),
                false);
    }

    /**
     * 연차 사용일과 연차 사용 여부 반환
     * @param leaveRecord 연차 기록
     * @return 연차 기록
     */
    @Override
    public Detail visitLeaveRecord(LeaveRecord leaveRecord) {
        return new Detail(
                leaveRecord.getAttendanceDate(),
                0L,
                true);
    }
}
