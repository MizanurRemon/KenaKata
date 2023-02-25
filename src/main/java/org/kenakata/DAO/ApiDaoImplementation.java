package org.kenakata.DAO;

import org.kenakata.Handler.ErrorHandler.ApiExceptionHandler;
import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Model.User;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApiDaoImplementation implements ApiDao {
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource source) {
        this.jdbcTemplate = new JdbcTemplate(source);
    }

    @Override
    public boolean userRegistration(User user) {

        String sqlQuery = "INSERT into " + Constants.TBL_USER + " (name,  address, email, phone, password) " + " values(?,?,?,?,?)";

        return jdbcTemplate.update(sqlQuery, user.getName(), user.getAddress(), user.getEmail(), user.getPhone(), user.getPassword()) == 1;
    }

    @Override
    public List<User> getAllUser() {

        String sqlQuery = "SELECT * from tbl_user";
        try {
            return jdbcTemplate.query(sqlQuery, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                    User user = new User(
                            rs.getInt(Constants.ID),
                            rs.getString(Constants.NAME),
                            rs.getString(Constants.ADDRESS),
                            rs.getString(Constants.EMAIL),
                            rs.getString(Constants.PHONE),
                            rs.getString(Constants.PASSWORD),
                            rs.getString(Constants.REG_DATE)
                    );
                    return user;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }
}
