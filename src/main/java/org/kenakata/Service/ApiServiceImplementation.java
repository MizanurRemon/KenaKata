package org.kenakata.Service;

import org.kenakata.DAO.ApiDao;
import org.kenakata.Helper.Hash.HashingString;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Admin;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ApiServiceImplementation implements ApiService {

    @Autowired
    private ApiDao apiDao;

    @Override
    public boolean userRegistration(EntityUser user) {
        user.setPassword(HashingString.passwordHashing(user.getPassword()));
        //System.out.printf(HashingString.passwordHashing(user.getPassword()));
        return apiDao.userRegistration(user);
    }

    @Override
    public List<User> getAllUser() {
        return apiDao.getAllUser();
    }

    @Override
    public boolean updateUserProfile(User user) {
        return apiDao.updateUserProfile(user);
    }

    @Override
    public User userLogin(String mail, String password) {
        return apiDao.userLogin(mail, password);
    }

    @Override
    public String checkPreviousPassword(int id) {
        return apiDao.checkPreviousPassword(id);
    }

    @Override
    public boolean updatePassword(int id, String password, String previousPassword) {
        return apiDao.updatePassword(id, password, previousPassword);
    }

    @Override
    public List<User> mailExistence(EntityUser user) {
        return apiDao.mailExistence(user);
    }

    @Override
    public boolean addCategory(Category category) {
        return apiDao.addCategory(category);
    }

    @Override
    public List<Category> getAllCategoryUser() {
        return apiDao.getAllCategoryUser();
    }

    @Override
    public List<Category> getAllCategoryAdmin() {
        return apiDao.getAllCategoryAdmin();
    }

    @Override
    public boolean updateCategory(Category category) {
        return apiDao.updateCategory(category);
    }

    @Override
    public boolean updateCategoryStatus(Category category) {
        return apiDao.updateCategoryStatus(category);
    }

    @Override
    public boolean deleteCategory(Category category) {
        return apiDao.deleteCategory(category);
    }

    @Override
    public Admin adminLogin(String email, String password) {
        return apiDao.adminLogin(email, password);
    }
}
