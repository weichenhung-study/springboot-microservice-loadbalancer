package com.ntou.creditcard.billing.feepayment;

import com.ntou.db.billofmonth.DbApiSenderBillofmonth;
import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeePaymentController {

    @Autowired
    DbApiSenderBillofmonth dbApiSenderBillofmonth;

    @Autowired
    DbApiSenderCuscredit dbApiSenderCuscredit;

    @PostMapping("/FeePayment")
    public ResponseEntity<FeePaymentRes> doController(@RequestBody FeePaymentReq req) throws Exception {
        return new FeePayment().doAPI(req,dbApiSenderBillofmonth,dbApiSenderCuscredit);
    }
}