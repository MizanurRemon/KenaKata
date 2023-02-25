package org.kenakata.Controller;


import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Model.CommonResponse;
import org.kenakata.Model.User;
import org.kenakata.Service.ApiService;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private ApiService apiService;


    @PostMapping("/")
    public ResponseEntity<?> test() {

        try {
            CommonResponse response = new CommonResponse();
            response.statusCode = HttpStatus.OK.value();
            response.message = Constants.SERVER_RUNNING;
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }


    }

    @PostMapping("/user_registration")
    public ResponseEntity<?> userRegistration(User user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getAddress().isEmpty() || user.getPhone().isEmpty() || user.getPassword().isEmpty()) {
                throw new ApiRequestException("parameter can't be empty");
            } else {
                try {
                    CommonResponse response = new CommonResponse();
                    if (apiService.userRegistration(user)) {
                        response.message = "success";
                        response.statusCode = HttpStatus.OK.value();

                    } else {
                        response.message = "registration failed";
                        response.statusCode = HttpStatus.FORBIDDEN.value();
                    }

                    return ResponseEntity.ok(response);

                } catch (Exception e) {
                    throw new ApiRequestException(e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/get_users")
    public ResponseEntity<?> getAllUsers() {
        try {
            HashMap<String, Object> body = new HashMap<String, Object>();
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllUser());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }
}
