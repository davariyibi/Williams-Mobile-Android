package com.daksoftwareproducts.kevin.wso;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Kevin on 8/18/2017.
 */

public class HttpsObject implements Serializable{

    protected String host = "wso.williams.edu";
    protected String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
    protected String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    protected String acceptEncoding = "gzip, deflate, br";
    protected String acceptLanguage = "en-US,en;q=0.8";
    protected String cacheControl = "max-age=0";
    protected String CONNECTION = "keep-alive";
    protected String contentType = "application/x-www-form-urlencoded";

    public int responseCode = 0;
    private List<String> cookies;


    public void sendPost( HttpsURLConnection connection, String requestMethod, String referer, String postParams ) throws Exception {

        // Acts like a browser
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Host", host );
        connection.setRequestProperty("Connection", CONNECTION );
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", accept );
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("Accept-Encoding", acceptEncoding );
        connection.setRequestProperty("Accept-Language", acceptLanguage );
        if (cookies != null) {
            for (String cookie : this.cookies) {
                connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        connection.setRequestProperty("Cache-Control", cacheControl );
        connection.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        connection.setRequestProperty("Content-Type", contentType );


        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setInstanceFollowRedirects(false);

        // Send post request
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        Map<String, List<String>> map = connection.getHeaderFields();
        //setCookies(connection.getHeaderFields().get("Set-Cookie"));

        System.out.println("\nPrinting Response Header...\n");

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
        }

        setResponseCode(connection.getResponseCode());
        String responseMessage = connection.getResponseMessage();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode +" "+ responseMessage);
        //connection.disconnect();
    }

    public void sendLoginPost(HttpsURLConnection connection, String requestMethod, String referer, String postParams ) throws Exception {
        // Acts like a browser
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
        for (String cookie : this.cookies) {
            connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);

        // Send post request
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        Map<String, List<String>> map = connection.getHeaderFields();
        setCookies(connection.getHeaderFields().get("Set-Cookie"));

        System.out.println("\nPrinting Response Header...\n");

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
        }

        setResponseCode(connection.getResponseCode());
        String responseMessage = connection.getResponseMessage();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode +" "+ responseMessage);
        connection.disconnect();
    }

    public void sendDiningPost(HttpURLConnection connection, String requestMethod, String referer, String postParams ) throws Exception {
        // Acts like a browser
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
        /*
        for (String cookie : this.cookies) {
            connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
        */
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        connection.setRequestProperty("Cookie", "CBORD.netnutrition2=NNexternalID=1; pshrapp2-23400-PORTAL-PSJSESSIONID=_LD_y0_hZnXwgePDuAv6J40mTyYb1sRR!1719960105; ASP.NET_SessionId=5kfw1oswo04egcsh11525zrw");

        //connection.setDoOutput(true);
        //connection.setDoInput(true);
        //connection.setInstanceFollowRedirects(false);

        // Send post request
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        Map<String, List<String>> map = connection.getHeaderFields();
        setCookies(connection.getHeaderFields().get("Set-Cookie"));

        System.out.println("\nPrinting Response Header...\n");

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
        }

        setResponseCode(connection.getResponseCode());
        String responseMessage = connection.getResponseMessage();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode +" "+ responseMessage);
        //connection.disconnect();
    }


    public void openConnection( HttpsURLConnection connection, String requestMethod, String referer ) throws Exception {
        // Acts like a browser
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("Connection", CONNECTION);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", accept);
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("Accept-Encoding",acceptEncoding);
        connection.setRequestProperty("Accept-Language", acceptLanguage);
        if (cookies != null) {
                for (String cookie : this.cookies) {
                    connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
            }
    }

    public String getPageContent( HttpsURLConnection connection, String link ) throws Exception{
        // act like a browser
        connection.setUseCaches(false);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Language", acceptLanguage);
        if (cookies != null) {
            for (String cookie : this.cookies) {
                connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }

        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        System.out.println("\nSending 'GET' request to URL : " + link);
        System.out.println("Response Code : " + responseCode + " " + responseMessage);
        System.out.println("GET Response : ");

        String response = printResponse(connection);

        connection.disconnect();

        return response.toString();
    }

    public String getLoginPageContent( HttpsURLConnection connection, String link ) throws Exception {
        // act like a browser
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", accept );
        connection.setRequestProperty("Accept-Language", acceptLanguage );
        if (cookies != null) {
            for (String cookie : this.cookies) {
                connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        System.out.println("\nSending 'GET' request to URL : " + link);
        System.out.println("Response Code : " + responseCode + " " + responseMessage);
        System.out.println("GET Response : ");

        String response = printResponse(connection);

        // Get the response cookies
        setCookies(connection.getHeaderFields().get("Set-Cookie"));

        connection.disconnect();
        return response.toString();
    }

    public String printResponse( HttpsURLConnection connection ) throws Exception{
        BufferedReader in =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();


        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    // Getter and Setter for responseCode
    public int getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    // Getter and Setter for cookies
    public List<String> getCookies() {
        return cookies;
    }
    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }

}
