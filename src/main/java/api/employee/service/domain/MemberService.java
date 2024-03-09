package api.employee.service.domain;

import api.employee.domain.Member;
import api.employee.model.MemberResponse;
import api.employee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 직원의 정보를 저장합니다.
     * @param member 대상 직원
     */
    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    /**
     * API로 요청된 모든 직원의 정보를 반환합니다.
     * @return 모든 직원 정보
     */
    public List<MemberResponse> findAllMemberResponse() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::fromMember)
                .toList();
    }

    /**
     * API로 요청된 특정 직원의 정보를 반환합니다.
     * @param memberId 대상 직원 ID
     * @return 요청 직원 정보
     */
    public MemberResponse findOneMemberResponse(Long memberId) {
        Member member =  memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
        return MemberResponse.fromMember(member);
    }

    /**
     * 특정 직원의 Proxy 객체를 조회합니다.
     * @param memberId 대상 직원 ID
     * @return 요청 직원 Proxy 객체
     */
    public Member getMemberReference(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }

    /**
     * 특정 직원을 ID로 조회합니다.
     * @param memberId 대상 직원 ID
     * @return 요청 직원
     */
    public Member findOneMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Not exists employee."));
    }

    public Member findOneMemberWithTeam(Long memberId) {
        return memberRepository.findMemberWithTeamById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Not exists employee."));
    }

    /**
     * 특정 직원을 이름으로 조회합니다.
     * @param name 대상 직원 이름
     * @return 요청 직원
     */
    public Member findOneMember(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Not exists employee."));
    }

    /**
     * 특정 직원을 삭제합니다.
     * @param memberId 대상 직원 ID
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = getMemberReference(memberId);
        memberRepository.delete(member);
    }

    /**
     * 사용할 연차가 팀의 연차정책에 부합하는지 확인합니다.
     * @param teamId 대상 팀 ID
     * @param remainingDay 사용할 연차까지의 남은 일 수
     * @return 사용 가능 여부
     */
    public boolean checkLeavePolicy(Long teamId, int remainingDay) {
        Integer result = memberRepository.canUseLeave(teamId, remainingDay)
                .orElse(0);
        return result == 1;
    }
}
