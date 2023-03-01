package org.kenakata.DAO;

import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Admin;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.Product;
import org.kenakata.Model.JsonModel.User;
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

    boolean addCategory(Category category);

    List<Category> getAllCategoryUser();

    List<Category> getAllCategoryAdmin();

    boolean updateCategory(Category category);

    boolean updateCategoryStatus(Category category);

    boolean deleteCategory(Category category);

    Admin adminLogin(String email, String password);

    boolean addProduct(EntityProduct product, MultipartFile file) throws IOException;

    List<Product> getAllProductForUser();

    List<Product> getAllProductForAdmin();

    boolean updateProduct(EntityProduct product);

    boolean updateProductStatus(int id, String status);

    boolean updateProductImage(int id, MultipartFile file);
}
