package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsWarning;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsDeviceInfoRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsmVersionAppRepo;

public class NewMemberActivity extends AppCompatActivity {
    private EditText txtMemberId;
    private clsWarning _clsWarning;
    private GoogleApiClient client;

    private String txtMember;
    private String role = "Role";
    private String[] roles = new String[1];
    ProgressDialog progress;
    long Delay = 3000;
    private Button btnSubmit, btnPing;
    private TextView txtVersionLogin;
    private TextView txtHDId, txtHDId2;
    private PackageInfo pInfo = null;
    //    private Spinner spnRole, spnOutlet;
    private int intSet = 1;
    private String[] arrdefaultBranch = new String[]{"-"};
    private String[] arrdefaultOutlet = new String[]{"-"};
    private static final String TAG = "MainActivity";
    clsDeviceInfoRepo repoDeviceInfo = null;
    clsmVersionAppRepo repoVersionApp = null;
    clsUserMemberRepo repoUserMember = null;
    int intProcesscancel = 0;

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
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
        setContentView(R.layout.activity_new_member);
    }
}
