package org.kenakata.Controller;


import org.kenakata.Handler.ErrorHandler.ApiRequestException;
import org.kenakata.Model.CommonResponse;
import org.kenakata.Utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

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
}
