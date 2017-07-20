package kalbefamily.crm.kalbe.kalbefamily;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;


/**
 * Created by Rian Andrivani on 7/19/2017.
 */

public class KontakDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    PackageInfo pInfo = null;
    int selectedId;
    Boolean isSubMenu = false;
    private GoogleApiClient client;
    clsActivity _clsMainActivity = new clsActivity();

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Exit");
//        builder.setMessage("Do you want to exit?");
//
//        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(KontakDetailActivity.this, CardViewActivity.class);
//                finish();
//                startActivity(intent);
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
//    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
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
        toolbar.setTitle("Contact Detail");
        setSupportActionBar(toolbar);

        FragmentInfoContact ContactFragment = new FragmentInfoContact();
        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
        fragmentTransactionHome.replace(R.id.frame, ContactFragment);
        fragmentTransactionHome.commit();
        selectedId = 99;

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View vwHeader = navigationView.getHeaderView(0);

        Menu header = navigationView.getMenu();
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
                    case R.id.logoutContact:
                        LayoutInflater layoutInflater = LayoutInflater.from(KontakDetailActivity.this);
                        final View promptView = layoutInflater.inflate(R.layout.confirm_data, null);

                        final TextView _tvConfirm = (TextView) promptView.findViewById(R.id.tvTitle);
                        final TextView _tvDesc = (TextView) promptView.findViewById(R.id.tvDesc);
                        _tvDesc.setVisibility(View.INVISIBLE);
                        _tvConfirm.setText("Log Out Application ?");
                        _tvConfirm.setTextSize(18);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KontakDetailActivity.this);
                        alertDialogBuilder.setView(promptView);
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        logout();
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
                    case R.id.contact:
                        toolbar.setTitle("Contact Detail");

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

                        FragmentInfoContact ContactFragment = new FragmentInfoContact();
                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHome.replace(R.id.frame, ContactFragment);
                        fragmentTransactionHome.commit();
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
}