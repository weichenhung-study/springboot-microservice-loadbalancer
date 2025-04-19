package com.ntou.creditcard.management.activation;

import com.ntou.connections.OkHttpServiceClient;
import com.ntou.db.cuscredit.CuscreditVO;
import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import com.ntou.exceptions.TException;
import com.ntou.sysintegrat.mailserver.JavaMail;
import com.ntou.sysintegrat.mailserver.MailVO;
import com.ntou.tool.Common;
import com.ntou.tool.DateTool;
import com.ntou.tool.ExecutionTimer;
import com.ntou.tool.ResTool;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** 信用卡開卡 */
@Log4j2
public class Activation {
    private final OkHttpServiceClient okHttpServiceClient = new OkHttpServiceClient();

    public ResponseEntity<ActivationRes> doAPI(ActivationReq req,DbApiSenderCuscredit dbApiSenderCuscredit) throws Exception {
        ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());

		log.info(Common.API_DIVIDER + Common.START_B + Common.API_DIVIDER);
        log.info(Common.REQ + req);
        ActivationRes res = new ActivationRes();

        if(!req.checkReq())
            ResTool.regularThrow(res, ActivationRC.T131A.getCode(), ActivationRC.T131A.getContent(), req.getErrMsg());
		
		ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        CuscreditVO voCuscredit = dbApiSenderCuscredit.getCardHolder(okHttpServiceClient, req.getCid(), req.getCardType());
        String resCode = dbApiSenderCuscredit.updateActivationRecord(okHttpServiceClient,voCuscreditUpdate(req));
        ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());

        MailVO vo = new MailVO();
        if(!resCode.equals("UpdateActivationRecord00")) {
            ResTool.setRes(res, ActivationRC.T131C.getCode(), ActivationRC.T131C.getContent());

            vo.setEmailAddr(voCuscredit.getEmail());
            vo.setSubject("信用卡開卡開卡失敗");
            vo.setContent("<h1>請聯繫客服</h1><h2>02-1234567</h2>");
            throw new TException(res);
        }
        vo.setEmailAddr(voCuscredit.getEmail());
        vo.setSubject("信用卡開卡完成");
        vo.setContent("<h1>您申請的信用卡已開卡完成</h1><h2>歡迎使用!</h2>");
        new JavaMail().sendMail(vo);

        ResTool.setRes(res, ActivationRC.T1310.getCode(), ActivationRC.T1310.getContent());

        log.info(Common.RES + res);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());
        ExecutionTimer.exportTimings(this.getClass().getSimpleName() + "_" + DateTool.getYYYYmmDDhhMMss() + ".txt");
		return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    private CuscreditVO voCuscreditUpdate(ActivationReq req){
        CuscreditVO vo = new CuscreditVO();
        vo.setCid  		(req.getCid  	 ());
        vo.setCardType  (req.getCardType ());
        vo.setActivationRecord(CuscreditVO.ActivationRecord.OPEN.getValue());
        return vo;
    }
}
