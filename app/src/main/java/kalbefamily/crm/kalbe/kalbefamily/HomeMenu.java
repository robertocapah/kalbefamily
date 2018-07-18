package kalbefamily.crm.kalbe.kalbefamily;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
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
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Fragment.FragmentGetInput;
import kalbefamily.crm.kalbe.kalbefamily.Fragment.FragmentGetPoint;
import kalbefamily.crm.kalbe.kalbefamily.Fragment.FragmentInfoContact;
import kalbefamily.crm.kalbe.kalbefamily.Fragment.FragmentNewPersonalData;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAvailablePoinRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;


/**
 * Created by Rian Andrivani on 7/19/2017.
 */

public class HomeMenu extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView tvUsername, tvEmail;
    CircleImageView ivProfile;
    PackageInfo pInfo = null;
    int selectedId;
    String linkImageProfile = "null";
    String txtLinkDesc, access_token, linkImageStruk;
    Boolean isSubMenu = false;
    clsTokenRepo tokenRepo;
    List<clsToken> dataToken;
    List<clsUserMember> dataMember = null;

    private GoogleApiClient client;
    clsActivity _clsMainActivity = new clsActivity();
    private ZXingLibConfig zxingLibConfig;
    DatabaseHelper helper = DatabaseManager.getInstance().getHelper();

    private String txtKontakID;
    private String txtMember;
    clsUserMemberRepo repoUserMember = null;
    clsUserMemberImageRepo imageRepo = null;
    clsAvailablePoinRepo repoAvailablePoin;
    clsUserImageProfileRepo repoUserImageProfile = null;
    List<clsUserImageProfile> dataUserImageProfile = null;
    private static Bitmap mybitmapImageProfile;
    private Context context;


//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Keluar");
//        builder.setMessage("Apakah Anda ingin keluar?");
//
//        builder.setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//
//        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        boolean isHome = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment.toString().contains("FragmentInfoContact") && getSupportFragmentManager().getFragments().size() == 1) {
                isHome = true;

            } else if (fragment != null && fragment.toString().contains("FragmentInfoContact") && getSupportFragmentManager().getFragments().size() > 1) {
                if (fragment.isVisible()) {
                    isHome = true;
                }
            } else if (fragment != null && !fragment.toString().contains("FragmentInfoContact") && getSupportFragmentManager().getFragments().size() > 1) {
                isHome = false;
            }
        }
        if (!isHome) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

            toolbar.setTitle("Home");
            FragmentInfoContact homeFragment = new FragmentInfoContact();
            FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
            fragmentTransactionHome.replace(R.id.frame, homeFragment);
            fragmentTransactionHome.commit();

//            navigationView.getMenu().getItem(0).setChecked(true);
        } else if (isHome) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Keluar");
            builder.setMessage("Apakah Anda ingin keluar?");

            builder.setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main_contact);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        try {
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserMember();

//        FragmentInfoContact ContactFragment = new FragmentInfoContact();
//        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
//        fragmentTransactionHome.replace(R.id.frame, ContactFragment);
//        fragmentTransactionHome.commit();
//        selectedId = 99;

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View vwHeader = navigationView.getHeaderView(0);
        ivProfile = (CircleImageView) vwHeader.findViewById(R.id.profile_image);
        tvUsername = (TextView) vwHeader.findViewById(R.id.username);
        tvEmail = (TextView) vwHeader.findViewById(R.id.email);



        Menu header = navigationView.getMenu();
        SubMenu subMenuVersion = header.addSubMenu(R.id.groupVersion, 0, 3, "Version");
        try {
            subMenuVersion.add(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " \u00a9 KN-IT").setIcon(R.drawable.ic_android).setEnabled(false);
            subMenuVersion.add(new mConfigRepo(context).API).setIcon(R.drawable.ic_action_link2).setEnabled(false);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.logoutContact:
                        LayoutInflater layoutInflater = LayoutInflater.from(HomeMenu.this);
                        final View promptView = layoutInflater.inflate(R.layout.confirm_data, null);

                        final TextView _tvConfirm = (TextView) promptView.findViewById(R.id.tvTitle);
                        final TextView _tvDesc = (TextView) promptView.findViewById(R.id.tvDesc);
                        _tvDesc.setVisibility(View.INVISIBLE);
                        _tvConfirm.setText("Log Out dari Aplikasi ?");
                        _tvConfirm.setTextSize(18);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeMenu.this);
                        alertDialogBuilder.setView(promptView);
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final ProgressDialog dialog2 = new ProgressDialog(HomeMenu.this, ProgressDialog.STYLE_SPINNER);
                                        dialog2.setIndeterminate(true);
                                        dialog2.setMessage("Logging out...");
                                        dialog2.setCancelable(false);
                                        dialog2.show();

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        // On complete call either onLoginSuccess or onLoginFailed
                                                        logout();
                                                        // onLoginFailed();
                                                        dialog2.dismiss();
                                                    }
                                                }, 3000);
