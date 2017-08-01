package come.example.viewbadger;

import android.content.ComponentName;
import android.content.Context;

import java.util.List;

public interface BadgerShortCut {

    /**
     * Called when user attempts to update notification count
     * @param context Caller context
     * @param componentName Component containing package and class name of calling application's
     *                      launcher activity
     * @param badgeCount Desired notification count
     * @throws ShortcutBadgeException
     */
    @SuppressWarnings("deprecation")
	void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException;
    //void executeBages(Context context, int badgeCount) throws ShortcutBadgeException;

    /**
     * Called to let {@link ShortcutBadger} knows which launchers are supported by this badger. It should return a
     * {@link List<String>} containing supported launchers package names
     */
    List<String> getSupportLaunchers();
}
