package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;

public class PasswordActivity extends AppCompatActivity {
    EditText password, confirmPassword;
    String txtPassword, txtConfirmPassword;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.title_login));
        }
        setContentView(R.layout.activity_create_password);

        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        submit = (Button) findViewById(R.id.buttonPassword);

        popup();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")){
                    new clsActivity().showCustomToast(getApplicationContext(), "Password tidak boleh kosong", false);
                } else if (confirmPassword.getText().toString().equals("")){
                    new clsActivity().showCustomToast(getApplicationContext(), "Confirm Password tidak boleh kosong", false);
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    new clsActivity().showCustomToast(getApplicationContext(), "Password harus sama", false);
                } else {
                    popupSubmitPassword();
                }
            }
        });
    }

    private void popup() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Anda belum memiliki password, silahkan buat password terlebih dahulu")
                .show();
    }

    private void popupSubmitPassword() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Are you sure?");
        pDialog.setCancelText("No,cancel plx!");
        pDialog.setConfirmText("Yes, sure!");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.setTitleText("Success !")
                        .setContentText("Password berhasil dibuat !")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(getApplicationContext(), NewLoginActivity.class);
                                finish();
                                startActivity(intent);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        });
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                // reuse previous dialog instance, keep widget user state, reset them if you need
                sDialog.setTitleText("Cancelled!")
                        .setContentText("Password batal dibuat :)")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
        pDialog.show();
    }

}
