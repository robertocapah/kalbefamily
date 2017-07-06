package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arick.anjasmara on 22/06/2017.
 */

public class VolleyUtils {
    public void makeJsonObjectRequest(final Activity activity, String strLinkAPI, final String mRequestBody, final VolleyResponseListener listener) {

        final ProgressDialog Dialog = new ProgressDialog(activity);
        Dialog.setCancelable(false);
        Dialog.show();

        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;

                /*if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("validJson");

                        String result = jsonObject2.getString("TxtResult");
                        String txtWarn = jsonObject2.getString("TxtWarn");

                        status = result.equals("1");

                        errorMessage = txtWarn;

                        *//*if (result.equals("1")){
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("TxtData");
                            String txtGUI = jsonObject3.getString("TxtGUI");
                            String txtNameApp = jsonObject3.getString("TxtNameApp");
                            String txtVersion = jsonObject3.getString("TxtVersion");
                            String txtFile = jsonObject3.getString("TxtFile");
                            String bitActive = jsonObject3.getString("BitActive");
                            String txtInsertedBy = jsonObject3.getString("TxtInsertedBy");
//                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String imeiNumber = tm.getDeviceId();
                            clsDeviceInfo data = new clsDeviceInfo();
                            data.setTxtGUI(txtGUI);
                            data.setTxtNameApp(txtNameApp);
                            data.setTxtDevice(android.os.Build.DEVICE);
                            data.setTxtFile(txtFile);
                            data.setTxtVersion(txtVersion);
                            data.setBitActive(bitActive);
                            data.setTxtInsertedBy(txtInsertedBy);
                            data.setIdDevice(imeiNumber);
                            data.setTxtModel(android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL);
                            repo =new clsDeviceInfoRepo(context);
                            int i = 0;
                            try {
                                i = repo.createOrUpdate(data);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if(i > -1)
                            {
                                Log.d("Data info", "Data info berhasil di simpan");
                            }
                        }else{
                            Toast.makeText(context, txtWarn, Toast.LENGTH_SHORT).show();
                        }*//*


//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject explrObject = jsonArray.getJSONObject(i);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                Dialog.dismiss();
                listener.onResponse(response, status, errorMessage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Dialog.dismiss();
                listener.onError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtParam", mRequestBody);
                return params;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(req);
    }

}
