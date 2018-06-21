package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Context;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by XSIS on 15/03/2017.
 */

public class LocalReportSender implements ReportSender {
    public LocalReportSender(MyApplication myApplication) {
    }

    private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>();
    private FileWriter crashReport;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String fileName = "log_"+dateFormat.format(date)+".txt";
    public LocalReportSender(Context ctx) {
        // the destination
        File logFile = new File(new clsHardCode().txtFolderData, fileName);
        crashReport = null;
        try {
            crashReport = new FileWriter(logFile, true);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNull(String aString) {
        return aString == null || ACRAConstants.NULL_VALUE.equals(aString);
    }

    private Map<String, String> remap(Map<ReportField, String> report) {

        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }

        final Map<String, String> finalReport = new HashMap<String, String>(
                report.size());
        for (ReportField field : fields) {
            if (mMapping == null || mMapping.get(field) == null) {
                finalReport.put(field.toString(), report.get(field));
            } else {
                finalReport.put(mMapping.get(field), report.get(field));
            }
        }
        return finalReport;
    }

    List<clsUserMember> dataMember = null;
    @Override
    public void send(Context context, CrashReportData errorContent) throws ReportSenderException {


        final Map<String, String> finalReport = remap(errorContent);

        try {
            BufferedWriter buf = new BufferedWriter(crashReport);

            Set<Map.Entry<String, String>> set = finalReport.entrySet();
            Iterator<Map.Entry<String, String>> i = set.iterator();

            buf.append("").append("\n");
            clsUserMemberRepo repo = new clsUserMemberRepo(context);
            try {
                dataMember = (List<clsUserMember>) repo.findAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataMember!=null){
                buf.append("\n").append("Member  id :"+ dataMember.get(0).getTxtMemberId() );
            }

            while (i.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();
                buf.append("[" + me.getKey() + "] = " + "'"+me.getValue()+"'").append("\n");
            }

            buf.append("\n").append("----------------*****----------------");
            buf.append("\n").append("\n");
            buf.flush();
            buf.close();
        } catch (IOException e) {
            Log.e("TAG", "IO ERROR", e);
        }
    }
    public String GenerateGuid() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }
}