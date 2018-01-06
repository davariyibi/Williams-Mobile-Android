package com.daksoftwareproducts.kevin.wso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class LoginActivity extends AppCompatActivity {

    private HttpsURLConnection connection;
    private HttpsObject httpsObject;

    boolean redirect = false;
    ProgressDialog mProgressDialog;

    String url, newUrl, html;
    Button login;
    EditText username,password;
    TextView badLogin, forgotU, forgotP;
    CheckBox rememberMe;

    SharedPreferences sharedPreferences;

//    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.loginButton);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        badLogin = (TextView) findViewById(R.id.incorrectLogin);
        forgotU = (TextView) findViewById(R.id.forgotUsername);
        forgotP = (TextView) findViewById(R.id.forgotPassword);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);

        // create primary HttpsObject
        httpsObject = new HttpsObject();

        // make sure cookies is turn on
        CookieHandler.setDefault(new CookieManager());

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        if( sharedPreferences.contains("Username") && sharedPreferences.contains("Password") ){
            username.setText(sharedPreferences.getString("Username","null") );
            password.setText(sharedPreferences.getString("Password","null"));
//            new Login().execute();
        }

        // Capture button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login().execute();
            }
        });

    } // end of onCreate method


    // Title AsyncTask
    private class Login extends AsyncTask<Void, Void, Void> {

        String name;
        String passwd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = username.getText().toString();
            passwd = password.getText().toString();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setTitle("Log In");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // 1. Send a "GET" request, so that you can extract the form's data.
                url = "https://wso.williams.edu/account/login";
                URL obj = new URL(url);
                connection = (HttpsURLConnection) obj.openConnection();
                String postParams = getFormParams( httpsObject.getLoginPageContent(connection,url), name, passwd );

                // 2. Construct above post's content and then send a POST request for authentication
                sendPost(postParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 3. success then go to wso
            mProgressDialog.dismiss();
        }
    } // End of login method

    private void launchActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("newUrl",newUrl);
        intent.putExtra("httpsObject",httpsObject);
        System.out.println(httpsObject.getCookies().toString());
        intent.putExtra("html",html);
        startActivity(intent);
        finish();
    }


    private void sendPost(String postParams) throws Exception {
        url = "https://wso.williams.edu/account/login?class=login";
        URL obj = new URL(url);
        connection = (HttpsURLConnection) obj.openConnection();

        // Param: HttpsURLConnection, RequestMethod String, Referer String, Post Parameters String
        httpsObject.sendLoginPost(connection,"POST","https://wso.williams.edu/account/login",postParams);
        System.out.println("\nSending 'POST' request to URL : " + url);

        //int responseCode = connection.getResponseCode();
        int responseCode = httpsObject.getResponseCode();

        if (responseCode != connection.HTTP_OK) {
            if (responseCode == connection.HTTP_MOVED_TEMP || responseCode == connection.HTTP_MOVED_PERM
                    || responseCode == connection.HTTP_SEE_OTHER)
                redirect = true;
        }

        if (redirect) {
            // get redirect url from "location" header field
            newUrl = connection.getHeaderField("Location");
            System.out.println("Redirect to URL : " + newUrl);
            connection = (HttpsURLConnection) new URL(newUrl).openConnection();
            httpsObject.openConnection(connection, "GET", "https://wso.williams.edu/account/login");

            // protect against correct then incorrect login attempts
            redirect = false;

            System.out.println("POST Response : ");

            Map<String, List<String>> header = connection.getHeaderFields();
            for ( Map.Entry<String, List<String>> entry : header.entrySet()) {
                System.out.println("Key : " + entry + " ,Value : " + entry.getValue());
            }

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.append(inputLine);
            }
            html = response.toString();
            in.close();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Username",username.getText().toString() );
            editor.putString("Password",password.getText().toString() );
            editor.commit();

            //connection.disconnect();
            launchActivity();
            return;
        }

        // Implement incorrect login situation
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                username.getText().clear();
                password.getText().clear();
                username.requestFocus();
                badLogin.setVisibility(View.VISIBLE);
            }
        });

        connection.disconnect();
    }

    public String getFormParams(String html, String username, String password) throws UnsupportedEncodingException {
        System.out.println("Extracting form's data...");
        Document doc = Jsoup.parse(html);

        // WSO login page form id
        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("username"))
                value = username;
            else if (key.equals("password"))
                value = password;
            else if (key.equals("remember_me"))
                if(rememberMe.isChecked()) {
                    value = "1";
                } else {
                    value = "0";
                }
            paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
        }
        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }
        return result.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.wso_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    // Test AsyncTask method
    private class Test extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setTitle("Log In");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL obj = new URL(url);
                connection = (HttpsURLConnection) obj.openConnection();

                //dumpl all cert info
                print_https_cert(connection);

                //dump all the content
                print_content(connection);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
        }
    } // End of test method


    private void print_https_cert(HttpsURLConnection con){
        if(con!=null){
            try {
                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void print_content(HttpsURLConnection con){
        if(con!=null){
            try {
                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input;
                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }

}