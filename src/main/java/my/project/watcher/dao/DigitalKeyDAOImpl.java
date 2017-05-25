package my.project.watcher.dao;

import my.project.watcher.model.DigitalKey;
import my.project.watcher.model.Contragent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Morozov on 18.05.2017.
 */
@Repository
public class DigitalKeyDAOImpl implements DigitalKeyDAO {

    private Logger logger = LogManager.getLogger(getClass());

    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void update(DigitalKey digitalKey) {
        final String sql = "UPDATE digital_key SET " +
                "name = :name, serial = :serial, description = :description, expire = :expire " +
                "WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        long digitalKeyId = digitalKey.getId();
        params.put("id", digitalKeyId);
        params.put("name", digitalKey.getName());
        params.put("serial", digitalKey.getSerial());
        params.put("description", digitalKey.getDescription());
        params.put("expire", digitalKey.getExpire());
        parameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void save(final DigitalKey digitalKey) {
        final String sql = "INSERT INTO digital_key (name, serial, description, expire) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        parameterJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                long date = digitalKey.getExpire().getTime();
                ps.setString(1, digitalKey.getName());
                ps.setString(2, digitalKey.getSerial());
                ps.setString(3, digitalKey.getDescription());
                ps.setDate(4, new Date(date));
                return ps;
            }
        }, keyHolder);
        long generatedId = keyHolder.getKey().longValue();
        digitalKey.setId(generatedId);
    }

    @Override
    public List<DigitalKey> getAllByContragentId(long contragentId) {
        final String sql = "SELECT DISTINCT d.id, d.name, d.serial, d.description, d.expire FROM digital_key_contacts c " +
                "LEFT JOIN digital_key d ON c.digital_key = d.id " +
                "WHERE c.contragent = ? AND NOT d.removed ORDER BY d.expire";
        return parameterJdbcTemplate.getJdbcOperations().query(sql, new DigitalKeyMapper(), contragentId);
    }

    @Override
    public List<DigitalKey> getAll() {
        final String sql = "SELECT * FROM digital_key WHERE NOT removed";
        return parameterJdbcTemplate.query(sql, new DigitalKeyMapper());
    }

    @Override
    public void setRemoved(long id) {
        final String SQL = "UPDATE digital_key SET removed = TRUE WHERE id = ?";
        parameterJdbcTemplate.getJdbcOperations().update(SQL, id);
    }

    @Override
    public DigitalKey getById(long id) {
        final String sql = "SELECT * FROM digital_key WHERE id = ?";
        DigitalKey digitalKey = parameterJdbcTemplate
                .getJdbcOperations()
                .queryForObject(sql, new DigitalKeyMapper(), id);
        return digitalKey;
    }

    private class DigitalKeyMapper implements RowMapper<DigitalKey> {
        @Override
        public DigitalKey mapRow(ResultSet resultSet, int i) throws SQLException {
            DigitalKey digitalKey = new DigitalKey();
            digitalKey.setId(resultSet.getLong("id"));
            digitalKey.setSerial(resultSet.getString("serial"));
            digitalKey.setName(resultSet.getString("name"));
            digitalKey.setDescription(resultSet.getString("description"));
            digitalKey.setExpire(resultSet.getDate("expire"));
            return digitalKey;
        }
    }
}
