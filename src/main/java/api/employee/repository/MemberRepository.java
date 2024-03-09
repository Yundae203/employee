package api.employee.repository;

import api.employee.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    @Query(value =
            "select 1 " +
            "  from member m " +
            "  join team t on m.team_id = t.team_id " +
            " where t.team_id = :teamId " +
            "   and t.leave_policy < :remainingDay ", nativeQuery = true)
    Optional<Integer> canUseLeave(@Param(value = "teamId") Long teamId,
                        @Param(value = "remainingDay") int remainingDay);

    @Query("select m " +
            " from Member m " +
            " join fetch m.team " +
            " where m.id = :memberId")
    Optional<Member> findMemberWithTeamById(@Param(value = "memberId") Long memberId);
}
