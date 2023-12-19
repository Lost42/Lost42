package lost42.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.SuccessResponse;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.common.mail.EmailMessage;
import lost42.backend.common.mail.EmailService;
import lost42.backend.common.redis.authToken.AuthTokenService;
import lost42.backend.domain.member.dto.*;
import lost42.backend.domain.member.service.LogInService;
import lost42.backend.domain.member.service.LogOutService;
import lost42.backend.domain.member.service.MemberService;
import lost42.backend.domain.member.service.SignUpService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final LogInService logInService;
    private final SignUpService signUpService;
    private final LogOutService logOutService;
    private final EmailService emailService;
    private final AuthTokenService authTokenService;

    @GetMapping("/my")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(memberService.getUserInfo(securityUser)));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        String[] tokens= logInService.login(req);

        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok().headers(headers).header("Set-Cookie", cookie.toString()).body("");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq req) {
        return ResponseEntity.ok().body(signUpService.SignUp(req));
    }

    @GetMapping("/signup")
    public ResponseEntity<?> checkDuplicateEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(SuccessResponse.from(signUpService.checkDuplicateEmail(email)));
    }

    @GetMapping("/auth")
    public ResponseEntity<?> authCheck(@RequestParam("id") Long memberId, @RequestParam("token") String token) {
        authTokenService.validate(memberId, token);

        return ResponseEntity.ok().body("인증 완료");
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordReq req) {
        emailService.sendMail(req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("name") String name, @RequestParam("email") String email, @RequestBody ResetPasswordReq req) {
        return ResponseEntity.ok().body(SuccessResponse.from(memberService.resetPassword(name, email, req)));
    }

    @PatchMapping("/change")
    public ResponseEntity<?> changeUserData(@RequestBody ChangeUserDataReq req, @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(memberService.changeUserData(req, securityUser)));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        logOutService.logOut(securityUser);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails securityUser) {
        return ResponseEntity.ok().body(SuccessResponse.from(memberService.withdrawalMember(securityUser)));
    }

}
