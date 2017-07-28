package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsQRCodeData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Common.dataJson;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsQRCodeRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 6/22/2017.
 */

public class clsHelper {
    private static final String TAG = "MainActivity";
    clsHardCode _path = new clsHardCode();
    public void createDatabase(Context context) throws IOException {
        final String CURRENT_DATABASE_PATH = "data/data/" + context.getPackageName() + "/databases/" + _path.dbName;

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(CURRENT_DATABASE_PATH);

        // Path to the just created empty db
        String outFileName = "/data/data/KalbeFamily.app.kalbenutritionals.KalbeFamily/databases/" + _path.dbName;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public clsSendData sendData(String versionName, Context context){
        clsSendData dtclsSendData = new clsSendData();
        dataJson dtSend = new dataJson();
        HashMap<String, byte[]> FileUpload = null;
        clsUserMemberRepo _ClsUserMemberRepo = new clsUserMemberRepo(context);
        clsUserMemberImageRepo _ClsUserMemberImageRepo = new clsUserMemberImageRepo(context);
        List<clsUserMember> ListOfUserMember = _ClsUserMemberRepo.getAllDataToSendData(context);
        List<clsUserMemberImage> ListOfUserMemberImage = _ClsUserMemberImageRepo.getAllDataToSendData(context);
        FileUpload = new HashMap<String, byte[]>();
        if (ListOfUserMemberImage != null) {
            dtSend.setListDataUserMemberImage(ListOfUserMemberImage);
            for (clsUserMemberImage dttUserMemberImage : ListOfUserMemberImage) {
                if (dttUserMemberImage.getTxtImg() != null) {
                    if (dttUserMemberImage.getTxtPosition().equals("txtFileName1")) {
                        FileUpload.put("txtFileName1", dttUserMemberImage.getTxtImg());
                    }
                    if (dttUserMemberImage.getTxtPosition().equals("txtFileName2")) {
                        FileUpload.put("txtFileName2", dttUserMemberImage.getTxtImg());
                    }
                }
            }
        }
        if (ListOfUserMember != null) {
            dtSend.setListDataUserMember(ListOfUserMember);
        }

        dtclsSendData.setDtdataJson(dtSend);
        dtclsSendData.setFileUpload(FileUpload);
        return dtclsSendData;
    }

    public clsSendData sendDataQRCode(String versionName, Context context){
        clsSendData dtclsSendData = new clsSendData();
        dataJson dtSend = new dataJson();
        HashMap<String, byte[]> FileUpload = null;
        clsQRCodeRepo _ClsQRCodeRepo = new clsQRCodeRepo(context);
        List<clsQRCodeData> ListOfClsQRCodeData = _ClsQRCodeRepo.getAllDataToSendData(context);

        if (ListOfClsQRCodeData != null) {
            dtSend.setLisClsQRCodeData(ListOfClsQRCodeData);
        }

        dtclsSendData.setDtdataJson(dtSend);
        return dtclsSendData;
    }

    public String volleyImplement(final Context context, final String mRequestBody, String strLinkAPI, Activity activity){
        RequestQueue queue = Volley.newRequestQueue(context);
        final String[] ret = {null};
        final ProgressDialog Dialog = new ProgressDialog(activity);
        Dialog.show();
//        JSONObject obj = null;
        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (response != null){
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("validJson");

                        String result = jsonObject2.getString("TxtResult");
                        String txtWarn = jsonObject2.getString("TxtWarn");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    ret[0] = response;
                    Dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("txtParam",mRequestBody);
                return params;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
        return ret[0];
    }
}
