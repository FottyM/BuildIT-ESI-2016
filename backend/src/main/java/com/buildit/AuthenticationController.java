package com.buildit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by rain on 29.04.16.
 */


@RequestMapping("/api/authenticate")
@RestController
public class AuthenticationController {

    @RequestMapping(method=GET)
    @CrossOrigin
    public List<String> authenticate() throws JsonProcessingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = new LinkedList<String>();

        if (principal instanceof UserDetails) {
            UserDetails details = (UserDetails) principal;
            for (GrantedAuthority authority: details.getAuthorities())
                roles.add(authority.getAuthority());
        }

        return roles;
    }

    @ExceptionHandler(value={SecurityException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleSecurityException() {
    }
}


