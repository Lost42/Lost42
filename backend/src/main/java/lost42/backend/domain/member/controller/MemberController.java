package lost42.backend.domain.member.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }
}
