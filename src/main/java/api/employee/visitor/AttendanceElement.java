package api.employee.visitor;

import api.employee.model.workRecordResponse.Detail;

public interface AttendanceElement {

    Detail accept(Visitor visitor);

}
