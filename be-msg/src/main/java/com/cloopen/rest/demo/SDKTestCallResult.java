/*
 *  Copyright (c) 2014 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cloopen.rest.demo;

import com.cloopen.rest.sdk.CCPRestSDK;

import java.util.HashMap;
import java.util.Set;
 

public class SDKTestCallResult {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
/*		restAPI.setAccount("AccountSid", "AccountToken");// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId("AppId");// 初始化应用ID*/
		restAPI.setAccount("8a216da85c62c9ad015cb034616b1e15", "3ad60ef8fe6d4af8b7551ea1e945d258");
		restAPI.setAppId("8a216da85c62c9ad015cb03461ab1e1a");
//		result = restAPI.CallResult("18401751715");//呼叫ID
		result = restAPI.sendTemplateSMS("18401751715", "202092", new String[] { "101717", "5分钟" });
		System.out.println("SDKTestCallResult result=" + result);
		
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
	}
	
}
