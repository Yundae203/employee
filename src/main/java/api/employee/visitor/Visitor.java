package api.employee.visitor;

import api.employee.domain.attendanceRecordType.LeaveRecord;
import api.employee.domain.attendanceRecordType.WorkRecord;
import api.employee.model.workRecordResponse.Detail;

public interface Visitor {

    Detail visitWorkRecord(WorkRecord workRecord);
    Detail visitLeaveRecord(LeaveRecord leaveRecord);

}
