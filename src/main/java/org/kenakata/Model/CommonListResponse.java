package org.kenakata.Model;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class CommonListResponse {
    public int statusCode;
    public List<User> data;
}
