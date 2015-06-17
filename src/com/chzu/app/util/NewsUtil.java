package com.chzu.app.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class NewsUtil {
	
	static AndroidHttpClient httpClient = HttpUtils.getHttpClient();
	/**
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String urlStr)   
    {  
        String sb = null;  
        try  
        {  
        	HttpGet httpGet = new HttpGet(urlStr);
    		HttpResponse response = httpClient.execute(httpGet);
            
    		if(response != null){
            	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
                {  
                	sb = EntityUtils.toString(response.getEntity());
                } 
            }
    		
        } catch (Exception e)  
        {  
            e.printStackTrace();
        }  
        return sb;  
    }  
}
