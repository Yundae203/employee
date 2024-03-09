package api.employee.controller;

import api.employee.model.MemberForm;
import api.employee.model.MemberResponse;
import api.employee.model.MemberUpdateForm;
import api.employee.service.domain.MemberService;
import api.employee.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final OrganizationService organizationService;

    @PostMapping("/member")
    public void saveMember(@RequestBody MemberForm memberForm) {
        organizationService.joinMember(memberForm);
    }

    @GetMapping("/member-list")
    public List<MemberResponse> findAllMember() {
        return memberService.findAllMemberResponse();
    }

    @GetMapping("/member/{memberId}")
    public MemberResponse findOneMember(@PathVariable(value = "memberId") Long memberId) {
        return memberService.findOneMemberResponse(memberId);
    }

    @PutMapping("/member")
    public void updateMember(@RequestBody MemberUpdateForm memberUpdateForm) {
        organizationService.updateMember(memberUpdateForm);
    }

    @DeleteMapping("/member/{memberId}")
    public void deleteMember(@PathVariable(value = "memberId") Long memberId) {
        memberService.deleteMember(memberId);
    }
}
