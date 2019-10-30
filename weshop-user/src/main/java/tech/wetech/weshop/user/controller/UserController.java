package tech.wetech.weshop.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wetech.weshop.common.api.BaseApi;
import tech.wetech.weshop.common.utils.Result;
import tech.wetech.weshop.user.api.UserApi;
import tech.wetech.weshop.user.po.User;
import tech.wetech.weshop.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseApi<User> implements UserApi {

    //UserApi是用于在BaseApi<User>之外拓展Api的，

    @Autowired
    private UserService userService;

    @Override
    public Result<List<User>> testSql() {
        return (Result.success(userService.testSql()));
    }

    @Override
    public Result<List<User>> testCriteria() {
        return (Result.success(userService.testCriteria()));
    }

    @Override
    public Result<User> testXml() {
        return (Result.success(userService.testXml()));
    }
}
