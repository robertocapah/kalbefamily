package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

public class NewLoginActivity extends AppCompatActivity {
    EditText memberID, password;
    String txtMember, txtPassword;
    Button btnSubmit;

    private int intSet = 1;

    clsUserMemberRepo repoUserMember = null;

    public void onBackPressed() {
        Intent intent = new Intent(NewLoginActivity.this, NewMemberActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }

        setContentView(R.layout.activity_new_login);
//        popup();

        memberID = (EditText) findViewById(R.id.memberID);
        password = (EditText) findViewById(R.id.password);
        btnSubmit = (Button) findViewById(R.id.button);

        memberID.setText(getIntent().getStringExtra("memberID"));
        memberID.setEnabled(false);

        password.requestFocus();
        password.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(password) {
            public boolean onDrawableClick() {
                if (intSet == 1) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    intSet = 0;
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    intSet = 1;
                }

                return true;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")){
                    new clsActivity().showCustomToast(getApplicationContext(), "Password tidak boleh kosong !", false);
                } else {
                    popupSubmit();
                }
            }
        });
    }

    private void popup() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Oops...")
                .setContentText("Silahkan isi password terlebih dahulu")
                .show();
    }

    private void popupSubmit() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Apakah Anda yakin ?");
        pDialog.setCancelText("BATAL");
        pDialog.setConfirmText("SUBMIT");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                userMember();
                pDialog.dismiss();
            }
        });
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                // reuse previous dialog instance, keep widget user state, reset them if you need
                sDialog.setTitleText("BATAL!")
                        .setContentText("Anda batal Login :)")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
        pDialog.show();
    }

    private void userMember() {
        final ProgressDialog Dialog = new ProgressDialog(NewLoginActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        txtMember = memberID.getText().toString();
        txtPassword = password.getText().toString();
        String strLinkAPI = new clsHardCode().linkGetDatadMembership;
        JSONObject resJson = new JSONObject();
        List<clsmVersionApp> dataInfoVersion = null;
        List<clsDeviceInfoData> dataInfo = null;

        try {
            resJson.put("txtMemberID", txtMember);
            resJson.put("txtPassword", txtPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        new VolleyUtils().makeJsonObjectRequest(NewLoginActivity.this, strLinkAPI, mRequestBody, "Mohon Tunggu Beberapa Saat...", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
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
                            JSONArray jsonDataUserMember = jsn.getJSONArray("ListOfObjectData");
                            for(int i=0; i < jsonDataUserMember.length(); i++) {
                                JSONObject jsonobject = jsonDataUserMember.getJSONObject(i);
                                String txtKontakId = jsonobject.getString("TxtKontakId");
                                String txtMemberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("txtNamaKartu");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaKeluarga = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");
                                String txtBasePoint = jsonobject.getString("IntBasePoin");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(txtMemberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtBasePoin(txtBasePoint);
                                dataUser.setTxtNamaBelakang(txtNamaKeluarga);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);

                                repoUserMember = new clsUserMemberRepo(getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
                                Log.d("Data info", "Data member valid");
                            }
//                            Toast.makeText(getApplicationContext(), "Your Member ID is valid", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewLoginActivity.this, HomeMenu.class);
                            finish();
                            startActivity(intent);
                        } else {
                            new clsActivity().showCustomToast(getApplicationContext(), warn + ", Password Anda salah ", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
