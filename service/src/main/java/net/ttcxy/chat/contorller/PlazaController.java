package net.ttcxy.chat.contorller;

import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.api.ResponseResult;
import net.ttcxy.chat.entity.CurrentMember;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.security.CurrentUtil;
import net.ttcxy.chat.service.GangService;
import net.ttcxy.chat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("plaza")
public class PlazaController {

    private final GangService gangService;

    private final MemberService memberService;

    @Autowired
    public PlazaController(GangService gangService, MemberService memberService) {
        this.gangService = gangService;
        this.memberService = memberService;
    }

    @GetMapping("gang/list")
    public ResponseResult<List<Gang>> gangList(){
        List<Gang> gangs = gangService.selectAllGang();
        return ResponseResult.success(gangs);
    }

    /**
     * 加入Gang
     */
    @PostMapping("gang/{id}")
    public ResponseResult<Integer> add(@PathVariable(name = "id") String id){
        CurrentMember member = CurrentUtil.member();
        String memberId = member.getMember().getId();
        int count = gangService.insertGangMember(id, memberId);
        return ResponseResult.success(count);
    }
}
