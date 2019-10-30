package tech.wetech.weshop.user.fallback;

import org.springframework.stereotype.Component;
import tech.wetech.weshop.common.enums.CommonResultStatus;
import tech.wetech.weshop.common.fallback.ApiFallback;
import tech.wetech.weshop.common.utils.Result;
import tech.wetech.weshop.user.api.UserApi;
import tech.wetech.weshop.user.po.User;

import java.util.List;

@Component
public class UserApiFallback extends ApiFallback<User> implements UserApi {

    @Override
    public Result<List<User>> testSql() {
        return (Result.failure(CommonResultStatus.REMOTE_SERVICE_ERROR));
    }

    @Override
    public Result<List<User>> testCriteria() {
        return (Result.failure(CommonResultStatus.REMOTE_SERVICE_ERROR));
    }

    @Override
    public Result<User> testXml() {
        return (Result.failure(CommonResultStatus.REMOTE_SERVICE_ERROR));
    }
}
