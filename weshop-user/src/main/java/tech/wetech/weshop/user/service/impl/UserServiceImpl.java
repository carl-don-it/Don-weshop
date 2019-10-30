package tech.wetech.weshop.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wetech.weshop.common.service.BaseService;
import tech.wetech.weshop.common.utils.Criteria;
import tech.wetech.weshop.user.mapper.UserMapper;
import tech.wetech.weshop.user.po.User;
import tech.wetech.weshop.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjbi@outlook.com
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> testSql() {
        return userMapper.selectBySql("select * from weshop_user");
    }

    @Override
    public List<User> testCriteria() {
        Criteria<User, Object> criteria = Criteria.of(User.class);
        criteria.fields(User::getId, User::getAvatar, User::getLastLoginIp, User::getNickname);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");

        criteria.andIn(User::getUserLevelId, strings);
        criteria.page(2, 2);

        String sql = criteria.buildSql();
        return userMapper.selectBySql(sql);

    }

    @Override
    public User testXml() {
        return userMapper.testXml();
    }
}
