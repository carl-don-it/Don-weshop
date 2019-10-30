package tech.wetech.weshop.common.utils;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 需要结合jpa注解一起使用
 * <p>
 * 用于生成sql语句，转化为java String拼接，不好用，繁琐
 *
 * @param <A> 具体的po类对象
 * @param <B> Fn<A, B>中的B，具体到方法来说，String比较和是，项目基本使用Object * @author cjbi@outlook.com
 */
@Data
public class Criteria<A, B> implements Serializable {

    private Statement statement;

    //缓存Criteria<A, B>
    private static final Map<Class<?>, EntityTable> entityTableCache = new ConcurrentHashMap<>();

    /**
     * 构建具体sql语句所需要的各部分，Criteria<A, B>的大部分方法就是为了构建这个东西
     */
    @Data
    public static class Statement<A> {

        //类名
        private Class<A> clazz;

        //字段
        private String[] fields;

        //条件
        private List<Criterion> criterions;

        //第几页
        private int pageNum;

        //数量
        private int pageSize;

        //排序字段
        private String[] sortNames;

        private String sortOrder;
    }

    /**
     * 该po所对应的所有field和properties
     */
    @Data
    public static class EntityTable {
        private String tableName;
        private Map<String, String> fieldsMap;
    }

    /**
     * statement中具体查询条件，orm思维
     */
    @Data
    public static class Criterion {
        private String property;
        private Object value;
        private Object secondValue;
        private String condition;
        private String andOr;
        private ValueType valueType;

        public enum ValueType {
            noValue, singleValue, betweenValue, listValue
        }

        public Criterion() {
        }

        public Criterion(String property, String condition, String andOr) {
            this.property = property;
            this.condition = condition;
            this.andOr = andOr;
            this.valueType = ValueType.noValue;
        }

        public Criterion(String property, Object value, String condition, String andOr) {
            this.property = property;
            this.value = value;
            this.condition = condition;
            this.andOr = andOr;

            if (value instanceof Iterable) {
                this.valueType = ValueType.listValue;
            } else {
                this.valueType = ValueType.singleValue;
            }
        }

        public Criterion(String property, Object value1, Object value2, String condition, String andOr) {
            this.property = property;
            this.value = value1;
            this.secondValue = value2;
            this.condition = condition;
            this.andOr = andOr;
            this.valueType = ValueType.betweenValue;
        }

    }

