package org.kenakata.Controller;


import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Helper.Hash.HashingString;
import org.kenakata.Model.Entity.EntityOrder;
import org.kenakata.Model.Entity.EntityProduct;
import org.kenakata.Model.Entity.EntityCategory;
import org.kenakata.Model.JsonModel.CommonResponse;
import org.kenakata.Model.Entity.EntityUser;
import org.kenakata.Model.JsonModel.Order;
import org.kenakata.Model.JsonModel.User;
import org.kenakata.Service.ApiService;
import org.kenakata.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private ApiService apiService;


    @GetMapping("/")
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

    @GetMapping("/get_users")
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
    public ResponseEntity<?> addCategory(EntityCategory category) {
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

    @GetMapping("/get_all_category_admin")
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
    public ResponseEntity<?> updateCategory(EntityCategory category) {
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
    public ResponseEntity<?> updateCategoryStatus(EntityCategory category) {
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
    public ResponseEntity<?> deleteCategory(EntityCategory category) {
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


    @PostMapping("/update_password")
    public ResponseEntity<?> updatePassword(int id, String password) {

        try {
            if (id == 0 || password.isEmpty()) {
                throw new ApiRequestException("parameter can't be empty");
            } else {

                password = HashingString.passwordHashing(password);
                if (password.equals(apiService.checkPreviousPassword(id))) {
                    throw new ApiRequestException("can't use previous password");
                } else {
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("password", apiService.checkPreviousPassword(id));
//                    return ResponseEntity.ok(map);

                    String pre_password = apiService.checkPreviousPassword(id);

                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    map.put("statusCode", HttpStatus.OK.value());
                    if (apiService.updatePassword(id, password, pre_password)) {
                        map.put("message", "updated successfully");
                    } else {
                        map.put("message", "can't change password");
                    }

                    return ResponseEntity.ok(map);
                }


            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(EntityProduct product, MultipartFile file) throws IOException {

        //apiService.uploadFile(product, file);

        try {
            if (product.getName().isEmpty() || product.getCategory_id() == 0 || product.getPrice().isEmpty() || product.getStock().isEmpty() || product.getStatus().isEmpty() || product.getUnit().isEmpty()) {
                if (product.getCategory_id() == 0) {
                    throw new ApiRequestException("empty category");
                } else {
                    throw new ApiRequestException("empty parameter");
                }
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.addProduct(product, file)) {
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

    @GetMapping("/getProductUser")
    public ResponseEntity<?> getProductForUser() {
        try {
            LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllProductForUser());

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @GetMapping("/getProductAdmin")
    public ResponseEntity<?> getAllProductForAdmin() {
        try {
            LinkedHashMap<String, Object> body = new LinkedHashMap<>(); //hashmap sort its keys, but LinkedHashMap maintain its default order
            body.put("statusCode", HttpStatus.OK.value());
            body.put("data", apiService.getAllProductForAdmin());

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(EntityProduct product) throws IOException {
        //apiService.uploadFile(product, file);

        try {
            if (product.getName().isEmpty() || product.getPrice().isEmpty() || product.getStock().isEmpty() || product.getUnit().isEmpty()) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.updateProduct(product)) {
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

    @PostMapping("/updateProductStatus")
    public ResponseEntity<?> updateProductStatus(int id, String status) throws IOException {


        try {
            if (id == 0 || status.isEmpty()) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.updateProductStatus(id, status)) {
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

    @PostMapping("/updateProductImage")
    public ResponseEntity<?> updateProductImage(int id, MultipartFile file) throws IOException {


        try {
            if (id == 0 || file.isEmpty()) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("statusCode", HttpStatus.OK.value());
                if (apiService.updateProductImage(id, file)) {
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

    @PostMapping("/insertUserOrder")
    public ResponseEntity<?> insertUserOrder(EntityOrder order) {
        try {
            if (order.getUser_id() == 0 || order.getProduct_id() == 0) {
                throw new ApiRequestException("parameter passing error");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put(Constants.STATUS_CODE, HttpStatus.OK.value());
                if (apiService.insertUserOrder(order)) {
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

    @GetMapping("/getAllOrderForAdmin")
    public ResponseEntity<?> getAllOrderForAdmin() {
        try {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put(Constants.STATUS_CODE, HttpStatus.OK.value());
            map.put(Constants.DATA, apiService.getAllOrderForAdmin());

            return ResponseEntity.ok(map);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @GetMapping("/getAllOrderByID")
    public ResponseEntity<?> getAllOrderByID(int id) {
        try {
            if (id == 0) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put(Constants.STATUS_CODE, HttpStatus.OK.value());
                map.put(Constants.DATA, apiService.getAllOrderByID(id));

                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<?> updateOrderStatus(int id, String status) {
        try {

            //System.out.println(String.valueOf(order.id) + " " + order.status);
//            Map<String , Object> param = new HashMap<>();
//            param.put("id", order.id);
//            param.put("status", order.status);

            Order order = new Order();
            order.id = id;
            order.status = status;

            if (order.id == 0 || order.status.isEmpty()) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put(Constants.STATUS_CODE, HttpStatus.OK.value());

                if (apiService.updateOrderStatus(order)) {
                    map.put(Constants.MESSAGE, Constants.SUCCESSFUL);
                } else {
                    map.put(Constants.MESSAGE, Constants.FAILED);
                }

                return ResponseEntity.ok(map);

            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @GetMapping("/getAllOrderByStatus")
    public ResponseEntity<?> getAllOrderByStatus(String status) {
        try {
            if (status.isEmpty()) {
                throw new ApiRequestException("empty parameter");
            } else {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put(Constants.STATUS_CODE, HttpStatus.OK.value());
                map.put(Constants.DATA, apiService.getAllOrderByStatus(status));

                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

}


