package com.alibaba.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class ALiPay
 */
@WebServlet("/ALiPay")
public class ALiPay extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONObject signObject;
	//private String appSecret="202cb962234w4ers2aa";//测试的appkey
	private String appSecret="";//正式版的appkey
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ALiPay() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		request.setCharacterEncoding("utf-8");
		//得到参数
		HashMap<String, String> map = parseParaMap(request, response);
		System.out.println("map:" + map);
		//验签
		String signStr=Util.getSign(signObject, appSecret);
		String md5Str=Util.md5(signStr);
		System.out.println("md5:"+map.get("sign").toString());
		System.out.println("md5Str:"+md5Str);
		if(md5Str.equals(map.get("sign").toString())){
			//验签成功
			//返回给阿里服务器SUCCESS
			response.setCharacterEncoding("utf-8");
			PrintWriter out=response.getWriter();
			out.write("SUCCESS");
			out.flush();
			out.close();
			//订单信息处理后发送给后台服务器
			Map<String,String> sendMap=convertFormat(map);
			Util.sendOrderFrom(sendMap);
		}
		else {
			//验签失败
			//返回给阿里服务器错误
			response.setCharacterEncoding("utf-8");
			PrintWriter out=response.getWriter();
			out.write("FAILURE");
			out.flush();
			out.close();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private HashMap<String, String> parseParaMap(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		String result = "";
		try {
			// 读取输入流到StringBuffer中
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);

		} catch (Exception e) {
			/* report an error */ }

		// 使用JSONObject的parseObject方法解析JSON字符串
		JSONObject jsonObject = JSONObject.parseObject(jb.toString());// json对象
		// result = jsonObject.toJSONString(); //json字符串
		JSONObject jsonObject2 = jsonObject.getJSONObject("data");
		signObject=jsonObject2;
		String str = jsonObject2.toJSONString();
		//System.out.println(str);
		HashMap<String, String> map = new HashMap<String, String>();
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			if (key == "data")
				continue;
			map.put(key, jsonObject.getString(key).toString());
		}
		keys = jsonObject2.keySet();
		for (String key : keys) {
			map.put(key, jsonObject2.getString(key).toString());
		}
		appSecret=map.get("attachInfo").toString().toLowerCase();
		return map;

		// return map;

	}
	
	//订单转换
	 private Map<String,String> convertFormat(Map<String, String> map) {
		
		if(!map.containsKey("tradeId"))
			return null;
		map.put("app_order_id", (String)map.get("tradeId"));
		map.remove("tradeId");
		if(!map.containsKey("attachInfo")) {
			map.put("app_ext1", "");
		}else {
			map.put("app_ext1", (String)map.get("attachInfo"));
			map.remove("attachInfo");
		}
		
		if(!map.containsKey("orderStatus")) {
			return null;
		}else
		{
			if("s".equals(map.get("orderStatus")))
				map.put("gateway_flag", "success");
			else {
				map.put("gateway_flag", "");
			}
		}
				
		System.out.println("mapFormat:"+map);
		return map;
	 }
}
