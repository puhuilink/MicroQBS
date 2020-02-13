package com.phlink.bus.message.service;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

//如果JDK版本是1.8,可使用原生Base64类

public class SendSms {
	
    public String url="https://rtcsms.cn-north-1.myhuaweicloud.com:10743/sms/batchSendSms/v1";
    
    public String appKey="p73P9225t9fMV36DE4G1b5lWPDz4";
    
    public String appSecret="VgIP8Eq244oA6psi30e604cwuUX3";
    
    //验证码通道
    public String senderCode="8820010835111";
    //通知通道
    public String senderNotify="8820010721815";
    
    //组册、登录、更换手机号的验证码模板
    public String newcode="12eeb03b391b487181468646f19bfa1c";
    
    //添加主责任人
    public String newBindGuardian="fa20ffdead8f4624b9765450e99d0e5a";
    
    //添加共同监护人模板
    public String newAddGuardian="0a5fdb8ddcef478db44341de25df1a1d";
 
    public String signature="中益安达";
    
    private String wsseHeader="";
    
    private String requestBody="";
    
    private String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    
    private String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";
    
    private String statusCallBack="";
    
	public SendSms(){
		wsseHeader=buildWsseHeader(appKey, appSecret);
	}
	
	public Boolean Send(String msgType,String receiver,String templateParams){
		if (null == wsseHeader || wsseHeader.isEmpty()) {
            return false;
        }
		String templateId="";
		switch(msgType){
		case "newCode":
 		   templateId = newcode;
 		   break;
 	   case "bindGuardian":
 		  templateId = newBindGuardian;
 		   break;
 	   case "addGuardian":
 		  templateId = newAddGuardian;
 		   break;
		   default:
			   break;
		}
		if(templateId!=null&&templateId.length()>0){
			if(msgType=="newCode"){    //验证码
				requestBody = buildRequestBody(senderCode, receiver, templateId, templateParams, statusCallBack, signature);
			}
			else{        //通知
				requestBody = buildRequestBody(senderNotify, receiver, templateId, templateParams, statusCallBack, signature);
			}
			if (null == requestBody || requestBody.isEmpty()) {
	            return false;
	        }
			Writer out = null;
	        BufferedReader in = null;
	        StringBuffer result = new StringBuffer();
	        HttpsURLConnection connection = null;
	        InputStream is = null;

	        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
	        HostnameVerifier hv = new HostnameVerifier() {
	            @Override
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        try{
	        	trustAllHttpsCertificates();
	        	URL realUrl = new URL(url);
	            connection = (HttpsURLConnection) realUrl.openConnection();

	            connection.setHostnameVerifier(hv);
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setUseCaches(true);
	            //请求方法
	            connection.setRequestMethod("POST");
	            //请求Headers参数
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setRequestProperty("Authorization", AUTH_HEADER_VALUE);
	            connection.setRequestProperty("X-WSSE", wsseHeader);
	            connection.connect();
	            out = new OutputStreamWriter(connection.getOutputStream());
	            out.write(requestBody); //发送请求Body参数
	            out.flush();
	            out.close();
	            
	            int status = connection.getResponseCode();
	            if (200 == status) { //200
	                is = connection.getInputStream();
	                return true;
	            } else { //400/401
	                is = connection.getErrorStream();
	                in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		            String line = "";
		            while ((line = in.readLine()) != null) {
		                result.append(line);
		            }
		            System.out.println(result.toString()); //打印响应消息实体
	                return false;
	            }
	        }
	        catch(Exception ex){
	        	return false;
	        }
	        finally{
	        	try {
	                if (null != out) {
	                    out.close();
	                }
	                if (null != is) {
	                    is.close();
	                }
	                if (null != in) {
	                    in.close();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		}else{
			return false;
		}
	}
	
	/**
     * 构造请求Body体
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    private String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
            String statusCallBack, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();

        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        if (null != templateParas && !templateParas.isEmpty()) {
            map.put("templateParas", templateParas);
        }
        if (null != statusCallBack && !statusCallBack.isEmpty()) {
            map.put("statusCallback", statusCallBack);
        }
        if (null != signature && !signature.isEmpty()) {
            map.put("signature", signature);
        }

        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : map.keySet()) {
            try {
                temp = URLEncoder.encode(map.get(s), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(s).append("=").append(temp).append("&");
        }

        return sb.deleteCharAt(sb.length()-1).toString();
    }
	
	/**
     * 构造X-WSSE参数值
     * @param appKey
     * @param appSecret
     * @return
     */
    private String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        MessageDigest md;
        byte[] passwordDigest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + time + appSecret).getBytes());
            passwordDigest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(passwordDigest); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
    
    /**
     * 忽略证书信任问题
     * @throws Exception
     */
    private void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

}