//                                        new clsActivity().showCustomToast(getApplicationContext(), "Logout, Success", true);
//                                        Toast.makeText(getApplicationContext(), "Logout, Success", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alertD = alertDialogBuilder.create();
                        alertD.show();
                        return true;
                    case R.id.contact:
                        toolbar.setTitle("Home");

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        FragmentInfoContact ContactFragment = new FragmentInfoContact();
                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHome.replace(R.id.frame, ContactFragment);
                        fragmentTransactionHome.commit();
                        selectedId = 99;

                        return true;
                    case R.id.personalData:
                        toolbar.setTitle("Personal data");

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        // send data from activity to fragment like intent
                        Bundle bundle=new Bundle();
                        bundle.putString("imageProfile", linkImageProfile);
                        helper.refreshData2();

                        FragmentNewPersonalData fragmentPersonalData = new FragmentNewPersonalData();
                        FragmentTransaction fragmentTransactionPersonalData = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionPersonalData.replace(R.id.frame, fragmentPersonalData);
                        fragmentTransactionPersonalData.commit();
                        fragmentPersonalData.setArguments(bundle);
                        selectedId = 99;

                        return true;
                    case R.id.scanQrCode:
//                        IntentIntegrator.initiateScan(HomeMenu.this, zxingLibConfig);

                        Intent intent = new Intent(getApplicationContext(), QrCodeActivity.class);
                        finish();
                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                        return true;
                    case R.id.rewardCard:
//                        toolbar.setTitle("Reward Card");
//
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//                        FragmentRewardCard fragmentRewardCard = new FragmentRewardCard();
//                        FragmentTransaction fragmentTransactionRewardCard= getSupportFragmentManager().beginTransaction();
//                        fragmentTransactionRewardCard.replace(R.id.frame, fragmentRewardCard);
//                        fragmentTransactionRewardCard.commit();
//                        selectedId = 99;

                        Intent intentReward = new Intent(getApplicationContext(), RewardCardActivity.class);
                        intentReward.putExtra("link", txtLinkDesc);
                        finish();
                        startActivity(intentReward);

                        return true;
                    case R.id.availablePont:
//                        availablePoin();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        toolbar.setTitle("Available Point Customer");
                        FragmentGetPoint fragmentAvailablePoin = new FragmentGetPoint();
                        FragmentTransaction fragmentTransactionAvailablePoint = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionAvailablePoint.replace(R.id.frame, fragmentAvailablePoin);
                        fragmentTransactionAvailablePoint.commit();
                        selectedId = 99;

                        return true;

                    case R.id.inputStruk:
