package com.ntou.creditcard.management.review;

import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
    @Autowired
    DbApiSenderCuscredit dbApiSenderCuscredit;

    @PutMapping("/Review")
    public ResponseEntity<ReviewRes> doController(@RequestBody ReviewReq req) throws Exception {
        return new Review().doAPI(req,dbApiSenderCuscredit);
    }
}