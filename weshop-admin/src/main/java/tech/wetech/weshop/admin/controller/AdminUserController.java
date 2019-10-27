package tech.wetech.weshop.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjbi@outlook.com
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController  {


    @GetMapping("/")
    public String admin() {
        return "admin-success";
    }

}