    /**
     * 缓存实体类和表的映射关系
     *
     * @param entityClass
     */
    private void cacheEntityTable(Class<A> entityClass) {
        if (!entityTableCache.containsKey(entityClass)) {
            Table table = entityClass.getAnnotation(Table.class);
            Map<String, String> fieldsMap = new LinkedHashMap<>();//<fieldName,columnName>
            Field[] declaredFields = entityClass.getDeclaredFields();

            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Column.class)) {
                    Column column = declaredField.getAnnotation(Column.class);
                    fieldsMap.put(declaredField.getName(), column.name());
                } else {
                    String s = StringUtils.convertByStyle(declaredField.getName(), StringUtils.Style.camelhump);
                    fieldsMap.put(declaredField.getName(), s);
                }
            }

            EntityTable entityTable = new EntityTable();
            entityTable.tableName = table.name();
            entityTable.fieldsMap = fieldsMap;

            entityTableCache.put(entityClass, entityTable);
        }
    }

    public Criteria() {
    }

    private Criteria(Class entityClass) {
        statement = new Statement();
        statement.criterions = new LinkedList();
        statement.clazz = entityClass;
    }

    /**
     * 静态创建Criteria,第一次不赋值那么B都是Object了
     *
     * @param entityClass
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Criteria<A, B> of(Class<A> entityClass) {
        return new Criteria<>(entityClass);
    }

    public Criteria<A, B> page(Integer pageNum, Integer pageSize) {
        this.statement.pageNum = pageNum;
        this.statement.pageSize = pageSize;
        return this;
    }

    public Criteria<A, B> sort(Fn<A, B>... fns) {
        this.statement.sortNames = Reflections.fnToFieldName(fns);
        return this;
    }

    public Criteria<A, B> sortDesc(Fn<A, B>... fns) {
        this.statement.sortNames = Reflections.fnToFieldName(fns);
        this.statement.sortOrder = "desc";
        return this;
    }

    public Criteria<A, B> fields(Fn<A, B>... fns) {
        this.statement.fields = Reflections.fnToFieldName(fns);
        return this;
    }

    public Criteria<A, B> andIsNotNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), "is not null", "and"));
        return this;
    }

    public Criteria<A, B> andEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "=", "and"));
        return this;
    }

    public Criteria<A, B> andNotEqualTo(Fn<A, B> fn, Object value) {
        new Criterion(Reflections.fnToFieldName(fn), value, "<>", "and");
        return this;
    }

    public Criteria<A, B> andGreaterThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, ">", "and"));
        return this;
    }

    public Criteria<A, B> andGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, ">=", "and"));
        return this;
    }

    public Criteria<A, B> andLessThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "<", "and"));
        return this;
    }

    public Criteria<A, B> andLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "<=", "and"));
        return this;
    }

    public Criteria<A, B> andIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), values, "in", "and"));
        return this;
    }

    public Criteria<A, B> andNotIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), values, "not in", "and"));
        return this;
    }

    public Criteria<A, B> andBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value1, value2, "between", "and"));
        return this;
    }

    public Criteria<A, B> andNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value1, value2, "not between", "and"));
        return this;
    }

    public Criteria<A, B> andLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "like", "and"));
        return this;
    }

    public Criteria<A, B> andNotLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "not like", "and"));
        return this;
    }

    public Criteria<A, B> orIsNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), "is null", "or"));
        return this;
    }

    public Criteria<A, B> orIsNotNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), "is not null", "or"));
        return this;
    }

    public Criteria<A, B> orEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "=", "or"));
        return this;
    }

    public Criteria<A, B> orNotEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "<>", "or"));
        return this;
    }

    public Criteria<A, B> orGreaterThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, ">", "or"));
        return this;
    }

    public Criteria<A, B> orGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, ">=", "or"));
        return this;
    }

    public Criteria<A, B> orLessThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "<", "or"));
        return this;
    }

    public Criteria<A, B> orLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "<=", "or"));
        return this;
    }

    public Criteria<A, B> orIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), values, "in", "or"));
        return this;
    }

    public Criteria<A, B> orNotIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), values, "not in", "or"));
        return this;
    }

    public Criteria<A, B> orBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value1, value2, "between", "or"));
        return this;
    }

    public Criteria<A, B> orNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value1, value2, "not between", "or"));
        return this;
    }

    public Criteria<A, B> orLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "like", "or"));
        return this;
    }

    public Criteria<A, B> orNotLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criterion(Reflections.fnToFieldName(fn), value, "not like", "or"));
        return this;
    }

    /**
     * 最终方法之一：构建普通sql
     *
     * @return
     */
    public String buildSql() {
        cacheEntityTable(statement.clazz);
        StringBuilder sql = new StringBuilder("");
        sql.append(SqlHelper.selectColumns(statement));
        sql.append(SqlHelper.fromTable(statement));
        sql.append(SqlHelper.whereClause(statement));
        sql.append(SqlHelper.orderByClause(statement));
        sql.append(SqlHelper.limit(statement));
        return sql.toString();
    }

    /**
     * 最终方法之二：构建统计的sql
     *
     * @return
     */
    public String buildCountSql() {
        cacheEntityTable(statement.clazz);
        StringBuilder sql = new StringBuilder("");
        sql.append(SqlHelper.selectCount(statement));
        sql.append(SqlHelper.fromTable(statement));
        sql.append(SqlHelper.whereClause(statement));
        return sql.toString();
    }

    /**
     * 用于根据statement生成具体的sql语句
     */
    public static class SqlHelper {
        /**
         * 决定要查找哪些列
         */
        public static String selectColumns(Statement statement) {
            StringBuilder selectColumns = new StringBuilder("select ");
            EntityTable entityTable = entityTableCache.get(statement.clazz);
            //从statement中一个个拿出来，拼接
            if (statement.fields != null && statement.fields.length > 0) {
                List<String> columns = new ArrayList<>();
                for (String fieldStr : statement.fields) {//TODO 改为String
                    columns.add(entityTable.fieldsMap.get(fieldStr));
                }
                return selectColumns.append(String.join(",", columns)).toString();
            } else {
                Map<String, String> fieldsMap = entityTable.fieldsMap;
                return selectColumns.append(fieldsMap.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.joining(","))).toString();
            }
        }

        public static String selectCount(Statement statement) {
            return "select count(*)";
        }

        public static String fromTable(Statement statement) {
            EntityTable entityTable = entityTableCache.get(statement.clazz);
            return " from " + entityTable.tableName;
        }

        /**
         * 条件语句
         *
         * @param statement
         * @return
         */
        public static String whereClause(Statement statement) {
            EntityTable entityTable = entityTableCache.get(statement.clazz);
            List<Criterion> criterions = statement.criterions;
            StringBuilder whereClause = new StringBuilder();
            if (criterions.size() > 0) {
                for (int i = 0; i < criterions.size(); i++) {
                    Criterion criterion = criterions.get(i);
                    if (i == 0) {
                        whereClause.append(" where ");
                    } else {
                        whereClause.append(" " + criterion.andOr + " ");
                    }
                    if (criterion.valueType == Criterion.ValueType.noValue) {
                        whereClause.append(entityTable.fieldsMap.get(criterion.property) + " " + criterion.condition);
                    }
                    if (criterion.valueType == Criterion.ValueType.singleValue) {
                        if (!(criterion.value instanceof Number || criterion.value instanceof Boolean)) {
                            criterion.value = "'".concat(criterion.value.toString()).concat("'");
                        }
                        whereClause.append(entityTable.fieldsMap.get(criterion.property) + " " + criterion.condition + " " + criterion.value);
                    }
                    if (criterion.valueType == Criterion.ValueType.betweenValue) {
                        if (!(criterion.value instanceof Number || criterion.value instanceof Boolean)) {
                            criterion.value = "'".concat(criterion.value.toString()).concat("'");
                        }
                        if (!(criterion.secondValue instanceof Number || criterion.value instanceof Boolean)) {
                            criterion.secondValue = "'".concat(criterion.secondValue.toString()).concat("'");
                        }
                        whereClause.append(entityTable.fieldsMap.get(criterion.property) + " " + criterion.condition + " " + criterion.value + " AND " + criterion.secondValue);
                    }
                    if (criterion.valueType == Criterion.ValueType.listValue) {
                        Iterable iterable = (Iterable) criterion.value;
                        StringBuilder listItem = new StringBuilder();
                        for (Object o : iterable) {
                            if (!(o instanceof Number || criterion.value instanceof Boolean)) {
                                o = "'".concat(o.toString()).concat("'");
                            }
                            listItem.append(o).append(",");
                        }
                        if (listItem.length() > 0) {
                            listItem.deleteCharAt(listItem.length() - 1);
                        }
                        whereClause.append(entityTable.fieldsMap.get(criterion.property) + " " + criterion.condition + " (" + listItem + ")");
                    }
                }
            }
            return whereClause.toString();
        }

        //这里有问题，每个sortName都可以特有的sortOrder，不一定只有一个
        public static String orderByClause(Statement statement) {
            if (statement.sortNames == null) {
                return "";
            }
            EntityTable entityTable = entityTableCache.get(statement.clazz);
            StringBuilder orderByClause = new StringBuilder();
            for (int i = 0; i < statement.sortNames.length; i++) {
                if (i == 0) {
                    orderByClause.append(" order by ");
                }
                orderByClause.append(entityTable.fieldsMap.get(statement.sortNames[i])).append(",");
            }
            //删除最后的","
            if (orderByClause.length() > 0) {
                orderByClause.deleteCharAt(orderByClause.length() - 1);
            }
            //加上最后的排序
            if (statement.sortOrder != null) {
                orderByClause.append(" " + statement.sortOrder);
            }
            return orderByClause.toString();
        }

        public static String limit(Statement statement) {
            if (statement.pageSize > 0) {
                Integer offset = statement.pageSize;
                if (statement.pageNum > 1) {
                    Integer limit = (statement.pageNum - 1) * statement.pageSize;
                    return " limit " + limit + "," + offset;
                }
                return " limit " + offset;
            }
            return "";
        }

    }

}
