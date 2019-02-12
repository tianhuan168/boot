package utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

/**
 * 连同StatusCode一并返回，区别于@HttpClientUtil
 */
public class HttpClientUtil2 {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil2.class);

    private static final int DEFAULT_TIME_OUT = 5 * 1000;

    public static Response put(String url, Map<String, String> data) {
        boolean useSSL = false;
        if (StringUtils.startsWithIgnoreCase(url, "https")) {
            useSSL = true;
        }
        return doPut(url, data, null, DEFAULT_TIME_OUT, useSSL);
    }


    private static Response doPut(String url, Map<String, String> data, Map<String, String> header, int connectionTimeOut, boolean ssl) {
        CloseableHttpClient httpClient;
        if (ssl) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }

        connectionTimeOut = connectionTimeOut < 0 ? DEFAULT_TIME_OUT : connectionTimeOut;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .build();

        HttpPut httpPut = new HttpPut(url);

        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Connection", "close");

        if (CollectionUtils.size(header) > 0) {
            for (String key : header.keySet()) {
                httpPut.setHeader(key, header.get(key));
            }
        }

        if (data != null) {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (String key : data.keySet()) {
                formParams.add(new BasicNameValuePair(key, data.get(key)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpPut.setEntity(entity);
        }

        Response rsp = new Response();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPut);
            if (response != null) {
                if (response.getEntity() != null) {
                    rsp.setEntity(EntityUtils.toString(response.getEntity(), "utf-8"));
                    EntityUtils.consume(response.getEntity());
                }
                rsp.setStatus(response.getStatusLine().getStatusCode());
                rsp.setStatusPhrase(response.getStatusLine().getReasonPhrase());
                logger.info("【Put】url:{} header:{} body:{} result:{}",
                        new Object[]{url, header, data, response.getStatusLine().toString() + " " + rsp.getEntity()});
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return rsp;
    }


    /**
     * 发起GET请求，默认header，默认超时
     */
    public static Response get(String url) {
        return get(url, null, DEFAULT_TIME_OUT);
    }

    /**
     * 发起GET请求，默认超时
     */
    public static Response get(String url, Map<String, String> header) {
        return get(url, header, DEFAULT_TIME_OUT);
    }

    public static Response getByParam(String url, Map<String, Object> params) {
        return get(url, null, DEFAULT_TIME_OUT, params);
    }

    /**
     * 发起GET请求
     */
    public static Response get(String url, Map<String, String> header, int connectionTimeOut) {
        return get(url, header, DEFAULT_TIME_OUT, null);
    }

    /**
     * get请求，connectionTimeOut传-1，表示使用默认超时时间
     */
    public static Response get(String url, Map<String, String> header, int connectionTimeOut, Map<String, Object> params) {
        CloseableHttpClient httpClient;
        boolean useSSL = StringUtils.startsWithIgnoreCase(url, "https");
        if (useSSL) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }
        connectionTimeOut = connectionTimeOut < 0 ? DEFAULT_TIME_OUT : connectionTimeOut;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .build();

        if (params != null && params.size() > 0) {
            boolean first = !StringUtils.containsIgnoreCase(url, "?");
            for (String key : params.keySet()) {
                Object v = params.get(key);
                if (v instanceof Collection && CollectionUtils.size(v) > 0) {
                    url += buildQry(url, key, (Collection) v);
                    first = false;
                } else {
                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }
                    url += (key + "=" + encode(String.valueOf(v)));
                }
            }
        }

        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Connection", "close");
        httpget.setConfig(requestConfig);

        if (header != null) {
            for (String key : header.keySet()) {
                httpget.setHeader(key, header.get(key));
            }
        }
        Response rsp = new Response();
        String result = "";
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
            if (response != null) {
                if (response.getEntity() != null) {
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    rsp.setEntity(result);
                    EntityUtils.consume(response.getEntity());
                }
                rsp.setStatusPhrase(response.getStatusLine().getReasonPhrase());
                rsp.setStatus(response.getStatusLine().getStatusCode());
                logger.info("【Get】 url:{} result:{} entity:{}", url, response.getStatusLine().toString(), rsp.getEntity());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            if (response != null) {
                try {
                    response.close();
                } catch (Exception ex) {
                    logger.error(e.getMessage(), ex);
                }
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return rsp;
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            logger.info("fail url_encode '{}'");
            return str;
        }
    }

    private static String buildQry(String url, String key, Collection c) {
        boolean first = !StringUtils.containsIgnoreCase(url, "?");

        String qry = "";
        for (Object o : c) {
            if (first) {
                qry += "?";
                first = false;
            } else {
                qry += "&";
            }

            qry += (key + "[]=" + encode(String.valueOf(o)));
        }

        return qry;
    }


    /**
     * 发起POST请求 默认超时 默认header
     */
    public static Response post(String url, Map<String, String> data, boolean parseHeaders) {
        return post(url, data, null, parseHeaders);
    }

    /**
     * 发起POST请求 默认超时
     */
    public static Response post(String url, Map<String, String> data, Map<String, String> header, boolean parseHeaders) {
        return post(url, data, header, DEFAULT_TIME_OUT, parseHeaders);
    }

    /**
     * 发起POST请求
     */
    public static Response post(String url, Map<String, String> data, Map<String, String> header, int connectionTimeOut, boolean parseHeaders) {
        return doPost(url, data, header, connectionTimeOut, parseHeaders, null);
    }

    public static Response doPost(String url, Map<String, String> data, Map<String, String> header, int connectionTimeOut, boolean parseHeaders, Map<String, String> cookieMap) {
        CloseableHttpClient httpClient;
        boolean useSSL = StringUtils.startsWithIgnoreCase(url, "https");
        if (useSSL) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }

        connectionTimeOut = connectionTimeOut < 0 ? DEFAULT_TIME_OUT : connectionTimeOut;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setConfig(requestConfig);

        if (CollectionUtils.size(header) > 0) {
            for (String key : header.keySet()) {
                httpPost.setHeader(key, header.get(key));
            }
        }

        if (CollectionUtils.size(cookieMap)>0) {
            httpPost.setHeader(new BasicHeader("Cookie", buildCookie(cookieMap)));
        }

        if (CollectionUtils.size(data) > 0) {
            List<NameValuePair> formParams = Lists.newArrayList();
            for (String key : data.keySet()) {
                formParams.add(new BasicNameValuePair(key, data.get(key)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpPost.setEntity(entity);
        }

        String result = "";
        Response rsp = new Response();;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response != null) {
                if (response.getEntity() != null) {
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    rsp.setEntity(result);
                    EntityUtils.consume(response.getEntity());
                }
                rsp.setStatusPhrase(response.getStatusLine().getReasonPhrase());
                rsp.setStatus(response.getStatusLine().getStatusCode());
                if (parseHeaders) {
                    Header[] allHeaders = response.getAllHeaders();
                    ArrayListMultimap<String, String> headerMap = ArrayListMultimap.create();
                    for (Header h : allHeaders) {
                        headerMap.put(h.getName(), h.getValue());
                    }

                    rsp.setHeaders(headerMap.asMap());
                }

                logger.info("【Post】url:{} header:{} body:{} result:{} result_header:{}",
                        new Object[]{url, header, data, response.getStatusLine().toString() + " " + result, rsp.getHeaders()});
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    httpPost.releaseConnection();
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return rsp;
    }

    private static CloseableHttpClient createSSLClientDefault() {
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (KeyStoreException e) {
            logger.error(e.getMessage(), e);
        }
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(
                    builder.build());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (KeyManagementException e) {
            logger.error(e.getMessage(), e);
        }
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                sslsf).build();
        return httpclient;
    }

    public static Response delete(String url, Map<String, String> data) {
        return doDelete(url, data, null, DEFAULT_TIME_OUT);
    }

    public static Response doDelete(String url, Map<String, String> params, Map<String, String> header, int connectionTimeOut) {
        CloseableHttpClient httpClient;
        boolean useSSL = StringUtils.startsWithIgnoreCase(url, "https");
        if (useSSL) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }
        connectionTimeOut = connectionTimeOut < 0 ? DEFAULT_TIME_OUT : connectionTimeOut;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .build();

        if (params != null && params.size() > 0) {
            boolean first = !StringUtils.containsIgnoreCase(url, "?");
            for (String key : params.keySet()) {
                Object v = params.get(key);
                if (v instanceof Collection && CollectionUtils.size(v) > 0) {
                    url += buildQry(url, key, (Collection) v);
                    first = false;
                } else {
                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }
                    url += (key + "=" + encode(String.valueOf(v)));
                }
            }
        }

        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Connection", "close");
        httpDelete.setConfig(requestConfig);
        if (header != null) {
            for (String key : header.keySet()) {
                httpDelete.setHeader(key, header.get(key));
            }
        }
        Response rsp = new Response();
        String result;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpDelete);
            if (response != null) {
                if (response.getEntity() != null) {
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    rsp.setEntity(result);
                    EntityUtils.consume(response.getEntity());
                }
                rsp.setStatusPhrase(response.getStatusLine().getReasonPhrase());
                rsp.setStatus(response.getStatusLine().getStatusCode());
                logger.info("【Delete】 url:{}  result:{}", url, response.getStatusLine().toString());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            if (response != null) {
                try {
                    response.close();
                } catch (Exception ex) {
                    logger.error(e.getMessage(), ex);
                }
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return rsp;
    }

    /**
     * 主要用于探查资源是否存在
     */
    public static Response head(String url) {
        return doHead(url, DEFAULT_TIME_OUT);
    }

    public static Response doHead(String url, int connectionTimeOut) {
        CloseableHttpClient httpClient;
        boolean useSSL = StringUtils.startsWithIgnoreCase(url, "https");
        if (useSSL) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }

        connectionTimeOut = connectionTimeOut < 0 ? DEFAULT_TIME_OUT : connectionTimeOut;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .build();

        HttpHead httpHead = new HttpHead(url);
        httpHead.setHeader("Connection", "close");

        String result = "";
        Response rsp = new Response();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
            rsp = new Response();
            if (response != null) {
                if (response.getEntity() != null) {
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    rsp.setEntity(result);
                    EntityUtils.consume(response.getEntity());
                }
                rsp.setStatusPhrase(response.getStatusLine().getReasonPhrase());
                rsp.setStatus(response.getStatusLine().getStatusCode());

                logger.info("【HEAD】url:{} header:{} body:{} result:{}",
                        new Object[]{url, "", "", response.getStatusLine().toString() + " " + result});
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    httpHead.releaseConnection();
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return rsp;
    }

    public static String buildCookie(Map<String, String> cookieMap) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (CollectionUtils.size(cookieMap) > 0) {
            for (String key : cookieMap.keySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(";");
                }

                sb.append(key);
                sb.append("=");
                sb.append(cookieMap.get(key));
            }
        }

        return sb.toString();
    }
}
