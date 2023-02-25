package org.kenakata.DAO;

import org.kenakata.Handler.ErrorHandler.ApiExceptionHandler;
import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Model.User;
import org.kenakata.Model.UserWithOutPassword;
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
    public List<UserWithOutPassword> getAllUser() {

        String sqlQuery = "SELECT * from tbl_user";
        try {
            return jdbcTemplate.query(sqlQuery, new RowMapper<UserWithOutPassword>() {
                @Override
                public UserWithOutPassword mapRow(ResultSet rs, int rowNum) throws SQLException {

                    UserWithOutPassword user = new UserWithOutPassword();
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
}
