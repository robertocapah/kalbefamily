package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpStatus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.addons.volley.VolleyMultipartRequest;

import static android.provider.Telephony.Carriers.PASSWORD;

/**
 * Created by arick.anjasmara on 22/06/2017.
 */

public class VolleyUtils {
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makeJsonObjectRequest(final Activity activity, String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {

        ProgressDialog Dialog = new ProgressDialog(activity);
//        Dialog.setCancelable(false);
//        Dialog.show();

        Dialog = ProgressDialog.show(activity, "",
                progressBarType, true);
//        Dialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.mipmap.ic_kalbe_2, null));
        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    new clsActivity().showCustomToast(activity.getApplicationContext(), "401", false);
                    finalDialog1.dismiss();
                    if (error.getMessage() != null) {
                        listener.onError(error.getMessage());
                    }
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            private void popup() {
                new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Mohon check kembali koneksi internet anda")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                activity.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                finalDialog1.dismiss();
//                listener.onError(error.getMessage());
//            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtParam", mRequestBody);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s","test","test");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
                String credentials = "test" + ":" + "test";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(req);
    }
    public void makeJsonObjectRequestSendData(final Activity activity, String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        ProgressDialog Dialog = new ProgressDialog(activity);
//        Dialog.setCancelable(false);
//        Dialog.show();

        Dialog = ProgressDialog.show(activity, "",
                "Mohon Tunggu...", true);
//        Dialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.mipmap.ic_kalbe_2, null));
        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                finalDialog1.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSON().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (mRequestBody.getFileUpload().get("txtFileName1") != null){
                    params.put("file_image1.jpg", new DataPart("file_image1.jpg", mRequestBody.getFileUpload().get("txtFileName1"), "image/jpeg"));
                }
                if (mRequestBody.getFileUpload().get("txtFileName2") != null){
                    params.put("file_image2.jpg", new DataPart("file_image2.jpg", mRequestBody.getFileUpload().get("txtFileName2"), "image/jpeg"));
                }
                if (mRequestBody.getFileUpload().get("profile_picture") != null){
                    params.put("profile_picture.jpg", new DataPart("profile_picture.jpg", mRequestBody.getFileUpload().get("profile_picture"), "image/jpeg"));
                }

                return params;
            }
        };
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(multipartRequest);
    }

    public void makeJsonObjectRequestSendDataMediaKontak(final Activity activity, String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        ProgressDialog Dialog = new ProgressDialog(activity);
//        Dialog.setCancelable(false);
//        Dialog.show();

        Dialog = ProgressDialog.show(activity, "",
                "Mohon Tunggu...", true);
//        Dialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.mipmap.ic_kalbe_2, null));
        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                finalDialog1.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSONmediaKontak().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(multipartRequest);
    }

    public void makeJsonObjectRequestSendDataQRCode(final Context ctx, String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSONqrCode().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(ctx.getApplicationContext());
        queue.add(multipartRequest);
    }

}
