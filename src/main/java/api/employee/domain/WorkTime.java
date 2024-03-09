package api.employee.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * 근무 시간을 분(Minute)단위로 표기하는 Value Type Class
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkTime {

    public static final WorkTime ZERO = WorkTime.minute(0L);
    private Long workTime;

    private WorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public static WorkTime minute(Long workTime) {
        return new WorkTime(workTime);
    }

    public static WorkTime workRecord(LocalTime startTime, LocalTime endTime) {
        Long workTime = ChronoUnit.MINUTES.between(startTime, endTime);
        return new WorkTime(workTime);
    }

    public Long getWorkingMinute() {
        return this.workTime;
    }

    public boolean greaterThan(WorkTime workTime) {
        return this.workTime > workTime.getWorkingMinute();
    }

}
