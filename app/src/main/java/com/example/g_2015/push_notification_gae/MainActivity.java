package com.example.g_2015.push_notification_gae;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private final String PROJECT_ID = "967269839915";
    AsyncTask<Void, Void, String> registtask = null;
//    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public GoogleCloudMessaging gcm;
    public String regid = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // Play serviceが有効かチェック
        if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);
            Log.i("", regid);
            if(regid.equals("")){

                regist_id();
            }

        } else {
            Log.i("", "Google Play Services は無効");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("", "Play Service not support");
            }
            return false;
        }
        return true;
    }


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.equals("")) {
            return "";
        }
        // アプリケーションがバージョンアップされていた場合、レジストレーションIDをクリア
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }

        return registrationId;
    }


    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }


    private int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("package not found : " + e);
        }


    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void regist_id(){
        if (regid.equals("")) {
            registtask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    try {
                        //GCMサーバーへ登録
                        regid = gcm.register(PROJECT_ID);
                        Log.i("", regid);
                        //取得したレジストレーションIDを自分のサーバーへ送信して記録しておく
                        //サーバーサイドでは、この レジストレーションIDを使ってGCMに通知を要求します
//                        registeid2YouServer(regid);

                        // レジストレーションIDを端末に保存
                        storeRegistrationId(context, regid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    registtask = null;


                }
            };
            registtask.execute(null, null, null);
        }

    }


//    public boolean registeid2YouServer(String regId) {
//
//
//        String serverUrl = "http://localhost:8080/pushtest.php";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("my_id", regId);
//        try {
//            post(serverUrl, params);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public static void post(String endpoint, Map<String, String> params)
//            throws IOException {
//        URL url;
//        try {
//            url = new URL(endpoint);
//        } catch (MalformedURLException e) {
//            throw new IllegalArgumentException("invalid url: " + endpoint);
//        }
//        StringBuilder bodyBuilder = new StringBuilder();
//        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> param = iterator.next();
//            bodyBuilder.append(param.getKey()).append('=')
//                    .append(param.getValue());
//            if (iterator.hasNext()) {
//                bodyBuilder.append('&');
//            }
//        }
//        String body = bodyBuilder.toString();
//        byte[] bytes = body.getBytes();
//        HttpURLConnection conn = null;
//        try {
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setFixedLengthStreamingMode(bytes.length);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
//            OutputStream out = conn.getOutputStream();
//            out.write(bytes);
//            out.close();
//            int status = conn.getResponseCode();
//            if (status != 200) {
//                throw new IOException("Post failed with error code " + status);
//            }
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//    }
}
