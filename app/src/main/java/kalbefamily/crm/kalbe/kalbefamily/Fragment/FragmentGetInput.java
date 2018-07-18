package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsImageStrukRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;

/**
 * Created by User on 2/26/2018.
 */

public class FragmentGetInput extends Fragment {
    View v;
    Context context;
    List<clsUserMember> dataMember = null;
    List<clsToken> dataToken;
    clsTokenRepo tokenRepo;
    clsImageStrukRepo repoImageStruk;
    private String txtKontakID, access_token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_input_struk_get_data, container, false);
        context = getActivity().getApplicationContext();

        try {
            tokenRepo = new clsTokenRepo(context);
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inputStruk();
        return v;
    }

    private void inputStruk() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(context);
        final clsUserMemberRepo repo = new clsUserMemberRepo(context);
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.refreshData2();
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkGetDataInputStruk;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", txtKontakID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";
        volleyInputStruk(strLinkAPI, mRequestBody, "Mohon Tunggu...", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                new clsActivity().showCustomToast(context, message, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtMessage");
                        String result = jsn.getString("IntResult");

                        if (result.equals("1")) {
                            JSONArray jsonDataInputStruk = jsn.getJSONArray("ListOfObjectData");
                            for(int i=0; i < jsonDataInputStruk.length(); i++) {
                                JSONObject jsonobject = jsonDataInputStruk.getJSONObject(i);
                                String txtKontakID = jsonobject.getString("txtKontakID");
                                String dtInserted = jsonobject.getString("dtInserted");
                                String txtpath = jsonobject.getString("txtPath");
                                String txtFileEdit = jsonobject.getString("txtFileEdit");
                                String txtReason = jsonobject.getString("txtReason");
                                int intActive = jsonobject.getInt("intActive");
                                boolean bitValidate = jsonobject.optBoolean("bitValidate");
                                String txtValidate = String.valueOf(bitValidate);
                                String txtActiveOcr = String.valueOf(intActive);
//                                String txtDtInserted = jsonobject.getString("txtDtInserted");

                                String txtDate = dtInserted.substring(6,19);
                                long timestamp = Long.parseLong(txtDate); //Example -> in ms
                                Date date = new Date(timestamp);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
                                String strDate= formatter.format(date);

                                clsImageStruk data = new clsImageStruk();
                                data.setTxtGuiId(new clsActivity().GenerateGuid());
                                data.setTxtKontakId(txtKontakID);
                                data.setTxtPath(txtpath);
                                data.setTxtValidate(txtValidate);
                                data.setTxtDate(strDate);
                                data.setTxtActiveOcr(txtActiveOcr);
                                data.setTxtFileEdit(txtFileEdit);
                                data.setTxtReason(txtReason);
                                data.setDtDate(date);

                                repoImageStruk = new clsImageStrukRepo(context);
                                repoImageStruk.createOrUpdate(data);
                            }
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            FragmentInputStrukGetData fragmentInputStrukGetData = new FragmentInputStrukGetData();
                            FragmentTransaction fragmentTransactionInputStruk = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransactionInputStruk.replace(R.id.frame, fragmentInputStrukGetData);
                            fragmentTransactionInputStruk.commit();
                        } else {
                            new clsActivity().showCustomToast(context, warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void volleyInputStruk(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog = ProgressDialog.show(getActivity(), "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(context);
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(5);
            client = configDataClient.getTxtDefaultValue().toString();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String finalClient = client;
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
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
//                    new clsActivity().showCustomToast(context, "401, Authorization has been denied for this request", false);
                    new VolleyUtils().requestTokenWithRefresh(getActivity(), strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String accessToken = "";
                                    String newRefreshToken = "";
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
                                    //Toast.makeText(getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
                                    newRefreshToken = refreshToken;
                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
                                        inputStruk();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(context, "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
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

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("Refresh Data ?");
                        pDialog.setContentText("");
                        pDialog.setConfirmText("Refresh");
                        pDialog.setCancelText("Keluar Aplikasi");
                        pDialog.showCancelButton(true);
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                inputStruk();
                                sweetAlertDialog.dismiss();
                            }
                        });
                        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.cancel();
                                getActivity().finish();
                            }
                        });
                        pDialog.show();
                    }
                });
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtParam", mRequestBody);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {
                    tokenRepo = new clsTokenRepo(context);
                    dataToken = (List<clsToken>) tokenRepo.findAll();
                    access_token = dataToken.get(0).getTxtUserToken();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);

                return headers;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(req);
    }
}
