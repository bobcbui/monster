package net.ttcxy.chat.contorller;


import net.ttcxy.chat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/other")
public class OtherController {
    private final MemberService memberService;

    @Autowired
    public OtherController(MemberService memberService) {
        this.memberService = memberService;
    }
    @RequestMapping("/selectId")
    public int selectIdByUsernameMember(String a){
        return memberService.selectIdByUsernameMember(a);
    }

    @RequestMapping("/selectTime/{id}")
    public String selectDataTimeById(String id){
        return memberService.selectDataTimeById(id);
    }
}
