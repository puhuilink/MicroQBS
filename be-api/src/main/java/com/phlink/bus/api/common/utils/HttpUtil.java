package com.phlink.bus.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.phlink.bus.api.common.exception.BusApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

@Slf4j
public class HttpUtil {

    private static final String USER_AGENT = "user-agent";
    private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
    private static final String CONNECTION = "connection";
    private static final String CONNECTION_VALUE = "Keep-Alive";
    private static final String ACCEPT = "accept";
    private static final String UTF8 = "utf-8";
    private static final String ACCEPT_CHARSET = "Accept-Charset";
    private static final String CONTENTTYPE = "contentType";
    private static final String APPLICATIONJSON = "application/json";
    private static final String FORM = "application/x-www-form-urlencoded";
    private static final String SSL = "ssl";
    private static final String POST = "POST";

    protected HttpUtil() {

    }
    public enum contentTypeEnum implements IEnum<String>{
        // application/json
        APPLICATIONJSON("1", "application/json"),
        // application/x-www-form-urlencoded
        XWWWFORMURLENCODED("2", "application/x-www-form-urlencoded");

        contentTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private String code;
        private String desc;

        @Override
        public String getValue() {
            return this.code;
        }

        public String getDesc(){return  this.desc;}
    }
    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) throws IOException {
        String urlNameString = url;
        if (StringUtils.isNotBlank(param))
            urlNameString += "?" + param;
        URL realUrl = new URL(urlNameString);
        URLConnection connection = realUrl.openConnection();
        StringBuilder result = new StringBuilder();
        connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        connection.setRequestProperty(CONNECTION, CONNECTION_VALUE);
        connection.setRequestProperty(ACCEPT, "*/*");
        connection.connect();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("发送GET请求出现异常！", e);
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) throws IOException {
        return sendGet(url, null);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) throws BusApiException {
        StringBuilder result = new StringBuilder();

        String urlNameString = url + "?" + param;
        URLConnection conn;
        try {
            URL realUrl = new URL(urlNameString);
            conn = realUrl.openConnection();
        }catch (Exception e) {
            throw new BusApiException("URL错误");
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty(CONTENTTYPE, UTF8);
        conn.setRequestProperty(ACCEPT_CHARSET, UTF8);
        conn.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        conn.setRequestProperty(CONNECTION, CONNECTION_VALUE);
        conn.setRequestProperty(ACCEPT, "*/*");
        try (PrintWriter out = new PrintWriter(conn.getOutputStream()); BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            out.flush();
            out.print(param);
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！", e);
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url  发送请求的 URL
     * @param json 请求参数，请求参数应该是JSON的字符串形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostRequest(String url, String json) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            //post方式请求
            httpURLConnection.setRequestMethod("POST");
            //设置头部信息
//            httpURLConnection.setRequestProperty("headerdata", "ceshiyongde");
            //一定要设置 Content-Type 要不然服务端接收不到参数
            httpURLConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            //指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
            httpURLConnection.setDoOutput(true);
            //httpURLConnection.setDoInput(true);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时
            //传入参数
            if (Strings.isNotBlank(json)) {
                out = httpURLConnection.getOutputStream();
                out.write(json.getBytes());
                out.flush(); //清空缓冲区,发送数据
                out.close();
            }
            int responseCode = httpURLConnection.getResponseCode();
            //获取请求的资源
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            httpURLConnection.disconnect();
        }
        return result;

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url  发送请求的 URL
     * @param form 请求参数字符串形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostRequest(String url, String form,contentTypeEnum contentType) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            //post方式请求
            httpURLConnection.setRequestMethod("POST");
            //一定要设置 Content-Type 要不然服务端接收不到参数
            httpURLConnection.setRequestProperty("Content-Type", contentType.getDesc());
            //指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
            httpURLConnection.setDoOutput(true);
            //httpURLConnection.setDoInput(true);
            httpURLConnection.setInstanceFollowRedirects(false);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时
            //传入参数
            if (Strings.isNotBlank(form)) {
                out = httpURLConnection.getOutputStream();
                out.write(form.getBytes( StandardCharsets.UTF_8 ));
                out.flush(); //清空缓冲区,发送数据
                out.close();
            }
            int responseCode = httpURLConnection.getResponseCode();
            //获取请求的资源
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            httpURLConnection.disconnect();
        }
        return result;

    }

    /**
     * 向指定 URL 发送DELETE方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendDeleteRequest(String url) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            //post方式请求
            httpURLConnection.setRequestMethod("DELETE");
            //设置头部信息
//            httpURLConnection.setRequestProperty("headerdata", "ceshiyongde");
            //一定要设置 Content-Type 要不然服务端接收不到参数
            httpURLConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            //指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
            httpURLConnection.setDoOutput(true);
            //httpURLConnection.setDoInput(true);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时
            int responseCode = httpURLConnection.getResponseCode();
            //获取请求的资源
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;

    }

    public static void main(String[] args) throws Exception {
        String url = "https://restapi.amap.com/v4/geofence/meta?key=6b81ee5516ef569d00dd0d0d9164529d&gid=b5369804-41bd-4072-9c33-650bf081f516";
/*        String json = "{\n" +
                "\"name\": \"测试围栏名称\",\n" +
                "\"center\": \"115.672126,38.817129\",\n" +
                "\"radius\": \"1000\",\n" +
                "\"enable\": \"true\",\n" +
                "\"repeat\": \"Mon,Tues,Wed,Thur,Fri,Sat,Sun\",\n" +
                "\"time\": \"00:00,11:59;13:00,20:59\",\n" +
                "\"desc\": \"测试围栏描述\",\n" +
                "\"alert_condition\": \"enter;leave\"\n" +
                "}";
        String result = sendPostRequest(url, json);*/
        String result = sendDeleteRequest(url);
        System.out.println(result);
        JSONObject jo = JSONObject.parseObject(result);
        System.out.println(jo.getInteger("errcode"));
        System.out.println(jo.getString("errmsg"));
        System.out.println(jo.getString("gid"));
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            SSLContext sc = SSLContext.getInstance(SSL);
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty(ACCEPT, "*/*");
            conn.setRequestProperty(CONNECTION, CONNECTION_VALUE);
            conn.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            conn.setRequestProperty(ACCEPT_CHARSET, UTF8);
            conn.setRequestProperty(CONTENTTYPE, UTF8);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader indata = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while (ret != null) {
                ret = indata.readLine();
                if (ret != null && !ret.trim().equals("")) {
                    result.append(ret);
                }
            }
            conn.disconnect();
            indata.close();
        } catch (Exception e) {
            log.error("发送SSL POST 请求出现异常！", e);
        }
        return result.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            //trust anything
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            //trust anything
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
