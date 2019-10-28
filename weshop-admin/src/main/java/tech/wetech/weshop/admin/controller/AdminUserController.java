package tech.wetech.weshop.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wetech.weshop.common.utils.Result;
import tech.wetech.weshop.user.api.UserApi;

/**
 * @author cjbi@outlook.com
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    //通过springCloud调用userApi ，Client
    @Autowired
    private UserApi userApi;

    @GetMapping("/")
    public String admin() {
        return "admin-success";
    }

    @GetMapping("/api")
    public Result<String> user() {
        return userApi.sayHello("admin");
    }
}
