package net.ttcxy.chat.contorller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/other")
public class OtherController {
    
    @GetMapping
    public int selectIdByUsernameMember(){
        return 1;
    }

    @RequestMapping("/selectTime/{id}")
    public String selectDataTimeById(String id){
        return "memberService.selectDataTimeById(id)";
    }
}
