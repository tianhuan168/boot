package utils;

import org.apache.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

/**
 * Created by quxiao on 2015/5/31.
 */
public class Response {
    Integer status;   // 返回码
    String statusPhrase;  // 返回状态行
    String entity;  // 返回值
    Map<String, Collection<String>> headers;   // headers


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusPhrase() {
        return statusPhrase;
    }

    public void setStatusPhrase(String statusPhrase) {
        this.statusPhrase = statusPhrase;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public boolean isSucProc() {
        return status != null && (status >= HttpStatus.SC_OK && status <= HttpStatus.SC_MULTI_STATUS);
    }

    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    public String getHeaderValue(String headerName) {
        if (headers!=null) {
            if (headers.containsKey(headerName)) {
                for (String s : headers.get(headerName)) {
                    return s;
                }
            }
        }

        return null;
    }
}
