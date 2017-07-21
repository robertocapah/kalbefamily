package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 7/20/2017.
 */

public class FragmentPersonalData extends Fragment {
    View v;
    EditText etNama, etEmail, etAlamat, etTelpon, etNoKTP;
    TextView etKontakId, etMemberId;
    RadioButton radioPria, radiowanita;
    RadioGroup radioGenderGroup;
    Button btnUpdate;
    Context context;

    List<clsUserMember> dataMember = null;
    clsUserMemberRepo repoUserMember = null;
    boolean validate = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_personal_data,container,false);
        context = getActivity().getApplicationContext();

        etKontakId = (TextView) v.findViewById(R.id.etKontakId);
        etMemberId = (TextView) v.findViewById(R.id.etMemberId);
        etNama = (EditText) v.findViewById(R.id.etNama);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etTelpon = (EditText) v.findViewById(R.id.etTelpon);
        etAlamat = (EditText) v.findViewById(R.id.etAlamat);
        radioGenderGroup = (RadioGroup) v.findViewById(R.id.radioGroupGender);
        radioPria = (RadioButton) v.findViewById(R.id.radioButton1);
        radiowanita = (RadioButton) v.findViewById(R.id.radioButton2);
        etNoKTP = (EditText) v.findViewById(R.id.etNoKTP);
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        etKontakId.setText(dataMember.get(0).getTxtKontakId().toString());
        etMemberId.setText(dataMember.get(0).getTxtMemberId().toString());
        etNama.setText(dataMember.get(0).getTxtNama().toString());
        etAlamat.setText(dataMember.get(0).getTxtAlamat().toString());
        etEmail.setText(dataMember.get(0).getTxtEmail().toString());
        etTelpon.setText(dataMember.get(0).getTxtNoTelp().toString());

        if (dataMember.get(0).getTxtNoKTP().toString().equals("null")) {
            etNoKTP.setText("");
        } else {
            etNoKTP.setText(dataMember.get(0).getTxtNoKTP().toString());
        }

        if (dataMember.get(0).txtJenisKelamin.equals("Perempuan")) {
            radioPria.setChecked(false);
            radiowanita.setChecked(true);
        } else {
            radioPria.setChecked(true);
            radiowanita.setChecked(false);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clsUserMember dataUser = new clsUserMember();
                        dataUser.setTxtKontakId(etKontakId.getText().toString());
                        dataUser.setTxtMemberId(etMemberId.getText().toString());
                        dataUser.setTxtNama(etNama.getText().toString());
                        dataUser.setTxtAlamat(etAlamat.getText().toString());
                        dataUser.setTxtNoKTP(etNoKTP.getText().toString());

                        if(!isValidEmail(etEmail.getText().toString())){
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Email tidak valid", false);
                            validate = false;
                        } else if (!isValidMobile(etTelpon.getText().toString())) {
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Telpon harus berupa angka", false);
                            validate = false;
                        } else {
                            dataUser.setTxtEmail(etEmail.getText().toString());
                            dataUser.setTxtNoTelp(etTelpon.getText().toString());

                            int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                            RadioButton rbGender = (RadioButton) v.findViewById(selectedId);

                            dataUser.setTxtJenisKelamin(rbGender.getText().toString());

                            repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                            repoUserMember.createOrUpdate(dataUser);

                            new clsActivity().showCustomToast(context.getApplicationContext(), "Saved", true);
                            Intent intent = new Intent(context.getApplicationContext(), HomeMenu.class);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

        return v;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
