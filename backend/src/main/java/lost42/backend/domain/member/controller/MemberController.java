package lost42.backend.domain.member.controller;

import lombok.AllArgsConstructor;
import lost42.backend.domain.member.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq req) {
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/signup")
    public ResponseEntity<?> checkDuplicateEmail(@RequestParam String email) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/find-email")
    public ResponseEntity<?> findEmail(@RequestBody FindEmailReq req) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordReq req) {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordReq req) {
        return ResponseEntity.ok().body("");
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
