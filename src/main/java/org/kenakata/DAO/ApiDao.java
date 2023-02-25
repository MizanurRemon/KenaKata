package org.kenakata.DAO;

import org.kenakata.Model.User;
import org.kenakata.Model.UserWithOutPassword;

import java.util.List;

public interface ApiDao {

    boolean userRegistration(User user);

    List<UserWithOutPassword> getAllUser();
}
