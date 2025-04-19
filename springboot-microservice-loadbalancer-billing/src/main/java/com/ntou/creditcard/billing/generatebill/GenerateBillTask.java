package com.ntou.creditcard.billing.generatebill;

import com.ntou.tool.Common;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GenerateBillTask {

    @Autowired
    private GenerateBillController generateBillController;
    @Scheduled(cron = "0 0 6 30 * ?") // 每月30號早上六點觸發(秒、分、小時、日期、月份、星期)
    public void executeTask() throws Exception {
        log.info(Common.START_B );
        generateBillController.doController();
        log.info(Common.END_B);
    }
}
