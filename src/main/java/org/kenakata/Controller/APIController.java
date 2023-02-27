package org.kenakata.Controller;


import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Helper.Hash.HashingString;
import org.kenakata.Model.JsonModel.Category;
import org.kenakata.Model.JsonModel.CommonResponse;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.User;
import org.kenakata.Service.ApiService;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

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

    @PostMapping("/admin_login")
    public ResponseEntity<?> adminLogin(String email, String password) {
        try {
            if (email.isEmpty() || password.isEmpty()) {
                throw new ApiRequestException("empty login credentials");
            } else {
                LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
                body.put("statusCode", HttpStatus.OK.value());
                body.put("message", Constants.LOGIN_SUCCESSFUL);
                body.put("data", apiService.adminLogin(email, password));
                return ResponseEntity.ok(body);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/user_registration")
    public ResponseEntity<?> userRegistration(EntityUser user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getAddress().isEmpty() || user.getPhone().isEmpty() || user.getPassword().isEmpty()) {
                throw new ApiRequestException("parameter can't be empty");
            } else {
                try {

                    if (apiService.mailExistence(user).size() >= 1) {
                        throw new ApiRequestException("already registered with this email");
                    } else {

                        // throw new ApiRequestException("nothing with this email");
                        CommonResponse response = new CommonResponse();
                        if (apiService.userRegistration(user)) {
                            response.message = "registration successful";
                            response.statusCode = HttpStatus.OK.value();

                        } else {
                            response.message = "registration failed";
                            response.statusCode = HttpStatus.FORBIDDEN.value();
                        }

                        return ResponseEntity.ok(response);
                    }

                } catch (Exception e) {
                    throw new ApiRequestException(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/update_user_profile")
    public ResponseEntity<?> updateUserProfile(User user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getAddress().isEmpty() || user.getPhone().isEmpty() || user.getId() == 0) {
                throw new ApiRequestException("parameter can't be empty");
            } else {
                try {

                    CommonResponse response = new CommonResponse();
                    if (apiService.updateUserProfile(user)) {
                        response.message = "successfully updated";
                        response.statusCode = HttpStatus.OK.value();

                    } else {
                        response.message = "update failed";
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
            LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllUser());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/user_login")
    public ResponseEntity<?> userLogin(EntityUser user) {
        try {
            if (user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                throw new ApiRequestException("empty login credentials");
            } else {
                LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
                body.put("statusCode", HttpStatus.OK.value());
                body.put("message", Constants.LOGIN_SUCCESSFUL);
                body.put("data", apiService.userLogin(user.getEmail(), HashingString.passwordHashing(user.getPassword())));
                return ResponseEntity.ok(body);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/add_category")
    public ResponseEntity<?> addCategory(Category category) {
        try {
            if (category.getName().isEmpty()) {
                throw new ApiRequestException("category name missing");
            } else {
                LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
                body.put("statusCode", HttpStatus.OK.value());
                if (apiService.addCategory(category)) {
                    body.put("message", Constants.SUCCESSFUL);
                } else {
                    body.put("message", Constants.FAILED);
                }

                return ResponseEntity.ok(body);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }

    @PostMapping("/get_all_category_user")
    public ResponseEntity<?> getAllCategoryUser() {
        try {
            LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllCategoryUser());

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/get_all_category_admin")
    public ResponseEntity<?> getAllCategoryAdmin() {
        try {
            LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllCategoryAdmin());

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/update_category")
    public ResponseEntity<?> updateCategory(Category category) {
        try {
            if (category.getId() == 0 || category.getName().isEmpty()) {
                throw new ApiRequestException("parameter value missing");
            } else {

                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.updateCategory(category)) {
                    map.put("message", Constants.SUCCESSFUL);
                } else {
                    map.put("message", Constants.FAILED);
                }

                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/update_category_status")
    public ResponseEntity<?> updateCategoryStatus(Category category) {
        try {
            if (category.getId() == 0 || category.getStatus().isEmpty()) {
                throw new ApiRequestException("parameter value missing");
            } else {

                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.updateCategoryStatus(category)) {
                    map.put("message", Constants.SUCCESSFUL);
                } else {
                    map.put("message", Constants.FAILED);
                }

                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/delete_category")
    public ResponseEntity<?> deleteCategory(Category category) {
        try {
            if (category.getId() == 0) {
                throw new ApiRequestException("parameter value missing");
            } else {

                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.deleteCategory(category)) {
                    map.put("message", Constants.SUCCESSFUL);
                } else {
                    map.put("message", Constants.FAILED);
                }

                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


    @PostMapping("/mail_validation")
    public ResponseEntity<?> checkMail(EntityUser user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getAddress().isEmpty() || user.getPhone().isEmpty() || user.getPassword().isEmpty()) {
                throw new ApiRequestException("parameter can't be empty");
            } else {
                try {

                    if (apiService.mailExistence(user).size() >= 1) {
                        throw new ApiRequestException("already registered with this email");
                    } else {
                        //return ResponseEntity.ok(apiService.mailExistence(user));
                        throw new ApiRequestException("nothing with this email");
                    }

                } catch (Exception e) {
                    throw new ApiRequestException(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }


}


