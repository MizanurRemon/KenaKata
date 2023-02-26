package org.kenakata.DAO;

import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.User;

import java.util.List;

public interface ApiDao {

    boolean userRegistration(EntityUser user);

    List<User> getAllUser();

    boolean updateUserProfile(User user);

    User userLogin(String mail, String password);

    boolean mailExistence(EntityUser user);

    boolean addCategory(Category category);

    List<Category> getAllCategoryUser();

    List<Category> getAllCategoryAdmin();

    boolean updateCategory(Category category);

    boolean updateCategoryStatus(Category category);

    boolean deleteCategory(Category category);
}
