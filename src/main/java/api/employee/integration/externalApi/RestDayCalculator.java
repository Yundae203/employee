package api.employee.integration.externalApi;

import api.employee.integration.model.restDayResponse.RestDayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RestDayCalculator {

    private WebClient webClient = WebClient.create("http://apis.data.go.kr");

    @Value("${restDay.serviceKey}")
    private String decodeServiceKey;

    /**
     * 요청 연도와 월을 받아서 초과 근무 계산을 위해 필요한 이번 달의 총 일 수, 주말 수, 주말이 아닌 휴일 수를 구하는 메서드에 전달.
     * 반환 받은 값으로 초과 근무 기준을 계산하여 반환한다.
     * @param year 요청 연도
     * @param month 요청 달
     * @return
     */
    public Long getOverTimeStandard(Integer year, Integer month) {
        int monthDays = countMonthDays(year, month); // 요청 달의 총 일 수
        int weekendsDays = countWeekend(year, month); // 요청 달의 주말 수
        int restDaysCount = countRestDays(year, month); // 요청 달의 주말이 아닌 휴일 수
        log.info("mothDay = {}, weekendsDays = {}, restDaysCount = {}", monthDays, weekendsDays, restDaysCount);
        return (monthDays - weekendsDays - restDaysCount) * 480L;
    }

    /**
     * 요청 달의 공휴일을 계산한 뒤, countNotWeekendRestDays()를 호출하여 주말인 휴일을 제외한 뒤 반환한다.
     * @param year 요청 연도
     * @param month 요청 달
     * @return 주말이 아닌 휴일 수
     */
    private int countRestDays(Integer year, Integer month) {
        String yearParam = year.toString();
        String monthParam;

        if (month < 10) {
            monthParam = "0" + month;
        } else {
            monthParam = month.toString();
        }

        // 이중 인코딩을 막기 위하여 디코딩 키를 직접 인코딩
        String encodedServiceKey = null;
        try {
            encodedServiceKey = URLEncoder.encode(decodeServiceKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // WebClient로 동기 통신
        String response = webClient.get()
                                .uri(UriComponentsBuilder.fromUriString("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                                        .queryParam("solYear", yearParam)
                                        .queryParam("solMonth", monthParam)
                                        .queryParam("serviceKey", encodedServiceKey)
                                        .queryParam("_type", "json")
                                        .queryParam("numOfRows", 20)
                                        .build(true)
                                        .toUri())
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();
        // 역직렬화
        ObjectMapper mapper = new ObjectMapper();
        int count = 0;
        try {
            RestDayResponse result = mapper.readValue(response, RestDayResponse.class);
            List<LocalDate> restDays = result.getRestDay();
            count = countNotWeekendRestDays(restDays);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    /**
     * 공휴일을 받아서, 주말이 아닌 휴일만을 세서 그 결과를 반환한다.
     * @param restDays 모든 공휴일
     * @return 주말이 아닌 공휴일
     */
    private int countNotWeekendRestDays(List<LocalDate> restDays) {
        int count = 0;
        // 중복 방지를 위하여, 공휴일이 주말이 아닌 경우만 카운트
        for (LocalDate restDay : restDays) {
            if (!(restDay.getDayOfWeek() == DayOfWeek.SATURDAY) && !(restDay.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                count ++;
            }
        }
        return count;
    }

    /**
     * 요청 달의 모든 주말 수를 반환한다.
     * @param year 요청 연도
     * @param month 요청 달
     * @return 요청 달의 주말 수
     */
    private int countWeekend(int year, int month) {
        List<LocalDate> weekendDays = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        LocalDate current = start;
        // 현재 날짜가 마지막 날을 초과하지 않는 동안 반복
        while (!current.isAfter(end)) {
            // 현재 날짜가 토요일이나 일요일인 경우 주말 리스트에 추가
            if ((current.getDayOfWeek() == DayOfWeek.SATURDAY) || (current.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                weekendDays.add(current);
            }
            current = current.plusDays(1); // 다음 날짜로 이동
        }
        return weekendDays.size();
    }

    /**
     * 요청 달의 총 일 수를 계산하여 반환한다.
     * @param year 요청 연도
     * @param month 요청 달
     * @return 요청 달의 총 일 수
     */
    private int countMonthDays(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
