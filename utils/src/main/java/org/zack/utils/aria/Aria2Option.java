/**
 * @Project Name:util
 * @File Name:Aria2Option.java
 * @Package Name:com.dls.util.aria
 * @Date:2021年6月21日上午10:30:02 Copyright (c) 2021, Zack All Rights Reserved.
 */

package org.zack.utils.aria;

import com.alibaba.fastjson.JSON;

import java.util.*;

public class Aria2Option {
    private String jsonrpc = "2.0";
    private String id;
    private String method;
    private List<String> params = new ArrayList<>();

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam(int index) {
        return params.get(index);
    }

    public void addParam(String param) {
        params.add(param);
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aria2Option that = (Aria2Option) o;
        return Objects.equals(jsonrpc, that.jsonrpc) &&
                Objects.equals(id, that.id) &&
                Objects.equals(method, that.method) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonrpc, id, method, params);
    }
}
