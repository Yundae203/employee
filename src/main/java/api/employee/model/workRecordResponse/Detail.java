package api.employee.model.workRecordResponse;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Detail {

    private LocalDate date;
    private Long workingMinutes;
    private boolean usingDayOff;

    public Detail(LocalDate date, Long workingMinutes, boolean usingDayOff) {
        this.date = date;
        this.workingMinutes = workingMinutes;
        this.usingDayOff = usingDayOff;
    }
}
