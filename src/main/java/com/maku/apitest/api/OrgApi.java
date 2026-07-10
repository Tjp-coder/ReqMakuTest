package com.maku.apitest.api;

import com.maku.apitest.client.RequestSpecFactory;
import io.restassured.response.Response;

public class OrgApi extends BaseApi {

    public OrgApi(RequestSpecFactory factory) {
        super(factory);
    }

    public Response list(String token){
        return req(token)
                .when()
                .get("/sys/org/list");
    }

    public Response getById(String token, Long id) {
        return req(token)
                .when()
                .get("/sys/org/{id}", id);
    }

    public Response delete(String token, Long id) {
        return req(token)
                .when()
                .delete("/sys/org/{id}", id);
    }

    public Response save(String token,String body) {
        return req(token)
                .body(body)
                .when()
                .post("/sys/org");
    }

    public Response update(String token,String body) {
        return req(token)
                .body(body)
                .when()
                .put("/sys/org");
    }
}
