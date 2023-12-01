package lost42.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.dto.*;
import lost42.backend.domain.member.service.LogInService;
import lost42.backend.domain.member.service.MemberService;
import lost42.backend.domain.member.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final LogInService logInService;
    private final SignUpService signUpService;

    // TODO 토큰 인증 유저 생성 후 진행할 것 : /change, /logout, /delete
    // TODO 이메일 인증 로직 이후 진행할 것 : /find-email, /find-password

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
//        return ResponseEntity.ok().body("Hello, " + securityUser.getUserEmail());
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/my")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        memberService.getUserInfo(securityUser);

        return ResponseEntity.ok().body(memberService.getUserInfo(securityUser));
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
        return ResponseEntity.ok().body(memberService.resetPassword(name, email, req));
    }

    @PatchMapping("/change")
    public ResponseEntity<?> changeUserData(@RequestBody ChangeUserDataReq req, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(memberService.changeUserData(req, securityUser));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body("");
    }

}
