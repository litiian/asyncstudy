package com.study.http.asynchttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class AsyncHttp {
//	private static final Logger LOG = LoggerFactory.getLogger(AsyncHttp.class);
//	public static void asyncHttpGet(String url) throws  Exception {
//		    CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
//		    
//	        try {
//	            httpclient.start();
//	            Future<Boolean> future = httpclient.execute(
//	                    HttpAsyncMethods.createGet(/**"http://httpbin.org/"**/url),
//	                    new MyResponseConsumer(), null);
//	            
//	            Boolean result = future.get();	            
//	            if (result != null && result.booleanValue()) {
//	                System.out.println("Request successfully executed");
//	            } else {
//	                System.out.println("Request failed");
//	            }
//	            
//	            System.out.println("Shutting down");
//	        } finally {
//	            httpclient.close();
//	        }
//	        
//	        System.out.println("Done");
//	}
	/**
	 * 异步httppost
	 * @param url url地址
	 * @param params post的map数据
	 */
	public static void asyncHttpPost(String url,Map<String, String> params) {
		 RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)  
	                .setConnectionRequestTimeout(10000).setConnectTimeout(10000).setSocketTimeout(50000). build();  
		 final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(globalConfig).build();  
       
            httpclient.start();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            String rowKey=params.get("rowkey");
            params.remove("rowkey");
	        for (Map.Entry<String, String> mapEntry : params.entrySet()) {
	        	nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue()));
			}
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
            httpclient.execute(
            		httpPost,
                    new FutureCallback<HttpResponse>() {

						public void completed(HttpResponse response) {
            				//这里使用EntityUtils.toString()方式时会大概率报错，原因：未接受完毕，链接已关  
          				  	String  body = getBody(response); 
          				  	System.out.println(body);
          				  	closeResource(httpclient);
						}

						public void failed(Exception ex) {
							closeResource(httpclient);
						}

						public void cancelled() {
							closeResource(httpclient);
						}

						private String getBody(final HttpResponse response) {
							String body="";
							try {  
							    HttpEntity entity = response.getEntity();  
							    if (entity != null) {  
							        final InputStream instream = entity.getContent();  
							        try {  
							            final StringBuilder sb = new StringBuilder();  
							            final char[] tmp = new char[1024];  
							            final Reader reader = new InputStreamReader(instream,"utf-8");  
							            int l;  
							            while ((l = reader.read(tmp)) != -1) {  
							                sb.append(tmp, 0, l);  
							            }  
							            body = sb.toString();  
							        } finally {  
							            instream.close();  
							            EntityUtils.consume(entity);  
							        }  
							    }  
							} catch (Exception e) {  
							    e.printStackTrace(); 
							}
							return body;
						}
            		} ); 
	}
	/**
	 * 关闭httpclient链接
	 * @param httpclient
	 */
	public static void closeResource(CloseableHttpAsyncClient httpclient){
		if(null!=httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				
			}
		}
	}
}
