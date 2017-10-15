/*
Salman Mujtaba: 800969897
Sharan Girdhani: 800960333
RequestParams
 */
// In Class 05
// ContactListActivity.java
// Sharan Girdhani     - 800960333
// Salman Mujtaba   - 800969897
//

package com.example.sharangirdhani.homework05;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

public class RequestParams implements Serializable{
    String baseUrl, method;
    LinkedHashMap<String, String> params = new LinkedHashMap<>();

    public RequestParams(String method, String baseUrl) {
        this.baseUrl = baseUrl;
        this.method = method;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();

        for (String key : params.keySet()) {
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if(sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getEncodedUrl() {
        return this.baseUrl + "?" + getEncodedParams();
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "baseUrl='" + baseUrl + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
