package api.employee.repository;

import api.employee.domain.AttendanceStatus;
import api.employee.domain.attendanceRecordType.WorkRecord;
import api.employee.model.MemberWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Long> {

    @Query("select a " +
            " from AttendanceStatus a " +
            "where a.member.id = :memberId " +
            "and a.attendanceDate = :attendanceDate " +
            "and a.endTime is null")
    Optional<WorkRecord> findUnrecordedData(@Param(value = "memberId") Long memberId,
                                            @Param(value = "attendanceDate") LocalDate attendanceDate);

    @Query("select a " +
            " from AttendanceStatus a " +
            "where a.member.id = :memberId " +
            "  and YEAR(a.attendanceDate) = :year " +
            "  and MONTH(a.attendanceDate) = :month " +
            "order by a.attendanceDate ASC")
    Optional<List<AttendanceStatus>> findAllRecordByYearMonth(@Param(value = "memberId") Long memberId,
                                                              @Param(value = "year") int year,
                                                              @Param(value = "month") int month);

    @Query("select new api.employee.model.MemberWorkTime(m.id, m.name, SUM(w.workTime.workTime))" +
            " from WorkRecord w " +
            " join w.member m " +
            "where YEAR(w.attendanceDate) = :year " +
            "  and MONTH(w.attendanceDate) = :month " +
            "group by m.id, m.name ")
    List<MemberWorkTime> findAllMemberWorkTimeByYearMonth(@Param(value = "year") int year,
                                                          @Param(value = "month") int month);
}
