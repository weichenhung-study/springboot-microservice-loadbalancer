package com.ntou.creditcard.management.application;

import com.ntou.connections.OkHttpServiceClient;
import com.ntou.db.cuscredit.CuscreditVO;
import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import com.ntou.sysintegrat.mailserver.JavaMail;
import com.ntou.sysintegrat.mailserver.MailVO;
import com.ntou.tool.Common;
import com.ntou.tool.ExecutionTimer;
import com.ntou.tool.DateTool;
import com.ntou.tool.ResTool;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** 申請信用卡 */
@Log4j2
public class Application {
    private final OkHttpServiceClient okHttpServiceClient = new OkHttpServiceClient();

    public ResponseEntity<ApplicationRes> doAPI(ApplicationReq req,DbApiSenderCuscredit dbApiSenderCuscredit) throws Exception {
        ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());

		log.info(Common.API_DIVIDER + Common.START_B + Common.API_DIVIDER);
        log.info(Common.REQ + req);
        ApplicationRes res = new ApplicationRes();

        if(!req.checkReq())
            ResTool.regularThrow(res, ApplicationRC.T111A.getCode(), ApplicationRC.T111A.getContent(), req.getErrMsg());
		
		ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        CuscreditVO resCodeGetCardHolder = dbApiSenderCuscredit.getCardHolder(okHttpServiceClient, req.getCid(), req.getCardType());
        if(resCodeGetCardHolder != null)
            ResTool.commonThrow(res, ApplicationRC.T111D.getCode(), ApplicationRC.T111D.getContent());
		
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        String resCode = dbApiSenderCuscredit.createCuscredit(okHttpServiceClient, voCuscreditInsert(req));
        if(!resCode.equals("CreateCuscredit00"))
            ResTool.commonThrow(res, ApplicationRC.T111C.getCode(), ApplicationRC.T111C.getContent());

        sendMail(req);

        ResTool.setRes(res, ApplicationRC.T1110.getCode(), ApplicationRC.T1110.getContent());

        log.info(Common.RES + res);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());
        ExecutionTimer.exportTimings(this.getClass().getSimpleName() + "_" + DateTool.getYYYYmmDDhhMMss() + ".txt");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    private CuscreditVO voCuscreditInsert(ApplicationReq req){
        CuscreditVO vo = new CuscreditVO();
        vo.setChName               (req.getChName               ());
        vo.setEnName               (req.getEnName               ());
        vo.setCid  			       (req.getCid  			    ());
        vo.setCidReissueDate       (req.getCidReissueDate       ());
        vo.setCidReissueLocation   (req.getCidReissueLocation   ());
        vo.setCidReissueStatus     (req.getCidReissueStatus     ());
        vo.setBirthDate            (req.getBirthDate            ());
        vo.setMaritalStatus        (req.getMaritalStatus        ());
        vo.setEducation            (req.getEducation            ());
        vo.setCurrentResidentialAdd(req.getCurrentResidentialAdd());
        vo.setResidentialAdd       (req.getResidentialAdd       ());
        vo.setCellphone            (req.getCellphone            ());
        vo.setEmail                (req.getEmail                ());
        vo.setCompanyName          (req.getCompanyName          ());
        vo.setCompanyIndustry      (req.getCompanyIndustry      ());
        vo.setOccupation           (req.getOccupation           ());
        vo.setDepartment           (req.getDepartment           ());
        vo.setJobTitle             (req.getJobTitle             ());
        vo.setDateOfEmployment     (req.getDateOfEmployment     ());
        vo.setCompanyAddress       (req.getCompanyAddress       ());
        vo.setCompanyPhoneNum      (req.getCompanyPhoneNum      ());
        vo.setAnnualIncome         (req.getAnnualIncome         ());
        vo.setCardApprovalStatus   (CuscreditVO.CardApprovalStatus.PROCESS.getValue());
        vo.setActivationRecord     (CuscreditVO.ActivationRecord.NOTOPEN.getValue());
        vo.setEventCode            (req.getEventCode            ());
        vo.setRegidate 		       (DateTool.getDateTime());
        vo.setIssuing_bank 	       ("803");
        vo.setCardType             (req.getCardType             ());
        vo.setRemark			   (req.getRemark			    ());
        return vo;
    }
    private void sendMail(ApplicationReq req){
        MailVO vo = new MailVO();
        vo.setEmailAddr(req.getEmail());
        vo.setSubject("收到您的信用卡申請");
        vo.setContent("<h1>感謝您申請</h1><h2>因為申請數量踴躍，預計15個工作天審核<br>感謝您的耐心等候</h2>");
        new JavaMail().sendMail(vo);
    }
}
