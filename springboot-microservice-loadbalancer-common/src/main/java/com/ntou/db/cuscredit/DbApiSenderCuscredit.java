package com.ntou.db.cuscredit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ntou.connections.OkHttpServiceClient;
import com.ntou.tool.Common;
import com.ntou.tool.JsonTool;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@NoArgsConstructor
public class DbApiSenderCuscredit {
    @Value("${jpa.service.url.cuscredit}")
    private String dbServiceUrl;

    public CuscreditVO getCardHolder(OkHttpServiceClient cuscreditSvc,String cid, String cardType) throws JsonProcessingException {
        String str = cuscreditSvc.getService(dbServiceUrl + "GetCardHolder?cid=" + cid + "&cardType=" + cardType);
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        CuscreditVO output = JsonTool.readValue(nodeResult.toString(),CuscreditVO.class);
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public CuscreditVO getActivatedCardHolder(OkHttpServiceClient cuscreditSvc,String cid, String cardType, String cardNum, String securityCode) throws JsonProcessingException {
        String str = cuscreditSvc.getService(dbServiceUrl + "GetActivatedCardHolder?cid=" + cid + "&cardType=" + cardType + "&cardNum=" + cardNum + "&securityCode=" + securityCode);
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        CuscreditVO output = JsonTool.readValue(nodeResult.toString(),CuscreditVO.class);
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updateActivationRecord(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.putService(dbServiceUrl + "UpdateActivationRecord", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updateCardApprovalStatus(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.putService(dbServiceUrl + "UpdateCardApprovalStatus", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String createCuscredit(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.postService(dbServiceUrl + "CreateCuscredit", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
}
