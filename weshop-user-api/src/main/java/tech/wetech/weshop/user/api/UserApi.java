package tech.wetech.weshop.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import tech.wetech.weshop.common.api.Api;
import tech.wetech.weshop.user.fallback.UserApiFallback;
import tech.wetech.weshop.user.po.User;

/**
 * 额外写一个api出来调用,因为需要写@FeignClient注解，父接口不能代替
 * <p>
 * 并且该接口可以写userApi特有的方法
 */
@FeignClient(value = "weshop-user", path = "/shop/user", fallback = UserApiFallback.class)
public interface UserApi extends Api<User> {
}
