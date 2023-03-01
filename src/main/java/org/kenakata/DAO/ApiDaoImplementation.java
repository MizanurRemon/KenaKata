package org.kenakata.DAO;

import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Helper.ChangeDateFormat;
import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Admin;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.Product;
import org.kenakata.Model.JsonModel.User;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            return namedParameterJdbcTemplate.query(mailValidationQuery, params, new RowMapper<User>() {
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
    public String checkPreviousPassword(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sqlQuery = "SELECT password FROM " + Constants.TBL_USER + " WHERE id =?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, String.class);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean updatePassword(int id, String password, String previousPassword) {

        String sql = "UPDATE " + Constants.TBL_USER + " SET password=?, previous_password=?" + " WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                password,
                previousPassword, id
        ) == 1;
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

    @Override
    public boolean addProduct(EntityProduct product, MultipartFile file) throws IOException {
        String finalPath = null;
        String ext = "";

        if (!file.getOriginalFilename().isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();

            String storageLocation = "/home/remon/SpringWorkSpace/Storage/KenaKata/";

            String fileName = product.getName() + dateFormat.format(cal.getTime()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            finalPath = storageLocation + fileName;

            ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            file.transferTo(new File(finalPath));
        }

        if (ext.toLowerCase().equals("png") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().isEmpty()) {
            String sqlQuery = "INSERT into " + Constants.TBL_PRODUCT + " (name, category_id,  price, unit, stock, image, status) " + " values(?,?,?,?,?,?,?)";

            return jdbcTemplate.update(sqlQuery, product.getName(), product.getCategory_id(), product.getPrice(), product.getUnit(), product.getStock(), finalPath, product.getStatus()) == 1;

        } else {
            throw new ApiRequestException("invalid image format (supported format: jpg, png, jpeg)");
        }


    }

    @Override
    public List<Product> getAllProductForUser() {

        String query = "SELECT p.id, p.name, p.price, p.unit,p.stock,p.image,p.status,p.date,p.updated_at," +
                " c.id as cid, c.name as cname, c.status as cstatus, c.date as cdate" +
                " FROM tbl_product p" +
                " LEFT JOIN tbl_category c" +
                " ON p.category_id = c.id" +
                " WHERE p.status= 'active' AND c.status = 'active'" + " ORDER BY p.id DESC";
        try {
            return jdbcTemplate.query(query, new RowMapper<Product>() {
                @Override
                public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Product product = new Product();
                    product.id = rs.getInt("id");
                    product.name = rs.getString("name");
                    product.price = rs.getInt("price");
                    product.unit = rs.getString("unit");
                    product.stock = rs.getInt("stock");
                    product.image = rs.getString("image");
                    product.status = rs.getString("status");
                    try {
                        product.date = ChangeDateFormat.ChangeDateFormat(rs.getString("date"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("updated_at"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Product.Category category = new Product.Category();
                    category.id = rs.getInt("cid");
                    category.name = rs.getString("cname");
                    category.status = rs.getString("cstatus");
                    try {
                        category.date = ChangeDateFormat.ChangeDateFormat(rs.getString("cdate"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    product.category = category;
                    return product;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProductForAdmin() {

        String query = "SELECT p.id, p.name, p.price, p.unit,p.stock,p.image,p.status,p.date,p.updated_at," +
                " c.id as cid, c.name as cname, c.status as cstatus, c.date as cdate" +
                " FROM tbl_product p" +
                " LEFT JOIN tbl_category c" +
                " ON p.category_id = c.id" + " ORDER BY id DESC";
        try {
            return jdbcTemplate.query(query, new RowMapper<Product>() {
                @Override
                public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Product product = new Product();
                    product.id = rs.getInt("id");
                    product.name = rs.getString("name");
                    product.price = rs.getInt("price");
                    product.unit = rs.getString("unit");
                    product.stock = rs.getInt("stock");
                    product.image = rs.getString("image");
                    product.status = rs.getString("status");
                    try {
                        product.date = ChangeDateFormat.ChangeDateFormat(rs.getString("date"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("updated_at"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Product.Category category = new Product.Category();
                    category.id = rs.getInt("cid");
                    category.name = rs.getString("cname");
                    category.status = rs.getString("cstatus");
                    try {
                        category.date = ChangeDateFormat.ChangeDateFormat(rs.getString("cdate"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    product.category = category;
                    return product;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean updateProduct(EntityProduct product) {
        String availableQuery = "SELECT COUNT(*) FROM " + Constants.TBL_PRODUCT + " WHERE id =?";
        String sqlQuery = "UPDATE " + Constants.TBL_PRODUCT + " SET name=?,  price=?, unit=?, stock=? " + " WHERE id=?";
        try {
            String count = jdbcTemplate.queryForObject(availableQuery, new Object[]{product.getId()}, String.class);
            System.out.println("id: " + String.valueOf(product.getId()) + " count:: " + count);
            if (Integer.parseInt(count) >= 1) {
                return jdbcTemplate.update(sqlQuery, product.getName(), product.getPrice(), product.getUnit(), product.getStock(), product.getId()) == 1;

            } else {
                throw new ApiRequestException("invalid id");
            }


        } catch (Exception e) {
            throw new ApiRequestException("invalid id");
        }
    }

    @Override
    public boolean updateProductStatus(int id, String status) {
        String availableQuery = "SELECT COUNT(*) FROM " + Constants.TBL_PRODUCT + " WHERE id =?";
        String sqlQuery = "UPDATE " + Constants.TBL_PRODUCT + " SET status=? " + " WHERE id=?";
        try {
            String count = jdbcTemplate.queryForObject(availableQuery, new Object[]{id}, String.class);
            // System.out.println("id: "+String.valueOf(product.getId())+" count:: "+count);
            if (Integer.parseInt(count) >= 1) {
                return jdbcTemplate.update(sqlQuery, status, id) == 1;

            } else {
                throw new ApiRequestException("invalid id");
            }


        } catch (Exception e) {
            throw new ApiRequestException("invalid id");
        }
    }

    @Override
    public boolean updateProductImage(int id, MultipartFile file) {
        String availableQuery = "SELECT COUNT(*) FROM " + Constants.TBL_PRODUCT + " WHERE id =?";
        String sqlQuery = "UPDATE " + Constants.TBL_PRODUCT + " SET image=?" + " WHERE id=?";
        String currentImagePathQuery = "SELECT image FROM " + Constants.TBL_PRODUCT + " WHERE id =?";
        String productNameQuery = "SELECT name FROM " + Constants.TBL_PRODUCT + " WHERE id =?";

        try {
            String count = jdbcTemplate.queryForObject(availableQuery, new Object[]{id}, String.class);
            String currentImagePath = jdbcTemplate.queryForObject(currentImagePathQuery, new Object[]{id}, String.class);
            String productName = jdbcTemplate.queryForObject(productNameQuery, new Object[]{id}, String.class);
            //System.out.println("data:: "+currentImagePath + " " + productName);

            if (Integer.parseInt(count) >= 1) {


                String finalPath = null;
                String ext = "";

                if (!file.getOriginalFilename().isEmpty()) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Calendar cal = Calendar.getInstance();

                    String storageLocation = "/home/remon/SpringWorkSpace/Storage/KenaKata/";

                    String fileName = productName + dateFormat.format(cal.getTime()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

                    finalPath = storageLocation + fileName;

                    ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

                }

                if (ext.toLowerCase().equals("png") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().isEmpty()) {
                    if (currentImagePath != null) {
                        Files.deleteIfExists(Paths.get(currentImagePath));
                    }
                    file.transferTo(new File(finalPath));
                    return jdbcTemplate.update(sqlQuery, finalPath, id) == 1;

                } else {
                    throw new ApiRequestException("invalid image format (supported format: jpg, png, jpeg)");
                }

            } else {
                throw new ApiRequestException("invalid id");
            }


        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


}
