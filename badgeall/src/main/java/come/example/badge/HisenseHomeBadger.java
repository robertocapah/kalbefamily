package come.example.badge;

import java.util.Arrays;
import java.util.List;

import come.example.viewbadger.BadgerShortCut;
import come.example.viewbadger.ShortcutBadgeException;
import come.example.viewbadger.ShortcutBadger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


public class HisenseHomeBadger implements BadgerShortCut{

	private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
	
	@Override
	public void executeBadge(Context context, ComponentName componentName,
			int badgeCount) throws ShortcutBadgeException {
		// TODO Auto-generated method stub
		Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
        intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        intent.putExtra("badge_vip_count", 0);
        context.sendBroadcast(intent);
		
	}

	@Override
	public List<String> getSupportLaunchers() {
		// TODO Auto-generated method stub
		return Arrays.asList("com.android.launcher");
	}

}
