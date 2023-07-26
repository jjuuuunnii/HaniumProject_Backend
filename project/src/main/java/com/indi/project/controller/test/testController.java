package com.indi.project.controller.test;


import com.indi.project.security.userService.PrincipalDetails;
import com.indi.project.security.userService.PrincipalDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class testController {

    @RequestMapping("/test")
    public String test(@AuthenticationPrincipal PrincipalDetails principalDetails){
        String name = principalDetails.getUser().getName();
        log.info("testController={}",name);
        return "test";
    }
}
