package com.ntou.db.billrecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntou.connections.OkHttpServiceClient;
import com.ntou.tool.Common;
import com.ntou.tool.JsonTool;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@NoArgsConstructor
public class DbApiSenderBillrecord {
    @Value("${jpa.service.url.billrecord}")
    private String dbServiceUrl;

    public List<BillrecordVO> findCusBillAll(OkHttpServiceClient svc, BillrecordVO vo, String startDate, String endDate) throws JsonProcessingException {
        String str = svc.getService(dbServiceUrl
                + "FindCusBillAll?cid=" + vo.getCid() + "&cardType=" + vo.getCardType() + "&startDate=" + startDate + "&endDate=" + endDate);
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        List<BillrecordVO> output = new ObjectMapper().readValue(nodeResult.toString(), new TypeReference<>() {});
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public List<BillrecordVO> FindCusBill(OkHttpServiceClient svc) throws JsonProcessingException {
        String str = svc.getService(dbServiceUrl + "FindCusBill");
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        List<BillrecordVO> output = new ObjectMapper().readValue(nodeResult.toString(), new TypeReference<>() {});
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String insertCusDateBill(OkHttpServiceClient cuscreditSvc, BillrecordVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.postService(dbServiceUrl + "InsertCusDateBill", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeResult = JsonTool.readTree(str);
        String output = nodeResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updateDisputedFlag(OkHttpServiceClient cuscreditSvc, BillrecordVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.putService(dbServiceUrl + "UpdateDisputedFlag", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeResult = JsonTool.readTree(str);
        String output = nodeResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
}
