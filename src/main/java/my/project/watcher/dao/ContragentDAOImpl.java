package my.project.watcher.dao;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Morozov on 18.05.2017.
 */

@Repository
public class ContragentDAOImpl implements ContragentDAO {

    private Logger logger = LogManager.getLogger(getClass());

    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private void removeContragentDetailsById(long contragentId) {
        final String SQL = "DELETE FROM contragent_detail WHERE contragent = ?";
        parameterJdbcTemplate.getJdbcOperations().update(SQL, contragentId);
    }

    private void insertContragentDetails(final long contragentId, final List<Contragent.ContactDetail> details) {
        final String SQL = "INSERT INTO contragent_detail (contragent, type, value) VALUES (?,?,?)";
        parameterJdbcTemplate.getJdbcOperations().batchUpdate(SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, contragentId);
                preparedStatement.setLong(2, details.get(i).getTypeId());
                preparedStatement.setString(3, details.get(i).getValue());
            }

            @Override
            public int getBatchSize() {
                return details.size();
            }
        });
    }

    @Override
    public void saveDigitalKeyContacts(long digitalKeyId, List<Contragent> contacts, String contactType) {
        final String sql = "INSERT INTO digital_key_contacts (digital_key, contragent, type) VALUES (?, ?, ?)";
        parameterJdbcTemplate.getJdbcOperations().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, digitalKeyId);
                preparedStatement.setLong(2, contacts.get(i).getId());
                preparedStatement.setString(3, contactType);
            }

            @Override
            public int getBatchSize() {
                return contacts.size();
            }
        });
    }

    @Override
    public void removeDigitalKeyContacts(long digitalKeyId) {
        final String sql = "DELETE FROM digital_key_contacts WHERE digital_key = ?";
        parameterJdbcTemplate.getJdbcOperations().update(sql, digitalKeyId);
    }

    @Override
    public List<Contragent> getDigitalKeyContacts(long digitalKeyId, String contactType) {
        final String SQL = "SELECT con.id, con.name, con.description FROM digital_key_contacts dkc " +
                "LEFT JOIN contragent con ON dkc.contragent = con.id " +
                "WHERE dkc.digital_key = ? AND NOT con.removed AND dkc.type = ?";
        return parameterJdbcTemplate.getJdbcOperations()
                .query(SQL, new ContragentMapper(), digitalKeyId, contactType);
    }

    @Override
    public Map<Long, String> getDetailTypes() {
        final String SQL = "SELECT * FROM contragent_detail_type";
        return parameterJdbcTemplate.getJdbcOperations().query(SQL, (ResultSet rs) -> {
            Map<Long, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getLong("id"), rs.getString("name"));
            }
            return map;
        });
    }

    @Override
    public void save(final Contragent contragent) {
        final String SQL = "INSERT INTO contragent (name,description) " +
                "VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        parameterJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL, new String[]{"id"});
                ps.setString(1, contragent.getName());
                ps.setString(2, contragent.getDescription());
                return ps;
            }
        }, keyHolder);
        contragent.setId(keyHolder.getKey().longValue());
        insertContragentDetails(contragent.getId(), contragent.getContactDetails());
    }

    @Override
    public void update(Contragent contragent) {
        final String SQL = "UPDATE contragent SET name = :name, description = :description " +
                "WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", contragent.getId());
        params.put("name", contragent.getName());
        params.put("description", contragent.getDescription());
        parameterJdbcTemplate.update(SQL, params);
        removeContragentDetailsById(contragent.getId());
        insertContragentDetails(contragent.getId(), contragent.getContactDetails());
    }

    @Override
    public List<Contragent> getAllByIds(List<Long> ids) {
        final String sql = "SELECT * FROM contragent WHERE id IN (:ids) AND NOT removed";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return parameterJdbcTemplate.query(sql, params, new ContragentMapper());
    }

    @Override
    public List<Contragent> getAllByName(String search) {
        final String sql = "SELECT * FROM contragent WHERE lower(name) LIKE lower(:search) AND NOT REMOVED";
        Map<String, Object> params = new HashMap<>();
        params.put("search", '%' + search + '%');
        return parameterJdbcTemplate.query(sql, params, new ContragentMapper());
    }

    @Override
    public Contragent getById(long id) {
        final String sql = "SELECT * FROM contragent WHERE id = ?";
        return parameterJdbcTemplate.getJdbcOperations().queryForObject(sql, new ContragentMapper(), id);
    }

    @Override
    public Contragent getByIdWithDetails(long id) {
        Contragent contragent = getById(id);
        List<Contragent.ContactDetail> contactDetails = getContragentDetails(id);
        if (contragent != null && contactDetails != null) {
            contragent.setContactDetails(contactDetails);
        }
        return contragent;
    }

    @Override
    public List<Contragent> getAll() {
        final String sql = "SELECT * FROM contragent WHERE NOT removed";
        return parameterJdbcTemplate.getJdbcOperations().query(sql, new ContragentMapper());
    }

    @Override
    public List<Contragent> getAllByType(String type) {
        final String SQL = "SELECT DISTINCT con.id, con.name, con.description FROM contragent con " +
                "LEFT JOIN digital_key_contacts dkc ON con.id = dkc.contragent " +
                "WHERE NOT con.removed AND dkc.type = ?";
        return parameterJdbcTemplate.getJdbcOperations().query(SQL, new ContragentMapper(), type);
    }

    @Override
    public void setRemoved(long contragentId) {
        final String SQL = "UPDATE contragent SET removed = TRUE WHERE id = ?";
        parameterJdbcTemplate.getJdbcOperations().update(SQL, contragentId);
    }

    private List<Contragent.ContactDetail> getContragentDetails(long contragentId) {
        final String sql = "SELECT d.id, d.value, t.id AS type_id, t.name AS type_name FROM contragent_detail d " +
                "LEFT JOIN contragent_detail_type t ON d.type = t.id WHERE d.contragent = ?";
        return parameterJdbcTemplate.getJdbcOperations().query(sql, new ContactInfoMapper(), contragentId);
    }

    private static class ContragentMapper implements RowMapper<Contragent> {
        @Override
        public Contragent mapRow(ResultSet resultSet, int i) throws SQLException {
            Contragent contragent = new Contragent();
            contragent.setId(resultSet.getLong("id"));
            contragent.setName(resultSet.getString("name"));
            contragent.setDescription(resultSet.getString("description"));
            return contragent;
        }
    }

    private static class ContactInfoMapper implements RowMapper<Contragent.ContactDetail> {
        @Override
        public Contragent.ContactDetail mapRow(ResultSet resultSet, int i) throws SQLException {
            Contragent.ContactDetail contactDetail = new Contragent.ContactDetail();
            contactDetail.setId(resultSet.getLong("id"));
            contactDetail.setValue(resultSet.getString("value"));
            contactDetail.setTypeId(resultSet.getLong("type_id"));
            contactDetail.setTypeName(resultSet.getString("type_name"));
            return contactDetail;
        }
    }
}
