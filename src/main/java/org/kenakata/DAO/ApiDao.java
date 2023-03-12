package org.kenakata.DAO;

import org.kenakata.Model.Entity.EntityCategory;
import org.kenakata.Model.Entity.EntityOrder;
import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ApiDao {

    boolean userRegistration(EntityUser user);

    List<User> getAllUser();

    boolean updateUserProfile(User user);

    User userLogin(String mail, String password);

    List<User> mailExistence(EntityUser user);

    String checkPreviousPassword(int id);

    boolean updatePassword(int id, String password, String previousPassword);

    boolean addCategory(EntityCategory category);

    List<Category> getAllCategoryUser();

    List<Category> getAllCategoryAdmin();

    boolean updateCategory(EntityCategory category);

    boolean updateCategoryStatus(EntityCategory category);

    boolean deleteCategory(EntityCategory category);

    Admin adminLogin(String email, String password);

    boolean addProduct(EntityProduct product, MultipartFile file) throws IOException;

    List<Product> getAllProductForUser();

    List<Product> getAllProductForAdmin();

    boolean updateProduct(EntityProduct product);

    boolean updateProductStatus(int id, String status);

    boolean updateProductImage(int id, MultipartFile file);

    boolean insertUserOrder(EntityOrder order);

    List<Order> getAllOrderForAdmin();

    List<Order> getAllOrderByID(int id);

    List<Order> getAllOrderByStatus(String status);

    boolean updateOrderStatus(Order order);
}
