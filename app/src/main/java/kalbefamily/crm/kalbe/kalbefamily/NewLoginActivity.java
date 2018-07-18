package kalbefamily.crm.kalbe.kalbefamily;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ACCOUNT_NAME;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_AUTH_TYPE;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.PARAM_USER_PASS;

public class NewLoginActivity extends AccountAuthenticatorActivity {
    EditText memberID, password;
    String txtMember, txtPassword, access_token;
    Button btnSubmit;
    private final String TAG = this.getClass().getSimpleName();
    private String mAuthTokenType;
    private AccountManager mAccountManager;
    private int intSet = 1;

    clsUserMemberRepo repoUserMember = null;
    clsTokenRepo tokenRepo;
    List<clsToken> dataToken;

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

        mAccountManager = AccountManager.get(getBaseContext());

        Intent mm = getIntent();
        Bundle bbb = mm.getExtras();
        String ccc = bbb.getString(AccountManager.KEY_ACCOUNT_TYPE);

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AUTHTOKEN_TYPE_FULL_ACCESS;

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
//                sDialog.setTitleText("BATAL!")
//                        .setContentText("Anda batal Login :)")
//                        .setConfirmText("OK")
//                        .showCancelButton(false)
//                        .setCancelClickListener(null)
//                        .setConfirmClickListener(null)
//                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                pDialog.dismiss();
            }
        });
        pDialog.show();
    }

    private void userMember() {
        final ProgressDialog Dialog = new ProgressDialog(NewLoginActivity.this);
        txtMember = memberID.getText().toString();
        txtPassword = password.getText().toString();
        String strLinkAPI = new clsHardCode().linkGetDatadMembership;
        JSONObject resJson = new JSONObject();
        try {
            resJson.put("txtMemberID", txtMember);
            resJson.put("txtPassword", txtPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = "[" + resJson.toString() + "]";

//        Intent mm = getIntent();
//        Bundle bbb = mm.getExtras();
//        String ccc = bbb.getString(AccountManager.KEY_ACCOUNT_TYPE);

        final String accountType = getIntent().getExtras().getString(AccountGeneral.ARG_ACCOUNT_TYPE);
        final boolean newAccount = getIntent().getExtras().getBoolean(ARG_IS_ADDING_NEW_ACCOUNT, false);

        final Bundle datum = new Bundle();

        volleyUserMember(strLinkAPI, mRequestBody, "Mohon Tunggu Beberapa Saat...", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
            }
            Intent res = null;
            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtMessage");
                        String result = jsn.getString("IntResult");
                        String accessToken = "dummy_access_token";

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

                                datum.putString(AccountManager.KEY_ACCOUNT_NAME, txtMember);
                                datum.putString(AccountGeneral.ARG_ACCOUNT_TYPE, accountType);
                                datum.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
                                datum.putString(PARAM_USER_PASS, txtPassword);
                                datum.putString(ARG_AUTH_TYPE, mAuthTokenType);
                                datum.putBoolean(ARG_IS_ADDING_NEW_ACCOUNT, newAccount);
                                res = new Intent();
                                res.putExtras(datum);
                                finishLogin(res, mAccountManager);
                            }
                            Toast.makeText(getApplicationContext(), "Your Member ID is valid", Toast.LENGTH_LONG).show();
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

    private void volleyUserMember(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ProgressDialog Dialog = new ProgressDialog(NewLoginActivity.this);
        Dialog = ProgressDialog.show(NewLoginActivity.this, "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(getApplicationContext());
        try {
            tokenRepo = new clsTokenRepo(getApplicationContext());
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
//                    new clsActivity().showCustomToast(getApplicationContext(), "401, Authorization has been denied for this request", false);

                    new VolleyUtils().requestTokenWithRefresh(NewLoginActivity.this, strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                                        userMember();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(getApplicationContext(), "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(NewLoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                        sweetAlertDialog.dismiss();
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
                    tokenRepo = new clsTokenRepo(getApplicationContext());
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
    public void finishLogin(Intent intent, AccountManager mAccountManager) {
        Log.d("kalbe", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountGeneral.ARG_ACCOUNT_TYPE));
        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        String authtokenType = intent.getStringExtra(ARG_AUTH_TYPE);
        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d("kalbe", TAG + "> finishLogin > addAccountExplicitly");
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("kalbe", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
