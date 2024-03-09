package api.employee.integration.model.restDayResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
    @JsonDeserialize(using = ItemDeserializer.class)
    private List<Item> item;

    public List<LocalDate> getRestDays() {
        List<LocalDate> restDays = new ArrayList<>();
        for (Item day : item) {
            restDays.add(day.convertToDate());
        }
        return restDays;
    }
}
