package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.BL.tdeviceBL;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsWarning;
import kalbefamily.crm.kalbe.kalbefamily.Common.tdeviceData;

public class LoginActivity extends clsActivity {
    private EditText txtMemberID;
    private Button btnMember;
    private clsWarning _clsWarning;
    private GoogleApiClient client;

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnMember=(Button) findViewById(R.id.btnMember);
        txtMemberID=(EditText) findViewById(R.id.txtMemberID);
        btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncCallLogin task=new AsyncCallLogin();
                task.execute();
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Button btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private class AsyncCallLogin extends AsyncTask<JSONObject, Void, JSONObject> {
        @SuppressWarnings("WrongThread")
        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject LoginData = null;
            try {
                LoginData = new JSONObject();
                tdeviceData _tdeviceData=new tdeviceBL().getInfoDevice();
                JSONObject JsonParam=new JSONObject();
                String txtVersion= txtVersionApp();
                JsonParam.put("txtDevice",_tdeviceData.txtDevice.toString());
                JsonParam.put("txtModel",_tdeviceData.txtModel.toString());
                JsonParam.put("txtGUIDeviceId",_tdeviceData.txtGUIDeviceId.toString());
                JsonParam.put("txtVersion",_tdeviceData.txtVersion.toString());
                JsonParam.put("txtParam",txtMemberID.getText().toString());
                LoginData=PushData("GetDetailKontak",JsonParam.toString());

                //EditText txt = (EditText) findViewById(R.id.txtLoginEmail);
                //roledata = new mUserRoleBL().getRoleAndOutlet(txtEmail1, pInfo.versionName);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                showToastWarning(getApplicationContext() ,e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showToastWarning(getApplicationContext()
                        ,e.getMessage());
            }

            return LoginData;
        }
        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPostExecute(JSONObject JsonRes) {
            if(new clsActivity().ValidJSON(JsonRes)){
                try {
                    String txtvalidJson=(String.valueOf(JsonRes.get("validJson")));
                    JSONObject validJson=new JSONObject(txtvalidJson);
                    String intresult=(String.valueOf(validJson.get("TxtResult")));
                    if(intresult.equals("1")){
                        String txtGetDataJson = String.valueOf(validJson.get("TxtData"));
                        JSONObject JsonData=new JSONObject(txtGetDataJson);
                        String txtJSONUserPegawai = String.valueOf(JsonData.get("UserPegawai"));
                        JSONObject JSONUserPegawai=new JSONObject(txtJSONUserPegawai);
                        String txtJSONUserJabatan = String.valueOf(JsonData.get("UserJabatan"));
                        JSONArray JSONUserJabatanArray=new JSONArray(txtJSONUserJabatan);

                        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
                        View promptView = layoutInflater.inflate(R.layout.listmember, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alertDialogBuilder.setView(promptView);
                        final TextView  _tvName=new TextView(LoginActivity.this);
                        _tvName.setText("asdasd");
                        final LinearLayout llMemberID=(LinearLayout) promptView.findViewById(R.id.llMemberID);
                        llMemberID.addView(_tvName);
                    }else{
                        String txtvalidJsonError=(String.valueOf(JsonRes.get("validJson")));
                        JSONObject validJsonError=new JSONObject(txtvalidJsonError);
                        String txtWarn=(String) validJsonError.get("TxtWarn");
                        new clsActivity().showToastWarning(LoginActivity.this,txtWarn);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new clsActivity().showToastWarning(LoginActivity.this,e.getMessage());
                }


            }
            Dialog.dismiss();
        }



        int intProcesscancel = 0;

        @Override
        protected void onPreExecute() {
            //Make ProgressBar invisible
            //pg.setVisibility(View.VISIBLE);
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Dialog.dismiss();
        }

    }
}
