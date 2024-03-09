package api.employee.integration.model.restDayResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class RestDayResponse {
    Response response;

    public List<LocalDate> getRestDay() {
        return response.callBody();
    }
}
