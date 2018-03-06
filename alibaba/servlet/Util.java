package com.alibaba.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Util {
	
	public static String getSign(JSONObject signObject, String appSecret) {
		Object[] keys = signObject.keySet().toArray();
		Arrays.sort(keys);
		String k, v;

		String str = "";
		for (int i = 0; i < keys.length; i++) {
			k = (String) keys[i];
			/*if (k.equals("sign") || k.equals("sign_return")) {
				continue;
			}
            if(params.get(k)==null){
                continue;
            }*/
			v = (String) signObject.get(k);

			/*if (v.equals("0") || v.equals("")) {
				continue;
			}*/
			str +=k+"="+v ;
		}
		
		str=str+appSecret;
		str=str.toLowerCase();
		System.out.println("sign str:"+str);
		return str;
	}

	public static String md5(String str) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes("UTF8"));
			byte bytes[] = m.digest();

			for (int i = 0; i < bytes.length; i++) {
				if ((bytes[i] & 0xff) < 0x10) {
					sb.append("0");
				}
				sb.append(Long.toString(bytes[i] & 0xff, 16));
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	public static String md5v2(String password) {
		try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
	}
	
	public static void sendOrderFrom(Map<String, String> orderParams)
	{
		String httpreq = "{";
		
		Iterator<String> iter = orderParams.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String velue=orderParams.get(key);
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
	
}
