package tech.wetech.weshop.user.service;

import tech.wetech.weshop.common.service.IService;
import tech.wetech.weshop.common.utils.ResultStatus;
import tech.wetech.weshop.user.po.User;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> testSql();

    List<User> testCriteria();

    User testXml();

}
