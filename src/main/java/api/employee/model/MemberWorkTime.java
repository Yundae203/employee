package api.employee.model;

import api.employee.domain.WorkTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberWorkTime {

    private Long id;
    private String name;
    private Long workTimeMinute;

    public MemberWorkTime(Long id, String name, Long workTimeMinute) {
        this.id = id;
        this.name = name;
        this.workTimeMinute = workTimeMinute;
    }

    public MemberWorkTime calculateOverTime(WorkTime overTime) {
        if (WorkTime.minute(workTimeMinute).greaterThan(overTime)) {
            workTimeMinute = workTimeMinute - overTime.getWorkingMinute();
        } else {
            workTimeMinute = 0L;
            System.out.println("workTimeMinute = " + workTimeMinute);
        }
        return this;
    }
}
