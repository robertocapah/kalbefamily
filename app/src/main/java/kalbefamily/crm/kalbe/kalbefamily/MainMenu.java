package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAbsenData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDisplayPicture;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Fragment.FragmentInformation;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAbsenDataRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsDisplayPictureRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserLoginRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsmVersionAppRepo;

/**
 * Created by Rian Andrivani on 7/7/2017.
 */

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private clsAbsenData dttAbsenUserData;

    private TextView tvUsername, tvEmail;
    private CircleImageView ivProfile;
    private List<clsDisplayPicture> tDisplayPictureData;

    PackageInfo pInfo = null;

    int selectedId;
    private static int menuId = 0;
    Boolean isSubMenu = false;

    clsActivity _clsMainActivity = new clsActivity();
    List<clsUserLoginData> dataLogin = null;
    List<clsmVersionApp> dataInfo = null;
    String[] listMenu;
    String[] linkMenu;

    private GoogleApiClient client;

    String i_view = null;
    Intent intent;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
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
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FragmentInformation homeFragment = new FragmentInformation();
        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
        fragmentTransactionHome.replace(R.id.frame, homeFragment);
        fragmentTransactionHome.commit();
        selectedId = 99;

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View vwHeader = navigationView.getHeaderView(0);
        ivProfile = (CircleImageView) vwHeader.findViewById(R.id.profile_image);
        tvUsername = (TextView) vwHeader.findViewById(R.id.username);
        tvEmail = (TextView) vwHeader.findViewById(R.id.email);
        clsUserLoginRepo repo = new clsUserLoginRepo(getApplicationContext());
        clsmVersionAppRepo repoVersionInfo = new clsmVersionAppRepo(getApplicationContext());

        try {
            dataLogin = (List<clsUserLoginData>) repo.findAll();
            dataInfo = (List<clsmVersionApp>) repoVersionInfo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUsername.setText(_clsMainActivity.greetings() + dataLogin.get(0).getTxtName());
        tvEmail.setText(dataLogin.get(0).getTxtEmail());
        try {
            tDisplayPictureData = (List<clsDisplayPicture>) new clsDisplayPictureRepo(getApplicationContext()).findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (tDisplayPictureData.size() > 0 && tDisplayPictureData.get(0).getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(tDisplayPictureData.get(0).getImage(), 0, tDisplayPictureData.get(0).getImage().length);
            ivProfile.setImageBitmap(bitmap);
        } else {
            ivProfile.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.profile));
        }
        ivProfile.setOnClickListener(this);
        Menu header = navigationView.getMenu();
        clsAbsenData dataAbsenAktif = new clsAbsenDataRepo(getApplicationContext()).getDataCheckinActive(getApplicationContext());
        if (dataAbsenAktif!=null){
            header.removeItem(R.id.absen);
            header.removeItem(R.id.logout);
        }else{
            header.removeItem(R.id.checkout);
        }
        SubMenu subMenuVersion = header.addSubMenu(R.id.groupVersion, 0, 3, "Version");
        try {
            subMenuVersion.add(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " \u00a9 KN-IT").setIcon(R.drawable.ic_android).setEnabled(false);
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
                    case R.id.logout:
                        LayoutInflater layoutInflater = LayoutInflater.from(MainMenu.this);
                        final View promptView = layoutInflater.inflate(R.layout.confirm_data, null);

                        final TextView _tvConfirm = (TextView) promptView.findViewById(R.id.tvTitle);
                        final TextView _tvDesc = (TextView) promptView.findViewById(R.id.tvDesc);
                        _tvDesc.setVisibility(View.INVISIBLE);
                        _tvConfirm.setText("Log Out Application ?");
                        _tvConfirm.setTextSize(18);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                        alertDialogBuilder.setView(promptView);
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        logout();
//                                        stopService(new Intent(getApplicationContext(), MyServiceNative.class));
//                                        stopService(new Intent(getApplicationContext(), MyTrackingLocationService.class));
//                                        AsyncCallLogOut task = new AsyncCallLogOut();
//                                        task.execute();
                                        //new clsHelperBL().DeleteAllDB();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alertD = alertDialogBuilder.create();
                        alertD.show();
                        return true;
//                    case R.id.absen:
//                        toolbar.setTitle("Absen");
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        FragmentAbsen fragmentAbsen = new FragmentAbsen();
//                        FragmentTransaction fragmentTransactionKuesioner = getSupportFragmentManager().beginTransaction();
//                        fragmentTransactionKuesioner.replace(R.id.frame, fragmentAbsen);
//                        fragmentTransactionKuesioner.commit();
//                        return true;
                    case R.id.home:
                        toolbar.setTitle("Home");

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

                        FragmentInformation homeFragment = new FragmentInformation();
                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHome.replace(R.id.frame, homeFragment);
                        fragmentTransactionHome.commit();
                        selectedId = 99;

                        return true;
//                    case R.id.checkout:
//                        LayoutInflater _layoutInflater = LayoutInflater.from(MainMenu.this);
//                        final View _promptView = _layoutInflater.inflate(R.layout.confirm_data, null);
//
//                        final TextView tvConfirm = (TextView) _promptView.findViewById(R.id.tvTitle);
//                        final TextView tvDesc = (TextView) _promptView.findViewById(R.id.tvDesc);
//                        tvDesc.setVisibility(View.INVISIBLE);
//                        tvConfirm.setText("Check Out Data ?");
//                        tvConfirm.setTextSize(18);
//
//                        AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
//                        _alertDialogBuilder.setView(_promptView);
//                        _alertDialogBuilder
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        clsAbsenData dataAbsen = new clsAbsenDataRepo(getApplicationContext()).getDataCheckinActive(getApplicationContext());
//
//                                        if (dataAbsen != null) {
//                                            dataAbsen.setDtCheckout(_clsMainActivity.FormatDateDB().toString());
//                                            try {
//                                                new clsAbsenDataRepo(getApplicationContext()).update(dataAbsen);
//                                            } catch (SQLException e) {
//                                                e.printStackTrace();
//                                            }
//                                            finish();
//                                            Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
//                                            nextScreen.putExtra("keyMainMenu", "main_menu");
//                                            finish();
//                                            startActivity(nextScreen);
//                                        }
//                                    }
//                                })
//                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        final AlertDialog _alertD = _alertDialogBuilder.create();
//                        _alertD.show();
//
//                        return true;
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

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                Intent intent = new Intent(this, CropDisplayPicture.class);
                String strName = imageUri.toString();
                intent.putExtra("uriPicture", strName);
                startActivity(intent);
                finish();
            }
        } else if (requestCode == 100 || requestCode == 130){
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    public void logout(){
        final ProgressDialog Dialog = new ProgressDialog(MainMenu.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String strLinkAPI = "http://prm.kalbenutritionals.web.id/VisitPlan/API/VisitPlanAPI/LogOut_J"; // http://prm.kalbenutritionals.web.id/VisitPlan/API/VisitPlanAPI/LogIn_J
        // http://10.171.10.30/KN2015_PRM_V2.WEB/VisitPlan/API/VisitPlanAPI/LogOut_J
//        String nameRole = selectedRole;
        final JSONObject resJson = new JSONObject();

        try {
            resJson.put("TxtGUI_trUserLogin", dataLogin.get(0).getTxtGUI());
            resJson.put("TxtUserID", dataLogin.get(0).getTxtUserID());
            resJson.put("TxtGUI_mVersionApp", dataInfo.get(0).getTxtVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";
        new VolleyUtils().makeJsonObjectRequest(MainMenu.this, strLinkAPI, mRequestBody,"Logging Out, Please Wait !", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtWarn");
                        String result = jsn.getString("TxtResult");
                        if (result.equals("1")){
//                            new DatabaseHelper(getApplicationContext()).clearDataAfterLogout();
                            DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
                            helper.clearDataAfterLogout();
//                            helper.close();
                            Intent nextScreen = new Intent(MainMenu.this, FlashActivity.class);
                            startActivity(nextScreen);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                pickImage2();
                break;
        }
    }

    public void pickImage2()
    {
        CropImage.startPickImageActivity(this);
    }
}
