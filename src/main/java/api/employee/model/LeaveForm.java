package api.employee.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LeaveForm {

    private LocalDate startDay;
    private LocalDate endDay;
    private String reason;

}
