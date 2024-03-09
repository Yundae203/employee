package api.employee.domain;

import api.employee.visitor.AttendanceElement;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "record_type")
public abstract class AttendanceStatus implements AttendanceElement {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AttendanceStatus_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate attendanceDate;

    public AttendanceStatus(Member member, LocalDate attendanceDate) {
        this.member = member;
        this.attendanceDate = attendanceDate;
    }
}
