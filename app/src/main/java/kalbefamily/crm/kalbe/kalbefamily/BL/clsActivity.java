package kalbefamily.crm.kalbe.kalbefamily.BL;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import kalbefamily.crm.kalbe.kalbefamily.Common.enum_mconfig;
import kalbefamily.crm.kalbe.kalbefamily.R;

/**
 * Created by rhezaTesar on 3/6/2017.
 */

public class clsActivity extends Activity {
    public String txtVersionApp(){
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            return "No Version";
        }
    }
    public Boolean ValidJSON(JSONObject JsonRes){
        if(JsonRes == null){
            return false;
        }else{
            if(JsonRes.length()==0){
                return false;
            }else{
                if(JsonRes.toString().equals("{}")==false){
                    return true;
                }else{
                    return false;
                }
            }
        }

    }
//    public JSONObject PushData(String txtMethod, String txtJson) throws Exception {
//        Mobile_mConfigBL _Mobile_mConfigBL=new Mobile_mConfigBL();
//        String txtLink=_Mobile_mConfigBL.getValue(enum_mconfig.API.getValue());
//        JSONObject _JSONArray=callPushDataReturnJson(txtLink,txtMethod,txtJson);
//        return _JSONArray;
//    }
    public void showToastWarning(Context ctx, String str){
        LayoutInflater mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View promptView = mInflater.inflate(R.layout.custom_toast, null);

        TextView tvTextToast = (TextView) promptView.findViewById(R.id.custom_toast_message);
        ImageView icon = (ImageView) promptView.findViewById(R.id.custom_toast_image);
        tvTextToast.setText(str);

        GradientDrawable bgShape = (GradientDrawable)promptView.getBackground();

        bgShape.setColor(Color.parseColor("#e74c3c"));
        icon.setImageResource(R.drawable.ic_error);
        /*
        if (status) {
            bgShape.setColor(Color.parseColor("#6dc066"));
            icon.setImageResource(R.drawable.ic_checklist);

        } else {
            bgShape.setColor(Color.parseColor("#e74c3c"));
            icon.setImageResource(R.drawable.ic_error);
        }
        */
        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(promptView);
        toast.setGravity(Gravity.TOP, 25, 400);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        /*
        Toast toast = Toast.makeText(ctx,
                str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
        */
    }

    public void showToast(Context ctx, String str){
        LayoutInflater mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View promptView = mInflater.inflate(R.layout.custom_toast, null);

        TextView tvTextToast = (TextView) promptView.findViewById(R.id.custom_toast_message);
        ImageView icon = (ImageView) promptView.findViewById(R.id.custom_toast_image);
        tvTextToast.setText(str);

        GradientDrawable bgShape = (GradientDrawable)promptView.getBackground();

        bgShape.setColor(Color.parseColor("#6dc066"));
        icon.setImageResource(R.drawable.ic_checklist);
        /*
        if (status) {
            bgShape.setColor(Color.parseColor("#6dc066"));
            icon.setImageResource(R.drawable.ic_checklist);

        } else {
            bgShape.setColor(Color.parseColor("#e74c3c"));
            icon.setImageResource(R.drawable.ic_error);
        }
        */
        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(promptView);
        toast.setGravity(Gravity.TOP, 25, 400);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        /*
        Toast toast = Toast.makeText(ctx,
                str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
        */
    }

    public void showCustomToast(Context context, String message, Boolean status) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View promptView = mInflater.inflate(R.layout.custom_toast, null);

        TextView tvTextToast = (TextView) promptView.findViewById(R.id.custom_toast_message);
        ImageView icon = (ImageView) promptView.findViewById(R.id.custom_toast_image);
        tvTextToast.setText(message);

        GradientDrawable bgShape = (GradientDrawable)promptView.getBackground();

        if (status) {
            bgShape.setColor(Color.parseColor("#6dc066"));
            icon.setImageResource(R.drawable.ic_checklist);

        } else {
            bgShape.setColor(Color.parseColor("#e74c3c"));
            icon.setImageResource(R.drawable.ic_error);
        }

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(promptView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public String GenerateGuid(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }
//    public JSONObject callPushDataReturnJson(String link, String txtMethod, String txtJson) {
//        JSONObject _JSONObject = null;
//        //notify("asa","asda","asdas");
//        URL url;
//        HttpURLConnection conn;
//        BufferedReader rd;
//        String line;
//        String result = "";
//        String txtTimeOut= new Mobile_mConfigBL().getValue(enum_mconfig.TimeOut.getValue());
//        String urlToRead=link+"?me="+txtMethod;
//        try {
//            url = new URL(urlToRead);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(Integer.valueOf(txtTimeOut));
//            conn.setRequestProperty("Accept","*/*");
//            String param="txtParam="+txtJson;
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            conn.setFixedLengthStreamingMode(param.getBytes().length);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("charset", "utf-8");
//            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            PrintWriter out = new PrintWriter(conn.getOutputStream());
//            out.print(param);
//            out.close();
//            String response= "";
//            Scanner inStream = new Scanner(conn.getInputStream());
//            while(inStream.hasNextLine())
//            {
//                response+=(inStream.nextLine());
//            }
//            conn.disconnect();
//            result=response;
//            _JSONObject=new JSONObject(result);
//        } catch (IOException e) {
//        } catch (Exception e) {
//        }
//        return _JSONObject;
//    }
//    public JSONObject callPushDataReturnJson(String link,String Method, String strJson, HashMap<String, String> ListOfDataFile) throws IOException {
//        JSONObject _JSONObject = null;
//        String charset = "UTF-8";
//        String UrlApi= "";
//        String txtTimeOut= new Mobile_mConfigBL().getValue(enum_mconfig.TimeOut.getValue());
//        UrlApi=link+Method;
//        MultipartUtility multipart = new MultipartUtility(UrlApi, charset,Integer.valueOf(txtTimeOut));
//        if(ListOfDataFile!= null){
//            for(Map.Entry<String, String> entry : ListOfDataFile.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                multipart.addFilePart(key, new File(value));
//            }
//        }
//        multipart.addFormField("txtParam",strJson);
//        //multipart.addHeaderField("txtParam",strJson);
//        String Result="";
//        List<String> response = multipart.finish();
//        for (String line : response) {
//            Result+=line;
//            System.out.println(line);
//        }
//        try {
//            _JSONObject=new JSONObject(Result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return _JSONObject;
//    }
}
