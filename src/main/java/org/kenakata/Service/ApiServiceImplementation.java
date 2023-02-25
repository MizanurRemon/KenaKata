package org.kenakata.Service;

import org.kenakata.DAO.ApiDao;
import org.kenakata.Helper.Hash.HashingString;
import org.kenakata.Model.User;
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
    public boolean userRegistration(User user) {
        user.setPassword(HashingString.passwordHashing(user.getPassword()));
        //System.out.printf(HashingString.passwordHashing(user.getPassword()));
        return apiDao.userRegistration(user);
    }

    @Override
    public List<User> getAllUser() {
        return apiDao.getAllUser();
    }
}
