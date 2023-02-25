package org.kenakata.Service;

import org.kenakata.Model.User;

import java.util.List;

public interface ApiService {
    boolean userRegistration(User user);

    List<User> getAllUser();
}
