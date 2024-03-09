package api.employee.service;
import api.employee.domain.Member;
import api.employee.domain.Role;
import api.employee.domain.Team;
import api.employee.model.MemberForm;
import api.employee.model.MemberUpdateForm;
import api.employee.service.domain.MemberService;
import api.employee.service.domain.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 직원의 회사와 관련된 정보를 관리합니다.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationService {

    private final MemberService memberService;
    private final TeamService teamService;

    /**
     * 직원을 등록합니다.
     * @param memberForm 등록할 직원 정보
     */
    @Transactional
    public void joinMember(MemberForm memberForm) {
        Member member = memberForm.convertToMember();
        addOrganizationInfo(memberForm.getTeamId(), memberForm.getRole(), member);
        memberService.saveMember(member);
    }

    /**
     * 직원 정보를 수정합니다.
     * @param memberUpdateForm 직원 수정 정보
     */
    @Transactional
    public void updateMember(MemberUpdateForm memberUpdateForm) {
        Member member = memberService.findOneMember(memberUpdateForm.getMemberId());
        memberUpdateForm.updatePrivacy(member);
        addOrganizationInfo(memberUpdateForm.getTeamId(), memberUpdateForm.getRole(), member);
    }

    /**
     * 직원의 회사와 관련 정보를 처리합니다.
     * @param teamId 대상 팀 ID
     * @param role 직원 역할
     * @param member 대상 직원
     */
    private void addOrganizationInfo(Long teamId, Role role, Member member) {
        addTeamInfo(teamId, member);
        addRoleInfo(role, member);
    }

    /**
     * 직원의 팀과 관련된 정보를 처리합니다.
     * @param teamId 대상 팀 ID
     * @param member 대상 직원
     */
    private void addTeamInfo(Long teamId, Member member) {
        if (teamId == null) return;

        Team team = teamService.getReferenceTeam(teamId);
        // 팀이 없으면 등록
        if (!member.hasTeam()) {
            member.signTeam(team);
            return;
        }
        // 팀이 있으면 변경
        member.changeTeam(team);
    }

    /**
     * 직원의 역할과 관련된 정보를 처리합니다.
     * @param role 직원 역할
     * @param member 대상 직원
     */
    private void addRoleInfo(Role role, Member member) {
        member.changeRole(role);
        
        // 역할이 매니저면 추가 로직 수행
        if (member.isManager()) {
            changeTeamManager(member);
        }
    }

    /**
     * 직원의 변경 역할이 매니저인 경우 팀의 매니저를 변경합니다.
     * 대상 팀에 매니저가 존재할 경우, 해당 직원의 역할을 멤버로 수정합니다.
     * @param member 역할이 매니저인 직원
     */
    private void changeTeamManager(Member member) {
        Assert.isTrue(member.isManager(), "The role must be Manager.");
        Team team = member.getTeam();
        // 팀에 기존의 매니저가 있으면 기존 매니저 멤버로 변경
        if (team.hasManager()) {
            Member currentManager = memberService.findOneMember(team.getManager());
            team.resignManager(currentManager.getName());
            currentManager.changeRole(Role.MEMBER);
        }
        team.signManager(member.getName());
    }
}