//                        availablePoin();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        Bundle bundle2=new Bundle();
//                        bundle2.putString("imageStruk", linkImageStruk);

                        toolbar.setTitle("Input Struk");
                        FragmentGetInput fragmentInputStruk = new FragmentGetInput();
                        FragmentTransaction fragmentTransactionInputStruk = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionInputStruk.replace(R.id.frame, fragmentInputStruk);
                        fragmentTransactionInputStruk.commit();
                        selectedId = 99;

                        return true;

                }
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        toolbar.setTitle(item.getTitle());

        Class<?> fragmentClass;
        try {
            Fragment fragment;

            fragmentClass = Class.forName("kalbefamily.crm.kalbe.kalbefamily" + String.valueOf(item.getTitle()).replaceAll("\\s+", ""));
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();

            selectedId=id;

            if (!isSubMenu) isSubMenu = true;
            else isSubMenu = false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    public void UserMember() {
        final ProgressDialog Dialog = new ProgressDialog(HomeMenu.this);
        clsUserMemberRepo repo = new clsUserMemberRepo(getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtMember = dataMember.get(0).getTxtMemberId();
        String strLinkAPI = new clsHardCode().linkGetDetailCustomer;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtMemberId", txtMember);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyUserMember(strLinkAPI, mRequestBody, "Refresh Data...", new VolleyResponseListener() {
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
                                String memberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("TxtNama");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaDepan = jsonobject.getString("TxtNamaDepan");
                                String txtNamaBelakang = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");
                                String intBasePoin = jsonobject.getString("IntBasePoin");
                                String txtTglAwal = jsonobject.getString("DtTglAwal");
                                String txtTglBerlaku = jsonobject.getString("TxttglBerlaku");
                                txtLinkDesc = jsonobject.getString("TxtLinkDesc");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(memberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtNamaDepan(txtNamaDepan);
                                dataUser.setTxtNamaBelakang(txtNamaBelakang);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);
                                dataUser.setTxtBasePoin(intBasePoin);
                                dataUser.setTxtTglAwal(txtTglAwal);
                                dataUser.setTxtTglBerlaku(txtTglBerlaku);

                                repoUserMember = new clsUserMemberRepo(getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
//
//                                int h = 0;
//                                h = repoUserMember.createOrUpdate(dataUser);
//                                if(h > -1) {
//                                    Log.d("Data info", "Data Member berhasil di update");
////                                    status = true;
//                                }

                                String listtkontakImageProfile = jsonobject.getString("ListtkontakImageProfile");
                                if (listtkontakImageProfile != "null") {
                                    JSONArray jsonDataUserMemberImageProfile = jsonobject.getJSONArray("ListtkontakImageProfile");
                                    for (int j = 0; j < jsonDataUserMemberImageProfile.length(); j++) {
                                        JSONObject jsonobjectImage = jsonDataUserMemberImageProfile.getJSONObject(j);
//                                        String txtGuiID = jsonobjectImage.getString("TxtDataID");
//                                        String txtKontakIDImage = jsonobjectImage.getString("TxtKontakID");
//                                        String txtImageName = jsonobjectImage.getString("TxtImageName");
//                                        String txtType = jsonobjectImage.getString("TxtType");
//
//                                        clsUserImageProfile imageProfile = new clsUserImageProfile();
//
//                                        try {
//                                            repoUserImageProfile = new clsUserImageProfileRepo(getApplicationContext());
//                                            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
//                                        } catch (SQLException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        if (dataUserImageProfile.size() == 0) {
//                                            imageProfile.setTxtGuiId(txtGuiID);
//                                        } else {
//                                            imageProfile.setTxtGuiId(dataUserImageProfile.get(0).txtGuiId);
//                                        }
//                                        imageProfile.setTxtKontakId(txtKontakIDImage);

                                        String url = String.valueOf(jsonobjectImage.get("TxtPath"));
                                        linkImageStruk = jsonobject.optString("TxtImageStruk");
                                        if (!url.equals("null")) {
//                                            byte[] logoImage = getLogoImage(url);
//                                            imageProfile.setTxtImg(logoImage);
//
//                                            repoUserImageProfile = new clsUserImageProfileRepo(getApplicationContext());
//
//                                            int k = 0;
//                                            k = repoUserImageProfile.createOrUpdate(imageProfile);
//                                            if(k > -1) {
//                                                Log.d("Data info", "Data Member Image profile berhasil di update");
////                                    status = true;
//                                            }
                                            linkImageProfile = url;
                                        }
                                    }
                                }

                            }
//                            new clsActivity().showCustomToast(context.getApplicationContext(), "Update Data, Success", true);
                        } else {
                            new clsActivity().showCustomToast(getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clsUserMemberRepo repo = new clsUserMemberRepo(getApplicationContext());
                    try {
                        dataMember = (List<clsUserMember>) repo.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    tvUsername.setText(_clsMainActivity.greetings() + dataMember.get(0).getTxtNama());
                    tvEmail.setText(dataMember.get(0).getTxtMemberId().toString());

                    try {
                        repoUserImageProfile = new clsUserImageProfileRepo(getApplicationContext());
                        dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (dataUserImageProfile.size() > 0) {
//                        viewImageProfile();
                        helper.clear();
                    }
                    if (!linkImageProfile.equals("null")) {
                            Picasso.with(getApplicationContext()).load(linkImageProfile)
                                    .placeholder(R.drawable.loading2)
                                    .error(R.drawable.profile)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                    .fit()
                                    .into(ivProfile);
                    }

                    FragmentInfoContact ContactFragment = new FragmentInfoContact();
                    FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionHome.replace(R.id.frame, ContactFragment);
                    fragmentTransactionHome.commit();
                    selectedId = 99;
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }

    private void volleyUserMember(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ProgressDialog Dialog = new ProgressDialog(HomeMenu.this);
        Dialog = ProgressDialog.show(HomeMenu.this, "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(getApplicationContext());
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
                    //new clsActivity().showCustomToast(getApplicationContext(), "401, Authorization has been denied for this request", false);

                    new VolleyUtils().requestTokenWithRefresh(HomeMenu.this, strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
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
                                        UserMember();
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
                final SweetAlertDialog dialog = new SweetAlertDialog(HomeMenu.this, SweetAlertDialog.WARNING_TYPE);
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

                        final SweetAlertDialog pDialog = new SweetAlertDialog(HomeMenu.this, SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("Refresh Data ?");
                        pDialog.setContentText("");
                        pDialog.setConfirmText("Refresh");
                        pDialog.setCancelText("Keluar Aplikasi");
                        pDialog.showCancelButton(true);
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                UserMember();
                                sweetAlertDialog.dismiss();
                            }
                        });
                        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.cancel();
                                finish();
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

    private byte[] getLogoImage(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            String contentType = ucon.getHeaderField("Content-Type");
            boolean image = contentType.startsWith("image/");

            if (image) {
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                ByteArrayBuffer baf = new ByteArrayBuffer(500);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                return baf.toByteArray();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    private void viewImageProfile() {
        try {
            repoUserImageProfile = new clsUserImageProfileRepo(getApplicationContext());
            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/Foto_Profil");
//        folder.mkdir();

        for (clsUserImageProfile imgDt : dataUserImageProfile){
            final byte[] imgFile = imgDt.getTxtImg();
            if (imgFile != null) {
                mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                Bitmap bitmap = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
                ivProfile.setImageBitmap(bitmap);

//                Picasso.with(getApplicationContext()).load(linkImageProfile).fit().into(ivProfile);
            }
        }
    }

    public void logout() {
        Intent intent = new Intent(HomeMenu.this, FlashActivity.class);
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.clearDataAfterLogout();
        finish();
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean status = false;

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult.getContents() == null && scanResult.getFormatName() == null) {
                return;
            }
            final String result = scanResult.getContents();
            if (result != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
                finish();
                startActivity(intent);
//                new clsActivity().showCustomToast(context.getApplicationContext(), result, true);
            }
        }
    }
}
