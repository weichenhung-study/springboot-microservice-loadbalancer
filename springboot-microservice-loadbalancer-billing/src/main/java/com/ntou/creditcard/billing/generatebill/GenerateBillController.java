package com.ntou.creditcard.billing.generatebill;

import com.ntou.creditcard.BaseController;
import com.ntou.db.billofmonth.DbApiSenderBillofmonth;
import com.ntou.db.billrecord.DbApiSenderBillrecord;
import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerateBillController extends BaseController {

    @Autowired
    DbApiSenderBillofmonth dbApiSenderBillofmonth;

    @Autowired
    DbApiSenderBillrecord dbApiSendeBillrecord;

    @Autowired
    DbApiSenderCuscredit dbApiSenderCuscredit;

    public ResponseEntity<GenerateBillRes> doController() throws Exception {
        return new GenerateBill().doAPI(dbApiSenderBillofmonth,dbApiSendeBillrecord,dbApiSenderCuscredit);
    }
}