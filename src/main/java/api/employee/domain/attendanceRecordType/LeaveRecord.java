package api.employee.domain.attendanceRecordType;

import api.employee.domain.AttendanceStatus;
import api.employee.domain.Member;
import api.employee.model.workRecordResponse.Detail;
import api.employee.visitor.Visitor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("leave")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaveRecord extends AttendanceStatus {

    private String reason;

    private LeaveRecord(Member member, LocalDate attendanceDate, String reason) {
        super(member, attendanceDate);
        this.reason = reason;
    }

    public static LeaveRecord reason(Member member, LocalDate attendanceDate, String reason) {
        return new LeaveRecord(member, attendanceDate, reason);
    }

    // ===== Visitor ==== //
    @Override
    public Detail accept(Visitor visitor) {
        return visitor.visitLeaveRecord(this);
    }
}
