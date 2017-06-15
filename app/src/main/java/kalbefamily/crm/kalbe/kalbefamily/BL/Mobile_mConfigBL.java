package kalbefamily.crm.kalbe.kalbefamily.BL;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.Mobile_mConfigData;

/**
 * Created by rhezaTesar on 3/13/2017.
 */

public class Mobile_mConfigBL {
    public String getValue(int id){
        String txtValue="";
        List<Mobile_mConfigData> ListData=new Select().from(Mobile_mConfigData.class).where("idConfig=?",new String[]{String.valueOf(id)}).execute();
        if(ListData!=null){
            if(ListData.get(0).txtValue.equals("")){
                txtValue=ListData.get(0).txtDefaultValue;
            }else{
                txtValue=ListData.get(0).txtValue;
            }
        }
        return txtValue;
    }
    public void InsertDefaultMconfig(){
        int _count=1;
        Mobile_mConfigData _clsMobile_mConfig=new Mobile_mConfigData();
        From from = new Select().from(Mobile_mConfigData.class);
        List<Mobile_mConfigData> ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",1).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            _count=from.count()+1;
        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(1);
        _clsMobile_mConfig.txtName="API";
        //_clsMobile_mConfig.txtValue="http://10.0.0.242/KN2015 _PRM_V2.WEB/VisitPlan/API/VisitPlanAPI/";
        //_clsMobile_mConfig.txtV   alue="http://10.171.10.17/KN2015_PRM_V2.WEB/VisitPlan/API/VisitPlanAPI/";
        //_clsMobile_mConfig.txtValue="http://10.192.0.66/KN2015_PRM_V2.WEB/VisitPlan/API/VisitPlanAPI/";
        _clsMobile_mConfig.txtValue="http://10.171.11.117:8001/api/apiKF.aspx";
        _clsMobile_mConfig.txtDefaultValue="http://10.171.52.210:8001/api/apiKF.aspx";
        _clsMobile_mConfig.save();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",2).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;
        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(2);
        _clsMobile_mConfig.txtName="Type Mobile";
        _clsMobile_mConfig.txtValue="KALBEFAMILY-Regiter";
        _clsMobile_mConfig.txtDefaultValue="KALBEFAMILY-Regiter";
        _clsMobile_mConfig.save();

        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",3).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;
        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(3);
        _clsMobile_mConfig.txtName="Background Service Online";
        _clsMobile_mConfig.txtValue="1000";
        _clsMobile_mConfig.txtDefaultValue="60000";
        _clsMobile_mConfig.save();

        _clsMobile_mConfig=new Mobile_mConfigData();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",4).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;

        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(4);
        _clsMobile_mConfig.txtName="TIMEOUT";
        _clsMobile_mConfig.txtValue="6000";
        _clsMobile_mConfig.txtDefaultValue="1000";
        _clsMobile_mConfig.save();

        _clsMobile_mConfig=new Mobile_mConfigData();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",5).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;

        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(5);
        _clsMobile_mConfig.txtName="VISIT_PLAN_GEOTAGING_RADIUS";
        _clsMobile_mConfig.txtValue="0";
        _clsMobile_mConfig.txtDefaultValue="0";
        _clsMobile_mConfig.save();

        _clsMobile_mConfig=new Mobile_mConfigData();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",6).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;

        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(6);
        _clsMobile_mConfig.txtName="VISIT_PLAN_SIZE_SAVE_PHOTO";
        _clsMobile_mConfig.txtValue="0";
        _clsMobile_mConfig.txtDefaultValue="0";
        _clsMobile_mConfig.save();


        _clsMobile_mConfig=new Mobile_mConfigData();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",7).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;

        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(7);
        _clsMobile_mConfig.txtName="LAST_SYNC";
        _clsMobile_mConfig.txtValue="0";
        _clsMobile_mConfig.txtDefaultValue="0";
        _clsMobile_mConfig.save();

        _clsMobile_mConfig=new Mobile_mConfigData();
        ListOfclsMobile_mConfig = new Select().from(Mobile_mConfigData.class).where("idConfig=?",8).execute();
        if(ListOfclsMobile_mConfig.size()==0){
            _clsMobile_mConfig=new Mobile_mConfigData();
            from = new Select().from(Mobile_mConfigData.class);
            _count=from.count()+1;

        }else{
            _clsMobile_mConfig=ListOfclsMobile_mConfig.get(0);
        }
        _clsMobile_mConfig.idConfig= Long.valueOf(8);
        _clsMobile_mConfig.txtName="LAST_RUN_SCHEDULE";
        _clsMobile_mConfig.txtValue="0";
        _clsMobile_mConfig.txtDefaultValue="0";
        _clsMobile_mConfig.save();

    }
}
