package come.example.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


import java.util.Arrays;
import java.util.List;

import come.example.viewbadger.BadgerShortCut;
import come.example.viewbadger.ShortcutBadgeException;
import come.example.viewbadger.ShortcutBadger;

public class AdwHomeBadger implements BadgerShortCut {

    public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
    public static final String PACKAGENAME = "PNAME";
    public static final String COUNT = "COUNT";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {

        Intent intent = new Intent(INTENT_UPDATE_COUNTER);
        intent.putExtra(PACKAGENAME, componentName.getPackageName());
        intent.putExtra(COUNT, badgeCount);
        context.sendBroadcast(intent);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "org.adw.launcher",
                "org.adwfreak.launcher"
        );
    }
}
