package api.employee.integration.model.restDayResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
    private Items items;

    public List<LocalDate> callItems() {
        return items.getRestDays();
    }
}
