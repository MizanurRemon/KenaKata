package org.kenakata.DAO;

import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Admin;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.User;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class ApiDaoImplementation implements ApiDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource source) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(source);
        this.jdbcTemplate = new JdbcTemplate(source);
    }

    @Override
    public boolean userRegistration(EntityUser user) {

        String sqlQuery = "INSERT into " + Constants.TBL_USER + " (name,  address, email, phone, password) " + " values(?,?,?,?,?)";

        return jdbcTemplate.update(sqlQuery, user.getName(), user.getAddress(), user.getEmail(), user.getPhone(), user.getPassword()) == 1;


    }

    @Override
    public List<User> getAllUser() {

        String sqlQuery = "SELECT * from " + Constants.TBL_USER;
        try {
            return jdbcTemplate.query(sqlQuery, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                    User user = new User();
                    user.setId(rs.getInt(Constants.ID));
                    user.setName(rs.getString(Constants.NAME));
                    user.setAddress(rs.getString(Constants.ADDRESS));
                    user.setEmail(rs.getString(Constants.EMAIL));
                    user.setPhone(rs.getString(Constants.PHONE));
                    user.setRegDate(rs.getString(Constants.REG_DATE));
                    return user;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean updateUserProfile(User user) {

        String query = "UPDATE " + Constants.TBL_USER + " SET name = ?,address =?, phone=?, email=? WHERE id = ?";

        try {
            return jdbcTemplate.update(
                    query,
                    user.getName(),
                    user.getAddress(),
                    user.getPhone(),
                    user.getEmail(),
                    user.getId()
            ) == 1;
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }

    @Override
    public User userLogin(String mail, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", mail);
        params.addValue("password", password);

        String query = "SELECT * from " + Constants.TBL_USER + " WHERE email=:email AND password=:password";


        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                    User user = new User();
                    user.setId(rs.getInt(Constants.ID));
                    user.setName(rs.getString(Constants.NAME));
                    user.setAddress(rs.getString(Constants.ADDRESS));
                    user.setEmail(rs.getString(Constants.EMAIL));
                    user.setPhone(rs.getString(Constants.PHONE));
                    user.setRegDate(rs.getString(Constants.REG_DATE));
                    return user;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException("invalid username/password");
        }
    }

    @Override
    public List<User> mailExistence(EntityUser user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());

        String mailValidationQuery = "SELECT * FROM " + Constants.TBL_USER + " WHERE email =:email";


        try {
            return namedParameterJdbcTemplate.query(mailValidationQuery,params, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                    User user = new User();
                    user.setId(rs.getInt(Constants.ID));
                    user.setName(rs.getString(Constants.NAME));
                    user.setAddress(rs.getString(Constants.ADDRESS));
                    user.setEmail(rs.getString(Constants.EMAIL));
                    user.setPhone(rs.getString(Constants.PHONE));
                    user.setRegDate(rs.getString(Constants.REG_DATE));
                    return user;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean addCategory(Category category) {
        String sqlQuery = "INSERT into " + Constants.TBL_CATEGORY + " (name) " + " values(?)";

        return jdbcTemplate.update(sqlQuery, category.getName()) == 1;
    }

    @Override
    public List<Category> getAllCategoryUser() {
        String query = "SELECT * from " + Constants.TBL_CATEGORY + " WHERE status != 'delete'";


        try {
            return jdbcTemplate.query(query, new RowMapper<Category>() {
                @Override
                public Category mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Category category = new Category();
                    category.setId(rs.getInt(Constants.ID));
                    category.setName(rs.getString(Constants.NAME));
                    category.setStatus(rs.getString(Constants.STATUS));
                    category.setDate(rs.getString(Constants.DATE));

                    return category;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException("invalid username/password");
        }
    }

    @Override
    public List<Category> getAllCategoryAdmin() {
        String query = "SELECT * from " + Constants.TBL_CATEGORY;


        try {
            return jdbcTemplate.query(query, new RowMapper<Category>() {
                @Override
                public Category mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Category category = new Category();
                    category.setId(rs.getInt(Constants.ID));
                    category.setName(rs.getString(Constants.NAME));
                    category.setStatus(rs.getString(Constants.STATUS));
                    category.setDate(rs.getString(Constants.DATE));

                    return category;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException("invalid username/password");
        }
    }

    @Override
    public boolean updateCategory(Category category) {


        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET name =? " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getName(), category.getId()) == 1;
    }

    @Override
    public boolean updateCategoryStatus(Category category) {
        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET status =? " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getStatus(), category.getId()) == 1;
    }

    @Override
    public boolean deleteCategory(Category category) {
        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET status ='delete' " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getId()) == 1;
    }

    @Override
    public Admin adminLogin(String email, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        params.addValue("password", password);

        String query = "SELECT * from " + Constants.TBL_ADMIN + " WHERE email=:email AND password=:password";


        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, new RowMapper<Admin>() {
                @Override
                public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Admin admin = new Admin();
                    admin.id = rs.getInt("id");
                    admin.name = rs.getString("name");
                    admin.email = rs.getString("email");

                    return admin;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException("invalid username/password");
        }
    }
}
