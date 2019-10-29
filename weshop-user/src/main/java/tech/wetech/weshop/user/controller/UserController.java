package tech.wetech.weshop.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wetech.weshop.common.api.BaseApi;
import tech.wetech.weshop.user.api.UserApi;
import tech.wetech.weshop.user.po.User;

@RestController
@RequestMapping("/user")
public class UserController extends BaseApi<User> implements UserApi {
    //UserApi是用于在BaseApi<User>之外拓展Api的，
}
