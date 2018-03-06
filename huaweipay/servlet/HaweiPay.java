package com.huaweipay.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class HaweiPay
 */
@WebServlet("/HaweiPay")
public class HaweiPay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HaweiPay() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		System.out.println("进入servelet：");
		// 获取华为服务器的参数
		request.setCharacterEncoding("UTF-8");
		Map<String, Object> map = null;
		map = getValue(request);
		if (null == map) {
			System.out.println("map is null");
			return;
		}
		//System.out.println("map:" + map);
		//得到签名，用来支持下面验签
		String sign = (String) map.get("sign");
		System.out.println("sign:" + sign);

		HandlePayResult result = new HandlePayResult();
		result.setResult(1); //
		
		//验签 正式的支付公钥
		String devPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsZ1sOB6K3tnqb7nXGNW8EfPLGn7LCD6YV9vtISuW78sZsT+juWgMFeoepSiErngPadb/FC5QSVAw/cwf2L9YAY3B3LS/t9HNNV5ah7YkPYEMGb+p0HaE26vEAnwLvysTcipZywhjJfSwhZUqkGWE0qVBX+cQTbusKlpimR3Sb98ToOdVKh0ZbN9UsM0QvrcS7Zu0J8i9THd/dBhgkWAyaLl7nx47hFWVhLEkB0CC8apCaJ4AxhECRtsiE7V0SHMIQaKTaIPBoXYr8XUJD1Uebm+/lZGumHqklXLw17n5L+9EEDPPKZBlsVkKEBh2ltzqvm/Qm1a3HqTMQ45IT1s/0QIDAQAB";
		
		//String mypubKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu0wLGfiBje9YFz83NZ8BGOL8OL/GXkN5vqD4l5AtqQ1qbgl9oyYn+ivcs7GF/yFMzDaLc9EV2sJ9JdL6wuIw8X9Ty8xmXhZi9sLusdTTUWTKD8CvJoh+PS2SYcPKPVXXEKgNWyBIlNLxKF1O2Q9CfF9xs/hTk9O8wT4gdqLnBzwIDAQAB";
		//测试公钥
		String testPubKey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIW1g+KAqqOeC1ypte8L3qTDk2nz6jUbM6o6Jg9obvivPnCAm/wZvV3jWbYWfOuO/wrFJygn/jZqf8cR1T1CQa8CAwEAAQ==";
		
		
		
		String content=Verification.getSignData(map);
		System.out.println(content);
		String myPrivatekey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK7TAsZ+IGN71gXPzc1nwEY4vw4v8ZeQ3m+oPiXkC2pDWpuCX2jJif6K9yzsYX/IUzMNotz0RXawn0l0vrC4jDxf1PLzGZeFmL2wu6x1NNRZMoPwK8miH49LZJhw8o9VdcQqA1bIEiU0vEoXU7ZD0J8X3Gz+FOT07zBPiB2oucHPAgMBAAECgYAIzOBv43t35ozwf/RpKZbZMhQT/7/WlFlOH5b5S1Bzye4us9JfsP18TbyBi2ihCRF/GWmgW79/aQarqO/Mb43Hius2+kzkfWqSoTPDMuJMIba80cyt3/Y/7/Fv8+EZcL4oEQxMuaYUE9nR6pbTTLPZCy9Dfx23GEQEdCracFgsIQJBAN8fwEPgrg/DyZ2T0rCcadpl1++8gNh7kRDJMiLeEoDD9HZddwgx4d6YauKo/AGV2nfTC2VpUPjEO7k3VK3yFlECQQDIlWPrhupeFORDFOKkRowsKppsLm/FNTBrssPder0YZHWrtFM4or+ANPY4kSDhr0OnEwc9yPjJuDU2iMPD3q4fAkB8Zhxw+8SwH226QF+Yfix6SovhKsz/Za1UmYgtbnkMd7RFhgjGdeFDlOEK7EJMs/rxEaZp7RTrDaVqt5GrxaxxAkEAgiWEZr7sdM0Dao8zmzl3G/Usq0LaHNAtMcQVGDDaXuhX/WgMk3Pt42b516w69JR+xfovuaQb4O7YACFvPDjRKwJAHvJDzzgLH/ZMH4O4ReAWyEKpHMapdAMpRNV0Em5W+jJRgGXVu0O1qImP52pe2QLQF44APadYOhe6H1Iz+8Qq+w==";
		String privatekey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxnWw4Hore2epvudcY1bwR88safssIPphX2+0hK5bvyxmxP6O5aAwV6h6lKISueA9p1v8ULlBJUDD9zB/Yv1gBjcHctL+30c01XlqHtiQ9gQwZv6nQdoTbq8QCfAu/KxNyKlnLCGMl9LCFlSqQZYTSpUFf5xBNu6wqWmKZHdJv3xOg51UqHRls31SwzRC+txLtm7QnyL1Md390GGCRYDJouXufHjuEVZWEsSQHQILxqkJongDGEQJG2yITtXRIcwhBopNog8GhdivxdQkPVR5ub7+Vka6YeqSVcvDXufkv70QQM88pkGWxWQoQGHaW3Oq+b9CbVrcepMxDjkhPWz/RAgMBAAECggEASrU6hchjBSWH9IKotOuB9dMjxPs8DzW1Ao+hCGi7ThWRsvpftWbMXpNhXHrUhEY/xXcHR8fLQWsXkElBw/uH2u9zcZAdbAY1WJNdZOP6DlyvaE0z8llHvNZc1eazUi+eOFET/9CBU7++WBHMG3x4B9o2y033j5q26gGmo1zl3OO15Z0AidhrsYUZcKI6gJp0ZfRhXPS39xrYHiOX1qxCjE9s0r7qpBG5oQ4fKxLCftYHs1R/HYYVXfrNtrMQYNNyMlgeBHfXPwLuXxw/M7pYT2kgLyUXzwA8JGot4hSxcqzE8RqdWRty/2/p643kKu+WNX01K9bgs2dUXZCvD5KKWQKBgQDoRWdJAXH3qZBeAOJoj/fJBhaU8zkDBnCogGfbqpwvrgIi4cEO2sjgSuFtwc+LcUV4Cv+w/qXKzkaTYvo1Y8RGhMrcNXHfUkTwV0zHjMDO5i5u6QsUuxnTye9p6OO7nFwd8PTLeOVJzMkGo3bJyHPpa1LuMHtP/3AZYLhjq7P9iwKBgQDDwpgLDh4uWzuxoguT0XYxN1xXMTxqnOzTQ+SeSgDm9N7S10uNxkeUaudap7cDChyzi3AKaAg/jMlrs8nyKrAtbeukST+A4gPYO6YlF7rGuKnG37PpaiWqqEbTZegrHsNs3r5/23RfHvyVAlrphAZxaAg5dlEGQgf1BLQ7YCkbkwKBgD8CzQFGLhfE3VBTJxi8rbjQOQIRdY73iUp9Ay/ZeeOJbjTuT4RrIAGQ7tTqthYbFYB6Y2Etw+ZfzU+gk0Y2mYGT9sCEky7FT8RvunmMGqZGVaRq/kGSfHAzIQr3TgiQY4EP5Cjq1DEQKBzv7YLSKXfSUL4jUuCh6FRKI9uOMhb/AoGBALR4+vj+K/7qpy7NSMUfD0qyUhQkVSGoyIDAj80KRqilyaMxTvtGeAkxQVcHVaactPclrsY5QJlt7ue3GY+DoWZQdzS/PqdQNuErpLF/nfbEmei5pcCj1lPtzVXpFlBijSIafB+drzxecdfiEvRDfjkhAqwPEwWk7HcLvikbLuq3AoGAPICdTFOWA0Is4qWAJySdsOnlvw6RaAWIpsP4ISeQuNsRLti5jy2sFdrg5DDhgBprW9GveqkR2V3L2WGqJug35EnMoSk8FMC3JBnLOZHKAn/W3bfODscY/AXuBeceXooGhCnlaoY+dqhcfoUJU6cYKl/TmSLADxTW2qD85uGiDow=";
		String str=Verification.sign(content, privatekey, "1");
		System.out.println("self sign:"+str);
		
		
		
		if (Verification.rsaDoCheck(map, sign, devPubKey, (String) map.get("signType"))) {
			//验签成功，发送给后台服务器
			result.setResult(0);
			Map<String, Object>mapformat=convertFormat(map);
			System.out.println("1111:"+mapformat);
			Map<String, String>mapString=new HashMap<String,String>();
			System.out.println(mapformat.keySet());
			for (String strkey : mapformat.keySet()) {
				mapString.put(strkey, (String)mapformat.get(strkey));
				}
			System.out.println("converted map:"+mapString);
			sendOrderFrom(mapString);
			System.out.println("Result : 0!");
		} else {
			//验签失败，设置返回给华为服务器的值为1
			result.setResult(1);
			System.out.println("Result : 1!");
		}

		String resultinfo = convertJsonStyle(result);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		System.out.println("Response string: " + resultinfo);

		PrintWriter out = response.getWriter();

		out.print(resultinfo);
		out.close();

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

	//解析华为发过来的原始字符串，扔到map当中
	public Map<String, Object> getValue(HttpServletRequest request) {

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
		Map<String, Object> valueMap = new HashMap<String, Object>();
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
		System.out.println("The parameters in map are : " + valueMap);
		try {
			String sign = (String) valueMap.get("sign");
			System.out.println(sign);
			String extReserved = (String) valueMap.get("extReserved");
			String sysReserved = (String) valueMap.get("sysReserved");
			String productName = (String) valueMap.get("productName");
			if (null != sign) {
				sign = URLDecoder.decode(sign, "utf-8");
				valueMap.put("sign", sign);
			}
			if (null != productName) {
				sign = URLDecoder.decode(sign, "utf-8");
				valueMap.put("sign", sign);
			}
			if (null != extReserved) {
				extReserved = URLDecoder.decode(extReserved, "utf-8");
				valueMap.put("extReserved", extReserved);
			}

			if (null != productName) {
				//productName = URLDecoder.decode(productName, "utf-8");
				productName=new String(productName.getBytes(),"UTF-8");
				valueMap.put("productName", productName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return valueMap;
	}

	private String convertJsonStyle(Object resultMessage) {
		ObjectMapper mapper = new ObjectMapper();
		Writer writer = new StringWriter();
		try {
			if (null != resultMessage) {
				mapper.writeValue(writer, resultMessage);
			}

		} catch (Exception e) {

		}

		return writer.toString();
	}

	 public static String loadPublicKeyByFile(String path) throws Exception {  
	        try {  
	            BufferedReader br = new BufferedReader(new FileReader(path  
	                    ));  
	            String readLine = null;  
	            StringBuilder sb = new StringBuilder();  
	            while ((readLine = br.readLine()) != null) {  
	                sb.append(readLine);  
	            }  
	            br.close();  
	            return sb.toString();  
	        } catch (IOException e) {  
	            throw new Exception("公钥数据流读取错误");  
	        } catch (NullPointerException e) {  
	            throw new Exception("公钥输入流为空");  
	        }  
	    }  
	 
	 //构建json字符串发送
	 public void sendOrderFrom(Map<String, String> mapString)
		{
			String httpreq = "{";
			
			Iterator<String> iter = mapString.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				String velue=mapString.get(key);
				httpreq=httpreq+"\""+ key + "\":\"" + velue + "\",";
			}
			httpreq+="\"GameType\":\"100016\"}";
			
			try{
			URL postUrl=new URL("http://localhost:8000/");
			//****************建立连接 begin*****************
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			//post模式设置
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			connection.connect();
			//*****************建立连接 end*******************
			//获取输入流
			DataOutputStream write=new DataOutputStream(connection.getOutputStream());
			
			write.writeBytes(httpreq);
			write.flush();
			write.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
			
			String line;
			while((line=reader.readLine())!=null)
			{

				System.out.println(line);
			}
			reader.close();	
			}catch(Exception exc){
				
			}
		}
	 
	//订单转换
	 private Map<String,Object> convertFormat(Map<String, Object> map) {
		System.out.println("map:"+map);
		if(!map.containsKey("orderID"))
			//return null;
		map.put("app_order_id", (String)map.get("orderID"));
		map.remove("orderID");
		if(!map.containsKey("extReserved")) {
			map.put("app_ext1", "");
		}
		map.put("app_ext1", (String)map.get("extReserved"));
		map.remove("extReserved");
		if(!map.containsKey("result")) {
			//return null;
		}	
		map.put("gateway_flag", "success");		
		
		return map;
	 }
	 
	 
	 
}
