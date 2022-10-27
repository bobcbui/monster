package net.ttcxy.chat.contorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.service.MemberGroupService;
import net.ttcxy.chat.service.MemberService;

@RestController
@RequestMapping("plaza")
public class PlazaController {

    @Autowired
    public PlazaController(MemberGroupService memberGroupService, MemberService memberService) {
        
    }

    @GetMapping("gang/list")
    public List<CtsGroup> gangList(){
        return null;
    }

    /**
     * 加入Gang
     */
    @PostMapping("gang/{id}")
    public String add(@PathVariable(name = "id") String id){
        return "";
    }
}
