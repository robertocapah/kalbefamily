package kalbefamily.crm.kalbe.kalbefamily.BL;

import android.content.Context;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsStatusMenuStart;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserLoginRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.enumStatusMenuStart;

/**
 * Created by Rian Andrivani on 7/11/2017.
 */

public class clsMainBL {
    public String GenerateGuid(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }
    public String GenerateGuid(Context context){
        DeviceUuidFactory _DeviceUuidFactory=new DeviceUuidFactory(context);
        return _DeviceUuidFactory.getDeviceUuid().toString();
    }
    public clsStatusMenuStart checkUserActive(Context context) throws ParseException {
        clsUserLoginRepo repo = new clsUserLoginRepo(context);
        clsUserMemberRepo repoUser = new clsUserMemberRepo(context);
        clsStatusMenuStart _clsStatusMenuStart =new clsStatusMenuStart();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String now = dateFormat.format(cal.getTime()).toString();
//        if(repo.CheckLoginNow()){
        List<clsUserLoginData> listData= null;
        List<clsUserMember> listDataMember = null;
        try {
            listData = (List<clsUserLoginData>) repo.findAll();
            listDataMember = (List<clsUserMember>) repoUser.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (clsUserMember data : listDataMember){
            if (!data.getTxtKontakId().equals(null)){
                _clsStatusMenuStart.set_intStatus(enumStatusMenuStart.UserActiveLogin);
            }
        }

//        }
        return _clsStatusMenuStart;
    }

}
