package org.kenakata.DAO;

import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Helper.ChangeDateFormat;
import org.kenakata.Model.Entity.EntityCategory;
import org.kenakata.Model.Entity.EntityOrder;
import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.*;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.Console;
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
                    try {
                        user.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        user.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
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
                    user.setCreated_at(rs.getString(Constants.CREATED_AT));
                    user.setUpdated_at(rs.getString(Constants.UPDATED_AT));
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
                    user.setCreated_at(rs.getString(Constants.CREATED_AT));
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
    public boolean addCategory(EntityCategory category) {
        String sqlQuery = "INSERT into " + Constants.TBL_CATEGORY + " (name) " + " values(?)";

        return jdbcTemplate.update(sqlQuery, category.getName()) == 1;
    }

    @Override
    public List<Category> getAllCategoryUser() {
        String query = "SELECT * from " + Constants.TBL_CATEGORY + " WHERE status = 'active'";


        try {
            return jdbcTemplate.query(query, new RowMapper<Category>() {
                @Override
                public Category mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Category category = new Category();
                    category.setId(rs.getInt(Constants.ID));
                    category.setName(rs.getString(Constants.NAME));
                    category.setStatus(rs.getString(Constants.STATUS));
                    try {
                        category.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        category.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    return category;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
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
                    try {
                        category.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        category.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    return category;
                }
            });
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean updateCategory(EntityCategory category) {


        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET name =? " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getName(), category.getId()) == 1;
    }

    @Override
    public boolean updateCategoryStatus(EntityCategory category) {
        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET status =? " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getStatus(), category.getId()) == 1;
    }

    @Override
    public boolean deleteCategory(EntityCategory category) {
        String sqlQuery = "UPDATE " + Constants.TBL_CATEGORY + " SET status ='delete' " + "WHERE id=?";

        return jdbcTemplate.update(sqlQuery, category.getId()) == 1;
    }

    @Override
    public Admin adminLogin(String email, String password) {
        System.out.println("password:: " + password);
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
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean addProduct(EntityProduct product, MultipartFile file) throws IOException {

        String sqlQuery = "INSERT into " + Constants.TBL_PRODUCT + " (name, category_id,  price, unit, stock, image) "
                + " values(?,?,?,?,?,?)";

        String finalPath = null;
        String ext = "";

        try {
            if (!file.getOriginalFilename().isEmpty()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar cal = Calendar.getInstance();

                String storageLocation = Constants.STORAGE_LOCATION;

                String fileName = (product.getName() + dateFormat.format(cal.getTime()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)).replace(" ", "");

                finalPath = storageLocation + fileName;

                ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);


            }

            if (ext.toLowerCase().equals("png") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")|| ext.toLowerCase().equals("webp") || ext.toLowerCase().isEmpty()) {

                if (jdbcTemplate.update(sqlQuery, product.getName(), product.getCategory_id(), product.getPrice(), product.getUnit(), product.getStock(), finalPath) == 1) {
                    file.transferTo(new File(finalPath));
                    return true;
                } else {
                    return false;
                }

            } else {
                throw new ApiRequestException("invalid image format (supported format: jpg, png, jpeg)");
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }


    }

    @Override
    public List<Product> getAllProductForUser() {

        String query = "SELECT p.*, c.*" +
                " FROM tbl_product p" +
                " LEFT JOIN tbl_category c" +
                " ON p.category_id = c.id" +
                " WHERE p.status= 'active' AND c.status = 'active'" + " ORDER BY p.id DESC";
        try {
            return jdbcTemplate.query(query, new RowMapper<Product>() {
                @Override
                public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Product product = new Product();
                    product.id = rs.getInt("p.id");
                    product.name = rs.getString("p.name");
                    product.price = rs.getInt("p.price");
                    product.unit = rs.getString("p.unit");
                    product.stock = rs.getInt("p.stock");
                    product.image = rs.getString("p.image");
                    product.status = rs.getString("p.status");
                    try {
                        product.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("created_at"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("updated_at"), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Category category = new Category();
                    category.id = rs.getInt("c.id");
                    category.name = rs.getString("c.name");
                    category.status = rs.getString("c.status");
                    try {
                        category.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        category.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
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

        String query = "SELECT p.*, c.*" +
                " FROM tbl_product p" +
                " LEFT JOIN tbl_category c" +
                " ON p.category_id = c.id" + " ORDER BY p.id DESC";
        try {
            return jdbcTemplate.query(query, new RowMapper<Product>() {
                @Override
                public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Product product = new Product();
                    product.id = rs.getInt("p.id");
                    product.name = rs.getString("p.name");
                    product.price = rs.getInt("p.price");
                    product.unit = rs.getString("p.unit");
                    product.stock = rs.getInt("p.stock");
                    product.image = rs.getString("p.image");
                    product.status = rs.getString("p.status");
                    try {
                        product.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Category category = new Category();
                    category.id = rs.getInt("c.id");
                    category.name = rs.getString("c.name");
                    category.status = rs.getString("c.status");
                    try {
                        category.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        category.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString(Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    //
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
            throw new ApiRequestException(e.getMessage());
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
            throw new ApiRequestException(e.getMessage());
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

                    String storageLocation = Constants.STORAGE_LOCATION;

                    String fileName = (productName + dateFormat.format(cal.getTime()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)).replace(" ", "");
                    System.out.println("filename:: "+fileName);
                    finalPath = storageLocation + fileName;

                    ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

                }

                if (ext.toLowerCase().equals("png") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")|| ext.toLowerCase().equals("webp") || ext.toLowerCase().isEmpty()) {
                    if (currentImagePath != null) {
                        Files.deleteIfExists(Paths.get(currentImagePath));
                    }
                    //file.transferTo(new File(finalPath));
                    // return jdbcTemplate.update(sqlQuery, finalPath, id) == 1;

                    if (jdbcTemplate.update(sqlQuery, finalPath, id) == 1) {
                        file.transferTo(new File(finalPath));
                        return true;
                    } else {
                        return false;
                    }

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

    @Override
    public boolean insertUserOrder(EntityOrder order) {

        String sqlQuery = "INSERT " + Constants.TBL_ORDER
                + " (user_id, product_id)"
                + " values(?, ?)";
        try {
            return jdbcTemplate.update(sqlQuery, order.getUser_id(), order.getProduct_id()) == 1;
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrderForAdmin() {
        String sqlQuery = "SELECT o.*, p.*, c.*, u.* FROM " + Constants.TBL_ORDER + " o" +
                " LEFT JOIN " + Constants.TBL_PRODUCT + " p" +
                " ON p.id = o.product_id" +
                " LEFT JOIN " + Constants.TBL_CATEGORY + " c" +
                " ON p.category_id = c.id" +
                " LEFT JOIN " + Constants.TBL_USER + " u" +
                " ON o.user_id = u.id";
        try {

            return jdbcTemplate.query(sqlQuery, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Order order = new Order();
                    order.id = rs.getInt("o.id");
                    order.status = rs.getString("o.status");

                    try {
                        order.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        order.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    User user = new User();
                    user.setId(rs.getInt("u.id"));
                    user.setName(rs.getString("u.name"));
                    user.setAddress(rs.getString("u.address"));
                    user.setEmail(rs.getString("u.email"));
                    user.setPhone(rs.getString("u.phone"));

                    try {
                        user.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        user.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Product product = new Product();
                    product.id = rs.getInt("p.id");
                    product.name = rs.getString("p.name");
                    product.price = rs.getInt("p.price");
                    product.unit = rs.getString("p.unit");
                    product.stock = rs.getInt("p.stock");
                    product.image = rs.getString("p.image");
                    product.status = rs.getString("p.status");
                    try {
                        product.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Category category = new Category();
                    category.id = rs.getInt("c.id");
                    category.name = rs.getString("c.name");
                    category.status = rs.getString("c.status");

                    try {
                        category.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        category.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    product.category = category;
                    order.user = user;
                    order.product = product;

                    return order;
                }
            });

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrderByID(int id) {
        String sqlQuery = "SELECT o.*, p.*, c.*, u.* FROM " + Constants.TBL_ORDER + " o" +
                " LEFT JOIN " + Constants.TBL_PRODUCT + " p" +
                " ON p.id = o.product_id" +
                " LEFT JOIN " + Constants.TBL_CATEGORY + " c" +
                " ON p.category_id = c.id" +
                " LEFT JOIN " + Constants.TBL_USER + " u" +
                " ON o.user_id = u.id" +
                " WHERE o.user_id = " + id;
        try {

            return jdbcTemplate.query(sqlQuery, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Order order = new Order();
                    order.id = rs.getInt("o.id");
                    order.status = rs.getString("o.status");

                    try {
                        order.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        order.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    User user = new User();
                    user.setId(rs.getInt("u.id"));
                    user.setName(rs.getString("u.name"));
                    user.setAddress(rs.getString("u.address"));
                    user.setEmail(rs.getString("u.email"));
                    user.setPhone(rs.getString("u.phone"));

                    try {
                        user.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        user.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Product product = new Product();
                    product.id = rs.getInt("p.id");
                    product.name = rs.getString("p.name");
                    product.price = rs.getInt("p.price");
                    product.unit = rs.getString("p.unit");
                    product.stock = rs.getInt("p.stock");
                    product.image = rs.getString("p.image");
                    product.status = rs.getString("p.status");
                    try {
                        product.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Category category = new Category();
                    category.id = rs.getInt("c.id");
                    category.name = rs.getString("c.name");
                    category.status = rs.getString("c.status");

                    try {
                        category.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        category.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    product.category = category;
                    order.user = user;
                    order.product = product;

                    return order;
                }
            });

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrderByStatus(String status) {
        String sqlQuery = "SELECT o.*, p.*, c.*, u.* FROM " + Constants.TBL_ORDER + " o" +
                " LEFT JOIN " + Constants.TBL_PRODUCT + " p" +
                " ON p.id = o.product_id" +
                " LEFT JOIN " + Constants.TBL_CATEGORY + " c" +
                " ON p.category_id = c.id" +
                " LEFT JOIN " + Constants.TBL_USER + " u" +
                " ON o.user_id = u.id" +
                " WHERE o.status = " + "'" + status + "'";
        try {

            return jdbcTemplate.query(sqlQuery, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Order order = new Order();
                    order.id = rs.getInt("o.id");
                    order.status = rs.getString("o.status");

                    try {
                        order.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        order.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("o." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    User user = new User();
                    user.setId(rs.getInt("u.id"));
                    user.setName(rs.getString("u.name"));
                    user.setAddress(rs.getString("u.address"));
                    user.setEmail(rs.getString("u.email"));
                    user.setPhone(rs.getString("u.phone"));

                    try {
                        user.setCreated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        user.setUpdated_at(ChangeDateFormat.ChangeDateFormat(rs.getString("u." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Product product = new Product();
                    product.id = rs.getInt("p.id");
                    product.name = rs.getString("p.name");
                    product.price = rs.getInt("p.price");
                    product.unit = rs.getString("p.unit");
                    product.stock = rs.getInt("p.stock");
                    product.image = rs.getString("p.image");
                    product.status = rs.getString("p.status");
                    try {
                        product.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        product.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("p." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Category category = new Category();
                    category.id = rs.getInt("c.id");
                    category.name = rs.getString("c.name");
                    category.status = rs.getString("c.status");

                    try {
                        category.created_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.CREATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        category.updated_at = ChangeDateFormat.ChangeDateFormat(rs.getString("c." + Constants.UPDATED_AT), "yyyy-MM-dd HH:mm:ss", "hh:mm a, dd MMM, yyyy");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    product.category = category;
                    order.user = user;
                    order.product = product;

                    return order;
                }
            });

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public boolean updateOrderStatus(Order order) {

        String sqlQuery = "UPDATE " + Constants.TBL_ORDER + " SET status = ?" +
                " WHERE id = ?";

        try {
            return jdbcTemplate.update(sqlQuery, order.status, order.id) == 1;
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }


}
