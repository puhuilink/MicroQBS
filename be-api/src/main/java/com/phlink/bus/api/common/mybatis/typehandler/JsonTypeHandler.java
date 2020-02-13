package com.phlink.bus.api.common.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map JSON string as value container with JsonNode.
 * Should always return not null value.
 * Use JSON string representation as intermediate data format.
 *
 * @see JSONObject
 */
@MappedTypes({JSONObject.class, JSONArray.class, JSON.class})
public class JsonTypeHandler extends BaseTypeHandler<JSON> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSON parameter, JdbcType jdbcType) throws SQLException {
        if(parameter != null) {
            PGobject json = new PGobject();
            json.setType("json");
            json.setValue(JSON.toJSONString(parameter, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
            ps.setObject(i, json);
        }
    }

    @Override
    public JSON getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        return parseToJSON(jsonSource);
    }

    private JSON parseToJSON(String jsonSource) {
        if(StringUtils.isEmpty(jsonSource)){
            return null;
        }
        if(jsonSource.startsWith("{")) {
            return JSONObject.parseObject(jsonSource);
        }
        if(jsonSource.startsWith("[")) {
            return JSONArray.parseArray(jsonSource);
        }

        return null;
    }

    @Override
    public JSON getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        return parseToJSON(jsonSource);
    }

    @Override
    public JSON getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        return parseToJSON(jsonSource);
    }

    /*
    Override BaseTypeHandler in such way that result will never be null
     */
    @Override
    public JSON getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return getNullableResult(rs, columnName);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
        }
    }

    @Override
    public JSON getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return getNullableResult(rs, columnIndex);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + e, e);
        }
    }

    @Override
    public JSON getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return getNullableResult(cs, columnIndex);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
    }
}