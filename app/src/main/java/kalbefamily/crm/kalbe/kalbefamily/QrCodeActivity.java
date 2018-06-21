package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.zxing.qrcode.encoder.QRCode;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsQRCodeData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsQRCodeRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
import kalbefamily.crm.kalbe.kalbefamily.addons.volley.VolleyMultipartRequest;

import static com.android.volley.VolleyLog.TAG;

public class QrCodeActivity extends AppCompatActivity {

    TextView statusQRCode;
    Button btnScan;
    private ZXingLibConfig zxingLibConfig;
    private Toolbar toolbar;
    clsUserMemberRepo repoUserMember = null;
    clsQRCodeRepo qrCodeRepo = null;
    clsTokenRepo tokenRepo;
    List<clsToken> dataToken;
    List<clsUserMember> dataMember = null;
    List<clsQRCodeData> dataQRCode = null;
    Context context;
    String access_token;

    @Override
    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Exit");
//        builder.setMessage("Do you want to exit?");
//
//        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();

        Intent intent = new Intent(this, HomeMenu.class);
        finish();
        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }
        setContentView(R.layout.activity_qr_code);

        toolbar = (Toolbar) findViewById(R.id.toolbarCode);
        toolbar.setTitle("Scan QR-Code");
        setSupportActionBar(toolbar);

        // set enable toolbar button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        statusQRCode = (TextView) findViewById(R.id.statusQRCode);

        // toolbar button for move to before screen
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
                finish();
                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        try {
            repoUserMember = new clsUserMemberRepo(context);
            tokenRepo = new clsTokenRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnScan = (Button) findViewById(R.id.buttonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.initiateScan(QrCodeActivity.this, zxingLibConfig);
            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean status = false;

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult.getContents() == null && scanResult.getFormatName() == null) {
                return;
            }
            final String result = scanResult.getContents();
//            if (result != null) {
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//            }
            clsQRCodeData qrCodeData = new clsQRCodeData();
            qrCodeData.setIntQRCodeID(result);
            qrCodeData.setTxtKontakID(dataMember.get(0).txtKontakId.toString());
            qrCodeData.setTxtMemberID(dataMember.get(0).txtMemberId.toString());

            qrCodeRepo = new clsQRCodeRepo(getApplicationContext());

            int h = 0;
            h = qrCodeRepo.createOrUpdate(qrCodeData);
            if(h > -1) {
                Log.d("Data info", "Scanning Success");
//                                    status = true;
            }

            sendData();
        }
    }

    private void sendData() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendDataQRCode(versionName, getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkScanQRCode;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendDataQRCode(strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), "Messeage : " + message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        String res = response;
                        Log.i(TAG, "Ski data from server - " + response);

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("validJson");
                            String txtWarn = jsonObject2.getString("TxtMessage");
                            Toast.makeText(getApplicationContext(), "Messeage : "+txtWarn, Toast.LENGTH_LONG).show();
                            if (txtWarn.equals("Scan Not Valid")) {
                                statusQRCode.setText("Scan Not Valid");
                            } else {
                                statusQRCode.setText("Scan Success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void volleyRequestSendDataQRCode(String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ProgressDialog Dialog = new ProgressDialog(QrCodeActivity.this);

        Dialog = ProgressDialog.show(QrCodeActivity.this, "", "Mohon Tunggu...", true);
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
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {

                    new VolleyUtils().requestTokenWithRefresh(QrCodeActivity.this, strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
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
                                        sendData();
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
                    new clsActivity().showCustomToast(context, "Scan Gagal, Mohon check kembali koneksi internet anda", false);
                    finalDialog1.dismiss();
                }
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
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(multipartRequest);
    }
}
