package net.ttcxy.chat.contorller;

import net.ttcxy.chat.api.ResponseResult;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.service.GangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("gang")
public class GangController {

    private final GangService gangService;

    @Autowired
    public GangController(GangService gangService) {
        this.gangService = gangService;
    }

    @GetMapping("list")
    public ResponseResult<List<Gang>> gangList(){
        List<Gang> gangs = gangService.selectAllGang();
        return ResponseResult.success(gangs);
    }
}
