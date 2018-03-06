package com.baidu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.awt.SunHints.Value;



/**
 * Servlet implementation class BaiduPay
 */
@WebServlet("/BaiduPay")
public class BaiduPay extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String appSecret="";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaiduPay() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("utf-8");
		Map<String,String>map=getValue(request);
		System.out.println("map:"+map);
		
		String signStr=Util.getSign(map, appSecret);
		System.out.println("signStr:"+signStr);
		
		String md5Str=Util.md5(signStr);
		System.out.println("md5Str:"+md5Str);
		
		if(md5Str.equals(map.get("sign").toString())) {
			//验签成功
			//返回给百度服务器SUCCESS
			response.setCharacterEncoding("utf-8");
			PrintWriter out=response.getWriter();
			out.write("success");
			out.flush();
			out.close();
			//订单信息处理后发送给后台服务器
			Map<String,String> sendMap=convertFormat(map);
			Util.sendOrderFrom(sendMap);
		}
		else {
			//验签失败
			//不用返回给百度服务器错误
			/*response.setCharacterEncoding("utf-8");
			PrintWriter out=response.getWriter();
			out.write("FAILURE");
			out.flush();
			out.close();*/
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public Map<String, String> getValue(HttpServletRequest request) {

		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			request.setCharacterEncoding("UTF-8");
			InputStream stream = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			System.out.println("The original data is : " + sb.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		String str = sb.toString();
		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		if (null == str || "".equals(str)) {
			return valueMap;
		}
		//str = str.replace("\"", "");
		str = str.replace("\r", "");
		str = str.replace("\n", "");
		String[] valueKey = str.split("&");
		for (String temp : valueKey) {
			if (temp != null) {
				int idx = temp.indexOf('=');
				int len = temp.length();
				if (idx != -1) {
					String key = temp.substring(0, idx);
					String value = idx + 1 < len ? temp.substring(idx + 1) : "";
					valueMap.put(key, value);
				}
			}
		}
		appSecret=valueMap.get("cpdefinepart").toString();
		System.out.println("The parameters in map are : " + valueMap);
		return valueMap;
	}
	
	
	 public String getMd5(String pInput) {  
	        try {  
	            MessageDigest lDigest = MessageDigest.getInstance("MD5");  
	            lDigest.update(pInput.getBytes());  
	            BigInteger lHashInt = new BigInteger(1, lDigest.digest());  
	            return String.format("%1$032X", lHashInt);  
	        } catch (NoSuchAlgorithmException lException) {  
	            throw new RuntimeException(lException);  
	        }  
	    }  
	 
	 private Map<String,String> convertFormat(Map<String, String> map) {
			
			if(!map.containsKey("orderid"))
				return null;
			map.put("app_order_id", (String)map.get("orderid"));
			map.remove("orderid");
			if(!map.containsKey("cpdefinepart")) {
				map.put("app_ext1", "");
			}else {
				map.put("app_ext1", (String)map.get("cpdefinepart"));
				map.remove("cpdefinepart");
			}
			
			if(!map.containsKey("status")) {
				return null;
			}else
			{
				if("success".equals(map.get("status")))
				{
					map.put("gateway_flag", "success");
					map.remove("status");
				}
				else {
					map.put("gateway_flag", "");
				}
			}
					
			System.out.println("mapFormat:"+map);
			return map;
		 }
}
