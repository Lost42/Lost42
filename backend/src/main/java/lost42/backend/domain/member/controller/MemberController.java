package lost42.backend.domain.member.controller;

import lombok.AllArgsConstructor;
import lost42.backend.domain.member.dto.*;
import lost42.backend.domain.member.service.LogInService;
import lost42.backend.domain.member.service.MemberService;
import lost42.backend.domain.member.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final LogInService logInService;
    private final SignUpService signUpService;

    // TODO 토큰 인증 유저 생성 후 진행할 것 : /change, /logout, /delete
    // TODO 이메일 인증 로직 이후 진행할 것 : /find-email, /find-password

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping("/my") // 토큰으로 인증된 유저 생성 후 진행
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        return ResponseEntity.ok().body(logInService.login(req));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq req) {
        return ResponseEntity.ok().body(signUpService.SignUp(req));
    }

    @GetMapping("/signup")
    public ResponseEntity<?> checkDuplicateEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(signUpService.checkDuplicateEmail(email));
    }

    @PostMapping("/find-email")
    public ResponseEntity<?> findEmail(@RequestBody FindEmailReq req) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordReq req) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("name") String name, @RequestParam("email") String email, @RequestBody ResetPasswordReq req) {
        return ResponseEntity.ok().body(memberService.reSetPassword(name, email, req));
    }

    @PatchMapping("/change")
    public ResponseEntity<?> changeUserData(@RequestBody ChangeUserDataReq req) {
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        return ResponseEntity.ok().body("");
    }

}
