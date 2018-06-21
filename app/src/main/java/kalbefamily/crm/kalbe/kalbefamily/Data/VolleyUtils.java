package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsMainBL;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsStatusMenuStart;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.FlashActivity;
import kalbefamily.crm.kalbe.kalbefamily.HomeMenu;
import kalbefamily.crm.kalbe.kalbefamily.NewMemberActivity;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.enumStatusMenuStart;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
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
//        Dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(activity, "cancel request", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Dialog.show();
        Dialog = ProgressDialog.show(activity, "",
                progressBarType, false);
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
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        activity.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)); // turn on internet with wifi
//                        activity.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)); // turn on internet with mobile data
                        dialog.dismiss();
                        sweetAlertDialog.dismiss();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE);
                        pDialog.setTitleText("Refresh Data ?");
                        pDialog.setContentText("");
                        pDialog.setConfirmText("Refresh");
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                clsStatusMenuStart _clsStatusMenuStart = new clsMainBL().checkUserActive2(activity.getApplicationContext());
                                if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin) {
                                    Intent intent = new Intent(activity, HomeMenu.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                    pDialog.dismiss();
                                } else {
                                    Intent intent = new Intent(activity, NewMemberActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                    pDialog.dismiss();
                                }
                            }
                        });
                        pDialog.show();
                    }
                });
                dialog.show();
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
                popup();
                finalDialog1.dismiss();
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.show();
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
                if (mRequestBody.getFileUploadProfile().get("profile_picture") != null){
                    params.put("profile_picture.jpg", new DataPart("profile_picture.jpg", mRequestBody.getFileUploadProfile().get("profile_picture"), "image/jpeg"));
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

    public void makeJsonObjectRequestToken(final Activity activity, final String strLinkAPI, final String username, final String password, final String clientId, final String progressBarType, final VolleyResponseListener listener) {
        final ProgressDialog Dialog = new ProgressDialog(activity);
        Dialog.setMessage(progressBarType);
        Dialog.setCancelable(false);
        Dialog.show();

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
                String body, message;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        JSONObject jsonObject = new JSONObject(body);
                        message = jsonObject.getString("Message");
                        Toast.makeText(activity.getApplicationContext(), "Error 401 " + message, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finalDialog1.dismiss();
                    if (error.getMessage() != null) {
                        listener.onError(error.getMessage());
                    }
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                    try {

                        if (activity.getClass().getSimpleName().equals(activity.getString(R.string.classname_PickAccountActivity))){
//                           popup();
                            String username = "";
                            String clientId = "";
                            String strLinkAPI = new clsHardCode().linkToken;

                            mConfigRepo configRepo = new mConfigRepo(activity);
                            try {
                                mConfigData configDataClient = (mConfigData) configRepo.findById(5);
                                mConfigData configDataUser = (mConfigData) configRepo.findById(6);
                                username = configDataUser.getTxtDefaultValue().toString();
                                clientId = configDataClient.getTxtDefaultValue().toString();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            makeJsonObjectRequestToken(activity,strLinkAPI,username,"",clientId,"Please Wait...",listener);
                        } else {
                            body = new String(error.networkResponse.data,"UTF-8");
                            JSONObject jsonObject = new JSONObject(body);
                            message = jsonObject.getString("error_description");
                            Toast.makeText(activity.getApplicationContext(), "Error 400, " + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finalDialog1.dismiss();
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    Toast.makeText(activity.getApplicationContext(), "Error 500, Server Error", Toast.LENGTH_SHORT).show();
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

                builder.setTitle("Request Time Out");
                builder.setMessage("Please check your connection");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", username);
                params.put("password", password);
                params.put("client_id", clientId);
                return params;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(req);
    }

    public void requestTokenWithRefresh(final Activity activity, String strLinkAPI, final String refreshToken, final String clientId, final VolleyResponseListener listener) {
        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body, message;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(activity.getApplicationContext(), "Error 401, Authorization has been denied for this request", Toast.LENGTH_SHORT).show();
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        JSONObject jsonObject = new JSONObject(body);
                        message = jsonObject.optString("error_description");
                        if (message.equals("")) {
                            popup();
                            message = jsonObject.optString("error");
                            //Toast.makeText(activity.getApplication(), "Error 400, " + message + ". Silahkan Login kembali", Toast.LENGTH_SHORT).show();
                        }
//                        else if (activity.getClass().getSimpleName().equals(activity.getString(R.string.classname_PickAccountActivity))){
//                           popup();
//                        }
                        else {
                            Toast.makeText(activity .getApplicationContext(), "Error 400, " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    Toast.makeText(activity.getApplicationContext(), "Error 500, Server Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Something Error, please request again", Toast.LENGTH_SHORT).show();
                }
            }

            public void popup() {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

                builder.setTitle("Request Time Out");
//                builder.setMessage("Silahkan logout dan login kembali");
                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, FlashActivity.class);
                        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
                        helper.clearAllDataInDatabase();
                        activity.finish();
                        activity.startActivity(intent);
                    }
                });

//                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        activity.finish();
//                        dialog.dismiss();
//                    }
//                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "refresh_token");
                params.put("client_id", clientId);
                params.put("refresh_token", refreshToken);
                return params;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(req);
    }

    String clientId = "";
    public void requestToken(final Activity activity){
        String a = activity.getClass().getSimpleName().toString();
        String username = "";
        String strLinkAPI = new clsHardCode().linkToken;

        mConfigRepo configRepo = new mConfigRepo(activity.getApplicationContext());
        final clsTokenRepo tokenRepo = new clsTokenRepo(activity.getApplicationContext());
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(4);
            mConfigData configDataUser = (mConfigData) configRepo.findById(5);
            username = configDataUser.getTxtDefaultValue().toString();
            clientId = configDataClient.getTxtDefaultValue().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new VolleyUtils().makeJsonObjectRequestToken(activity, strLinkAPI, username, "", clientId, "Request Token, Please Wait", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        String accessToken = "";
                        String refreshToken = "";
                        JSONObject jsonObject = new JSONObject(response);
                        accessToken = jsonObject.getString("access_token");
                        refreshToken = jsonObject.getString("refresh_token");
                        String dtIssued = jsonObject.getString(".issued");

                        clsToken data = new clsToken();
                        data.setIntId("1");
                        data.setDtIssuedToken(dtIssued);
                        data.setTxtUserToken(accessToken);
                        data.setTxtRefreshToken(refreshToken);

                        tokenRepo.createOrUpdate(data);
                        Log.d("Data info", "get access_token & refresh_token, Success");

                        Toast.makeText(activity.getApplicationContext(), "Ready For Login", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
