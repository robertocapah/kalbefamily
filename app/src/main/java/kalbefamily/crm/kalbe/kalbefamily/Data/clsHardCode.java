package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;

/**
 * Created by Rian Andrivani on 6/22/2017.
 */

public class clsHardCode {
    Context context;
    public String txtPathApp= Environment.getExternalStorageDirectory()+File.separator+"data"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"app_database"+File.separator;
    public String dbName = "KalbeFamily.db";
    public String txtFolderData = Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"image_Person"+File.separator;
    public String txtFolderAbsen = Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"image_Person"+File.separator;

    public String linkGetDetailKontak =  new mConfigRepo(context).API + "GetDetailKontak";;
    public String linkCheckVersion = new mConfigRepo(context).API + "CheckVersionApp";
    //    public String linkPushData = "http://10.171.11.87/APIEF2/api/PushData/pushData2";
    public String linkSendData = new mConfigRepo(context).API + "UpdateDataKontak";
    public String linkSendDataMediaKontak = new mConfigRepo(context).API + "UpdateDataMediaKontak";
    public String linkGetDetailCustomer = new mConfigRepo(context).API + "GetDetailKontakCustomer";
    public String linkScanQRCode = new mConfigRepo(context).API + "ScanQRCode";
    public String linkGetDatadMembership = new mConfigRepo(context).API + "GetDatadMembership";
    public String linkAvailablePoin = new mConfigRepo(context).API + "AvailablePoinCustomer";
    public String linkGetDataKontakDetail = new mConfigRepo(context).API + "GetDataMediaKontakDetail";
    public String linkGetDataMediaType = new mConfigRepo(context).API + "GetMediaType";
    public String linkGetDataJenisMedia = new mConfigRepo(context).API + "GetJenisMedia";
}
