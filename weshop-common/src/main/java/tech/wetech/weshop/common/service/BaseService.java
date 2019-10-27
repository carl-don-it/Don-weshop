package tech.wetech.weshop.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.wetech.weshop.common.utils.Criteria;

import java.util.List;

/**
 * @param <T>
 * @author cjbi@outlook.com
 */
@Component
//public abstract class BaseService<T> implements IService<T> {
public class BaseService<T> implements IService<T> {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<T> queryAll() {
        return null;
    }

    @Override
    public List<T> queryList(T entity) {
        return null;
    }

    @Override
    public List<T> queryByCriteria(Criteria<T, Object> criteria) {
        return null;
    }

    @Override
    public T queryOneByCriteria(Criteria<T, Object> criteria) {
        return null;
    }

    @Override
    public int countByCriteria(Criteria<T, Object> criteria) {
        return 0;
    }

    @Override
    public T queryOne(T entity) {
        return null;
    }

    @Override
    public T queryById(Object id) {
        return null;
    }

    @Override
    public int create(T entity) {
        return 0;
    }

    @Override
    public int createBatch(List<T> list) {
        return 0;
    }

    @Override
    public int updateAll(T entity) {
        return 0;
    }

    @Override
    public int updateNotNull(T entity) {
        return 0;
    }

    @Override
    public int delete(T entity) {
        return 0;
    }

    @Override
    public int deleteById(Object id) {
        return 0;
    }

    @Override
    public int count(T entity) {
        return 0;
    }

    //@Autowired
    //protected MyMapper<T> mapper;

}
