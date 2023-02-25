package org.kenakata.Service;

import org.kenakata.Model.User;
import org.kenakata.Model.UserWithOutPassword;

import java.util.List;

public interface ApiService {
    boolean userRegistration(User user);

    List<UserWithOutPassword> getAllUser();
}
