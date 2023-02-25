package org.kenakata.DAO;

import org.kenakata.Model.User;

import java.util.List;

public interface ApiDao {

    boolean userRegistration(User user);

    List<User> getAllUser();
}
