package com.lance.api.common.base.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 该类是基类的dao
 */
@Repository
@Data
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行Sql语句进行数据修改
     */
    public void execute(String sql) throws DataAccessException
    {
        this.jdbcTemplate.execute(sql);
    }

    /**
     * 查询Sql，返回double
     *
     * @param sql
     * @return
     */
    public double queryForDouble(String sql) throws DataAccessException
    {
        double backVal = 0.0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, Double.class);
            backVal = (number != null ? number.doubleValue() : 0.0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询double
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return double
     */
    public double queryForDouble(String sql, Object[] args) throws DataAccessException
    {
        double backVal = 0.0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, args, Double.class);
            backVal = (number != null ? number.doubleValue() : 0.0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询double
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return double
     */
    public double queryForDouble(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        double backVal = 0.0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, args, argTypes, Double.class);
            backVal = (number != null ? number.doubleValue() : 0.0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询Sql，返回float
     *
     * @param sql
     * @return
     */
    public float queryForFloat(String sql) throws DataAccessException
    {
        float backVal = 0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, Float.class);
            backVal = (number != null ? number.floatValue() : 0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询float
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return
     */
    public float queryForFloat(String sql, Object[] args) throws DataAccessException
    {
        float backVal = 0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, args, Float.class);
            backVal = (number != null ? number.floatValue() : 0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询float
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return
     */
    public float queryForFloat(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        float backVal = 0;
        try
        {
            Number number = (Number) this.jdbcTemplate.queryForObject(sql, args, argTypes, Float.class);
            backVal = (number != null ? number.floatValue() : 0);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询Sql，返回int
     */
    public int queryForInt(String sql) throws DataAccessException
    {
        int backVal = 0;
        try
        {
            Integer temBack = this.jdbcTemplate.queryForObject(sql, Integer.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询int
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return int
     */
    public int queryForInt(String sql, Object[] args) throws DataAccessException
    {
        int backVal = 0;
        try
        {
            Integer temBack = this.jdbcTemplate.queryForObject(sql, args, Integer.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询int
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return
     */
    public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        int backVal = 0;
        try
        {
            Integer temBack = this.jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询Sql，返回List
     */
    public List queryForList(String sql) throws DataAccessException
    {
        List<Map<String, Object>> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.query(sql, new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper(), 0));
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询List
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return List
     */
    public List queryForList(String sql, Object[] args) throws DataAccessException
    {
        List<Map<String, Object>> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.query(sql, args, new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper(), 0));
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询List
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return
     */
    public List queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        List<Map<String, Object>> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.query(sql, args, argTypes, new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper(), 0));
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询Sql，返回long
     */
    public long queryForLong(String sql) throws DataAccessException
    {
        long backVal = 0L;
        try
        {
            Long temBack = this.jdbcTemplate.queryForObject(sql, Long.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }

        return backVal;
    }

    /**
     * 查询long
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return long
     */
    public long queryForLong(String sql, Object[] args) throws DataAccessException
    {
        long backVal = 0L;
        try
        {
            Long temBack = this.jdbcTemplate.queryForObject(sql, args, Long.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询long
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return
     */
    public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        long backVal = 0L;
        try
        {
            Long temBack = this.jdbcTemplate.queryForObject(sql, args, argTypes, Long.class);
            if (null != temBack)
            {
                backVal = temBack;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * @param sql
     * @return Map 如果sql可以查到值，返回Map，否则返回null
     */
    public Map queryForMap(String sql) throws DataAccessException
    {
        Map<String, Object> map = null;
        try
        {
            map = this.jdbcTemplate.queryForMap(sql);
        }
        catch (EmptyResultDataAccessException emptyException)
        {
            map = null;
        }
        return map;
    }

    /**
     * 查询Map
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return Map
     */
    public Map queryForMap(String sql, Object[] args) throws DataAccessException
    {
        Map<String, Object> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.queryForMap(sql, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            backVal = null;
        }
        return backVal;
    }

    /**
     * 查询Map
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return Map
     */
    public Map queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        Map<String, Object> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.queryForMap(sql, args, argTypes);
        }
        catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            backVal = null;
        }
        return backVal;
    }

    /**
     * 执行sql，查询String
     */
    public String queryForString(String sql) throws DataAccessException
    {
        String backVal = null;
        try
        {
            backVal = (String) this.jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            backVal = null;
        }
        return backVal;
    }

    /**
     * 查询String
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return String
     */
    public String queryForString(String sql, Object[] args) throws DataAccessException
    {
        String backVal = null;
        try
        {
            backVal = (String) this.jdbcTemplate.queryForObject(sql, args, String.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            backVal = null;
        }
        return backVal;
    }

    /**
     * 查询String
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return String
     */
    public String queryForString(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        String backVal = null;
        try
        {
            backVal = (String) this.jdbcTemplate.queryForObject(sql, args, argTypes, String.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            backVal = null;
        }
        return backVal;
    }

    /**
     * 查询Sql，返回List
     */
    public <T> List<T> queryForBaseList(String sql) throws DataAccessException
    {
        List<T> backVal = null;
        backVal = this.jdbcTemplate.query(sql, new RowMapper<T>()
        {
            @Override
            public T mapRow(ResultSet rs, int i) throws SQLException
            {
                String val = rs.getString(1);
                return (T) val;
            }
        });
        return backVal;
    }

    /**
     * 查询List
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return List
     */
    public <T> List<T> queryForBaseList(String sql, Object[] args)
    {
        List<T> backVal = null;
        backVal = this.jdbcTemplate.query(sql, args, new RowMapper<T>()
        {
            @Override
            public T mapRow(ResultSet rs, int i) throws SQLException
            {
                String val = rs.getString(1);
                return (T) val;
            }
        });
        return backVal;
    }

    /**
     * 查询List
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return
     */
    public <T> List<T> queryForBaseList(String sql, Object[] args, int[] argTypes)
    {
        List<T> backVal = null;
        backVal = this.jdbcTemplate.query(sql, args, argTypes, new RowMapper<T>()
        {
            @Override
            public T mapRow(ResultSet rs, int i) throws SQLException
            {
                String val = rs.getString(1);
                return (T) val;
            }
        });
        return backVal;
    }

    /**
     * 执行数据修改操作，返回影响的行数
     */
    public int update(final String sql) throws DataAccessException
    {
        int backVal;
        try
        {
            backVal = this.jdbcTemplate.update(sql);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 执行数据库修改操作
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return 影响的数据行数
     * @throws DataAccessException
     */
    public int update(String sql, Object[] args) throws DataAccessException
    {
        int backVal;
        try
        {
            backVal = this.jdbcTemplate.update(sql, args);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 执行数据库修改操作
     *
     * @param sql      预编译带？的sql
     * @param args     参数数组
     * @param argTypes 参数类型数组(使用java.sql.Types常数)
     * @return 影响的数据行数
     * @throws DataAccessException
     */
    public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException
    {
        int backVal;
        try
        {
            backVal = this.jdbcTemplate.update(sql, args, argTypes);
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

    /**
     * 查询 entity List
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return List
     */
    public List queryForModelList(String sql, Object[] args, Class cls) throws DataAccessException
    {
        List<Class> backVal = null;
        try
        {
            backVal = this.jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(cls));
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }


    /**
     * 查询 entity
     *
     * @param sql  预编译带？的sql
     * @param args 参数数组
     * @return entity
     */
    public <T> T queryForModel(String sql, Object[] args, Class<T> cls) throws DataAccessException
    {
        T backVal = null;
        try
        {
            backVal = this.jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<T>(cls));
        }
        catch (EmptyResultDataAccessException dataAccessException)
        {
            backVal = null;
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        return backVal;
    }

}
