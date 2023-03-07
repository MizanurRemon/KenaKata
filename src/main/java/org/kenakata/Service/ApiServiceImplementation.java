package org.kenakata.Service;

import org.kenakata.DAO.ApiDao;
import org.kenakata.Helper.Hash.HashingString;
import org.kenakata.Model.Entity.EntityOrder;
import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Override
    public boolean addProduct(EntityProduct product, MultipartFile file) throws IOException {
        return apiDao.addProduct(product, file);
    }

    @Override
    public List<Product> getAllProductForUser() {
        return apiDao.getAllProductForUser();
    }

    @Override
    public List<Product> getAllProductForAdmin() {
        return apiDao.getAllProductForAdmin();
    }

    @Override
    public boolean updateProduct(EntityProduct product) {
        return apiDao.updateProduct(product);
    }

    @Override
    public boolean updateProductStatus(int id, String status) {
        return apiDao.updateProductStatus(id, status);
    }

    @Override
    public boolean updateProductImage(int id, MultipartFile file) {
        return apiDao.updateProductImage(id, file);
    }

    @Override
    public boolean insertUserOrder(EntityOrder order) {
        return apiDao.insertUserOrder(order);
    }

    @Override
    public List<Order> getAllOrderForAdmin() {
        return apiDao.getAllOrderForAdmin();
    }

    @Override
    public List<Order> getAllOrderByID(int id) {
        return apiDao.getAllOrderByID(id);
    }

    @Override
    public boolean updateOrderStatus(Order order) {
        return apiDao.updateOrderStatus(order);
    }

    @Override
    public List<Order> getAllOrderByStatus(String status) {
        return apiDao.getAllOrderByStatus(status);
    }
}
