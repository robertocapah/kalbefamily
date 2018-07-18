package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import kalbefamily.crm.kalbe.kalbefamily.BL.AuthenticatorUtil;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsMainBL;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsStatusMenuStart;
import kalbefamily.crm.kalbe.kalbefamily.Repo.enumStatusMenuStart;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

//import com.activeandroid.query.Select;
//import kalbefamily.crm.kalbe.kalbefamily.Common.tdeviceData;

public class FlashActivity extends clsActivity {
    long delay = 5000;
    private TextView version,txtLink;
    public ImageView ivBannerLogin=null;
    boolean firstStart;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private AccountManager mAccountManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_flash);
        mAccountManager = AccountManager.get(this);
        version = (TextView) findViewById(R.id.tv_version);
//        txtLink = (TextView) findViewById(R.id.tv_link);
        version.setText(txtVersionApp()+ "\n" + new mConfigRepo(getApplicationContext()).API );
        version.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//        txtLink.setText(new mConfigRepo(getApplicationContext()).API );
        new MyApp().onCreate();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
//        tdeviceData dt=new tdeviceData();
//        new tdeviceBL().SaveData(dt);
//        new Mobile_mConfigBL().InsertDefaultMconfig();
        super.onResume();
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCameraPermission = ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.CAMERA);
        int hasPhoneStatePermission = ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.READ_PHONE_STATE);

        if (Build.VERSION.SDK_INT >= 23){
            if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                checkPermission();
            } else if (hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED){
                checkPermission();
            } else if (hasCameraPermission != PackageManager.PERMISSION_GRANTED){
                checkPermission();
            }  else if (hasPhoneStatePermission != PackageManager.PERMISSION_GRANTED){
                checkPermission();
            } else {
                StartAnimations();
                checkStatusMenu();
            }
        } else {
            StartAnimations();
            checkStatusMenu();
        }

//        if (Build.VERSION.SDK_INT >= 23
//                && hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED
//                && hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED
//                && hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED
//                && hasCameraPermission != PackageManager.PERMISSION_GRANTED
//                ) {
//            boolean checkPermission = checkPermission();
//
//        } else if (Build.VERSION.SDK_INT >= 23
//                && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED
//                && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED
//                && hasAccessFineLocationPermission == PackageManager.PERMISSION_GRANTED
//                && hasCameraPermission == PackageManager.PERMISSION_GRANTED
//                ){
//            StartAnimations();
//            checkStatusMenu();
//
//        } else if (Build.VERSION.SDK_INT >= 23
//                && hasCameraPermission != PackageManager.PERMISSION_GRANTED){
//            boolean checkPermission = checkPermission();
//        } else {
//            StartAnimations();
//            checkStatusMenu();
//        }
        //StartAnimations();
        //checkStatusMenu();
    }
    private boolean checkPermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FlashActivity.this);
        builder.setMessage("You need to allow access. . .");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && !ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                        Manifest.permission.CAMERA)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                        Manifest.permission.READ_PHONE_STATE)){
                    ActivityCompat.requestPermissions(FlashActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    dialog.dismiss();

                }
//                ActivityCompat.requestPermissions(FlashActivity.this,
//                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
//                        REQUEST_CODE_ASK_PERMISSIONS);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView tv = (TextView) findViewById(R.id.iv_anim);
        tv.clearAnimation();
        tv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.imageView2);
        iv.setBackgroundResource(R.mipmap.ic_kalbe_2);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }
//    private void checkStatusMenu() {
//
//        Timer runProgress = new Timer();
//        TimerTask viewTask = new TimerTask() {
//            public void run() {
//                SharedPreferences setting = getSharedPreferences("PRFRS", 0);
//                firstStart = setting.getBoolean("first_time_start", true);
//
//                if (firstStart) {
//                    SharedPreferences.Editor editor = setting.edit();
//                    editor.putBoolean("first_time_start", false);
//                    editor.commit();
//
//                    Intent myIntent = new Intent(getApplicationContext(), IntroActivity.class);
//                    try {
//                        new mConfigRepo(getApplicationContext()).InsertDefaultmConfig();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    finish();
//                    startActivity(myIntent);
//                } else {
//                    Intent myIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
//                    clsStatusMenuStart _clsStatusMenuStart = null;
//
//                    try {
//                        _clsStatusMenuStart = new clsMainBL().checkUserActive(getApplicationContext());
//                        if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.FormLogin) {
//                            myIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
//                        } else if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin) {
//                            myIntent = new Intent(getApplicationContext(), HomeMenu.class);
//                            myIntent.putExtra("key_view", "home_menu");
////                        startService(new Intent(getApplicationContext(), MyServiceNative.class));
////                        startService(new Intent(getApplicationContext(), MyTrackingLocationService.class));
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        new mConfigRepo(getApplicationContext()).InsertDefaultmConfig();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    finish();
//                    startActivity(myIntent);
//                }
//            }
//        };
//        runProgress.schedule(viewTask, delay);
//    }

    Intent myIntent = null;
    clsStatusMenuStart _clsStatusMenuStart = null;
private void checkStatusMenu() {

    Timer runProgress = new Timer();
    TimerTask viewTask = new TimerTask() {
        public void run() {
            SharedPreferences setting = getSharedPreferences("PRFRS", 0);
            firstStart = setting.getBoolean("first_time_start", true);

            if (firstStart) {
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean("first_time_start", false);
                editor.commit();

                Intent myIntent = new Intent(getApplicationContext(), IntroActivity.class);
                try {
                    new mConfigRepo(getApplicationContext()).InsertDefaultmConfig();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(myIntent);
            } else {
//                Intent myIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
//                clsStatusMenuStart _clsStatusMenuStart = null;

                try {
                    _clsStatusMenuStart = new clsMainBL().checkUserActive(getApplicationContext());
//                    new MyApplication().onCreate();
                    if (_clsStatusMenuStart.get_intStatus() != null){
                        if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.FormLogin) {
                            myIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
                        } else if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin) {
                            if (new AuthenticatorUtil().countingAccount(mAccountManager).length==0){
                                myIntent = new Intent(getApplicationContext(), HomeMenu.class);
                                myIntent.putExtra("key_view", "home_menu");
                                finish();
                                startActivity(myIntent);
                            } else {
                                myIntent = new Intent(getApplicationContext(), HomeMenu.class);
                                myIntent.putExtra("key_view", "home_menu");
                                finish();
                                startActivity(myIntent);
                            }
//                            myIntent = new Intent(getApplicationContext(), HomeMenu.class);
//                            myIntent.putExtra("key_view", "home_menu");
//                        startService(new Intent(getApplicationContext(), MyServiceNative.class));
//                        startService(new Intent(getApplicationContext(), MyTrackingLocationService.class));
                        }
                    } else {
                        new AuthenticatorUtil().showAccountPicker(FlashActivity.this, mAccountManager, AUTHTOKEN_TYPE_FULL_ACCESS);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    new mConfigRepo(getApplicationContext()).InsertDefaultmConfig();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                finish();
//                startActivity(myIntent);
            }
        }
    };
    runProgress.schedule(viewTask, delay);
}
}
