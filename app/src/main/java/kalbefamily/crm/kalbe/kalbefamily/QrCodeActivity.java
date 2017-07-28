package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.qrcode.encoder.QRCode;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsQRCodeData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsQRCodeRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

import static com.android.volley.VolleyLog.TAG;

public class QrCodeActivity extends AppCompatActivity {

    TextView statusQRCode;
    Button btnScan;
    private ZXingLibConfig zxingLibConfig;
    private Toolbar toolbar;
    clsUserMemberRepo repoUserMember = null;
    clsQRCodeRepo qrCodeRepo = null;
    List<clsUserMember> dataMember = null;
    List<clsQRCodeData> dataQRCode = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        toolbar = (Toolbar) findViewById(R.id.toolbarCode);
        toolbar.setTitle("Scan QR-Code");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        statusQRCode = (TextView) findViewById(R.id.statusQRCode);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
                finish();
                startActivity(intent);
            }
        });

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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

            qrCodeRepo = new clsQRCodeRepo(getApplicationContext());

            int h = 0;
            h = qrCodeRepo.createOrUpdate(qrCodeData);
            if(h > -1) {
                Log.d("Data info", "Scanning Success");
//                                    status = true;
            }

            sendData();
            statusQRCode.setText("Scan Success");
        }
    }

    private void sendData() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendDataQRCode(versionName, getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = "http://10.171.10.27/WebApi2/KF/ScanQRCode";
                final String mRequestBody = "[" + dtJson.toString() + "]";

                new VolleyUtils().makeJsonObjectRequestSendDataQRCode(getApplicationContext(), strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
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
}
