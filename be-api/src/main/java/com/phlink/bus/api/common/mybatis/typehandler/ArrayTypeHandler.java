package com.phlink.bus.api.common.mybatis.typehandler;

import org.apache.ibatis.type.*;

import java.sql.*;
import java.util.List;

@MappedTypes({List.class, Integer[].class, Long[].class, String[].class, Boolean[].class, Double[].class})
@MappedJdbcTypes({JdbcType.ARRAY})
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {
    private static final String TYPE_NAME_VARCHAR = "varchar";
    private static final String TYPE_NAME_INTEGER = "integer";
    private static final String TYPE_NAME_BOOLEAN = "boolean";
    private static final String TYPE_NAME_NUMERIC = "numeric";
    private static final String TYPE_NAME_BIGINT = "bigint";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter, JdbcType jdbcType) throws SQLException {
        String typeName = null;
        if (parameter instanceof Integer[]) {
            typeName = TYPE_NAME_INTEGER;
        } else if (parameter instanceof String[]) {
            typeName = TYPE_NAME_VARCHAR;
        } else if (parameter instanceof Boolean[]) {
            typeName = TYPE_NAME_BOOLEAN;
        } else if (parameter instanceof Double[]) {
            typeName = TYPE_NAME_NUMERIC;
        } else if (parameter instanceof Long[]) {
            typeName = TYPE_NAME_BIGINT;
        }

        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
        }

        // 这3行是关键的代码，创建Array，然后ps.setArray(i, array)就可以了
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(typeName, parameter);
        ps.setArray(i, array);
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getArray(resultSet.getArray(s));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getArray(resultSet.getArray(i));
    }

    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getArray(callableStatement.getArray(i));
    }

    private Object[] getArray(Array array) {
        if (array == null) {
            return null;
        }
        try {
            return (Object[]) array.getArray();
        } catch (Exception e) {
        }
        return null;
    }

}