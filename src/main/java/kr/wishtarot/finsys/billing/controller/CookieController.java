package kr.wishtarot.finsys.billing.controller;

import kr.wishtarot.finsys.billing.model.UseCookieRequest;
import kr.wishtarot.finsys.billing.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cookie")
public class CookieController {

    @Autowired
    private CookieService cookieService;

    @PostMapping("/use")
    public ResponseEntity<?> useCookies(HttpServletRequest request, @RequestBody UseCookieRequest useCookieRequest) {
        try {
            String ipAddress = request.getRemoteAddr();
            cookieService.useCookies(useCookieRequest.getUserId(), useCookieRequest.getAmount(), useCookieRequest.getServiceId(), ipAddress);
            return ResponseEntity.ok("Cookies used successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
