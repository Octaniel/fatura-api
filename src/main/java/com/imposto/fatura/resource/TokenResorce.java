package com.imposto.fatura.resource;

import com.imposto.fatura.config.property.SpringApiProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tokens")
public class TokenResorce {
    private final SpringApiProperty property;

    public TokenResorce(SpringApiProperty property) {
        this.property = property;
    }

    @DeleteMapping("/revoke")
    public void revoke(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(property.getSeguranca().isEnableHttps());//TODO:Mudar para true na producao
        cookie.setPath(request.getContextPath() + "/oauth/token");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
