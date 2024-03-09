package api.employee.model.workRecordResponse;

import lombok.Getter;

import java.util.List;

@Getter
public class WorkRecordResponse {

    private List<Detail> detail;
    private Long sum;

    public WorkRecordResponse(List<Detail> detail) {
        this.detail = detail;
        this.sum = detail.stream()
                        .mapToLong(Detail::getWorkingMinutes)
                        .sum();
    }
}
