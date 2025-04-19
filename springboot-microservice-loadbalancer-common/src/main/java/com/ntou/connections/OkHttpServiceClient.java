package com.ntou.connections;

import com.ntou.tool.Common;
import com.ntou.tool.JsonTool;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;

import java.io.IOException;

@Log4j2
public class OkHttpServiceClient {

    private final OkHttpClient client = new OkHttpClient();

    public String getService(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        log.info("url:{}", url);
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
            else {
                return "Failed to call service: " + response.code();
            }
        } catch (IOException e) {
            log.error(Common.EXCEPTION, e);
            return "Exception occurred: " + e.getMessage();
        }
    }
    public String postService(String url, Object requestBody) {
        log.info("url:{}", url);
        log.info("requestBody:{}", requestBody);
        try {
            String json = JsonTool.format2Json(requestBody);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    return response.body().string();
                }
                else {
                    return "Failed to call service: " + response.code();
                }
            }
        } catch (IOException e) {
            log.error(Common.EXCEPTION, e);
            return "Exception occurred: " + e.getMessage();
        }
    }
    public String putService(String url, Object requestBody) {
        log.info("url:{}", url);
        log.info("requestBody:{}", requestBody);
        try {
            String json = JsonTool.format2Json(requestBody);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    return response.body().string();
                }
                else {
                    return "Failed to call service: " + response.code();
                }
            }
        } catch (IOException e) {
            log.error(Common.EXCEPTION, e);
            return "Exception occurred: " + e.getMessage();
        }
    }
}
