package api.employee.service.domain;

import api.employee.domain.Team;
import api.employee.model.TeamForm;
import api.employee.model.TeamResponse;
import api.employee.model.TeamUpdateForm;
import api.employee.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    /**
     * 팀 정보를 저장합니다.
     * @param teamForm 저장 팀 정보
     */
    @Transactional
    public void saveTeam(TeamForm teamForm) {
        teamRepository.save(teamForm.convertToTeam());
    }

    /**
     * API로 모든 팀 정보를 반환합니다.
     * @return 모든 팀 정보
     */
    public List<TeamResponse> findAllTeamResponse() {
        return teamRepository.findAll().stream()
                .map(TeamResponse::fromTeam)
                .toList();
    }

    /**
     * API로 요청된 팀 정보를 반환합니다.
     * @param teamId 대상 팀 ID
     * @return 요청 팀
     */
    public TeamResponse findOneTeamResponse(Long teamId) {
        return teamRepository.findById(teamId)
                .map(TeamResponse::fromTeam)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    /**
     * 특정 팀을 Proxy 객체로 조회합니다.
     * @param teamId 대상 팀 ID
     * @return 요청 팀 Proxy 객체
     */
    public Team getReferenceTeam(Long teamId) {
        return teamRepository.getReferenceById(teamId);
    }

    /**
     * 특정 팀을 조회합니다.
     * @param teamId 대상 팀 ID
     * @return 요청 팀
     */
    public Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    /**
     * 팀 정보를 변경합니다.
     * @param teamUpdateForm 팀 변경 정보
     */
    @Transactional
    public void updateTeam(TeamUpdateForm teamUpdateForm) {
        Team team = findTeamById(teamUpdateForm.getTeamId());
        teamUpdateForm.update(team);
    }

    /**
     * 팀을 삭제합니다.
     * @param teamId 대상 팀 ID
     */
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = findTeamById(teamId);
        teamRepository.delete(team);
    }
}
