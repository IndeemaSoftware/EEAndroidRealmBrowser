package com.indeema.realmbrowser;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.indeema.realmbrowser.activities.BaseActivity;
import com.indeema.realmbrowser.activities.FilesActivity;
import com.indeema.realmbrowser.activities.TablesActivity;
import com.indeema.realmbrowser.entities.RbEntity;
import com.indeema.realmbrowser.entities.ThemeType;
import com.indeema.realmbrowser.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ivan Savenko on 15.11.16
 *
 * Updated by Ivan Savenko on 22.11.16
 */

public class RealmBrowser {

    private static final String TAG = RealmBrowser.class.getSimpleName();

    public static final int DEFAULT_PAGE_SIZE = 20;
    private static final int NOTIFICATION_ID = 10_000;

    private static final RealmBrowser sInstance = new RealmBrowser();

    private List<RbEntity> mRbEntities;

    private RealmBrowser() {
        mRbEntities = new ArrayList<>();
    }

    public static RealmBrowser getInstance() {
        return sInstance;
    }

    public static void initialize(@NonNull Activity activity, @NonNull RbEntity... realmDBEntities) {
        sInstance.mRbEntities = new ArrayList<>();
        sInstance.mRbEntities.addAll(Arrays.asList(realmDBEntities));

        Collections.sort(sInstance.mRbEntities, new Comparator<RbEntity>() {
            @Override
            public int compare(RbEntity rbEntity1, RbEntity rbEntity2) {
                return rbEntity1.getRBEntityTitle().compareToIgnoreCase(rbEntity2.getRBEntityTitle());
            }
        });

        if (sInstance.mRbEntities.size() == 1) {
            showRealmTablesBrowsingNotification(activity, 0);
        } else {
            showRealmFilesBrowsingNotification(activity);
        }
    }

    public static void initialize(@NonNull Activity activity, @ThemeType int theme, @NonNull RbEntity... realmDBEntities) {
        initialize(activity, realmDBEntities);
        setBrowserTheme(activity, theme);
    }

    public static void initialize(@NonNull Activity activity, @ThemeType int theme, int pageSize, @NonNull RbEntity... realmDBEntities) {
        initialize(activity, realmDBEntities);
        setBrowserTheme(activity, theme);
        setBrowserPageSize(activity, pageSize);
    }

    public static void setBrowserTheme(Context context, @ThemeType int theme) {
        PreferenceUtils.saveTheme(context, theme);
    }

    public static void setBrowserPageSize(Context context, int pageSize) {
        if (pageSize > 0) {
            PreferenceUtils.savePageSize(context, pageSize);
        }
    }

    public List<RbEntity> getRbEntities() {
        return mRbEntities;
    }

    public void setRbEntities(List<RbEntity> rbEntities) {
        mRbEntities = rbEntities;
    }

    public final void addRealmDBEntities(RbEntity... realmDBEntities) {
        mRbEntities.addAll(Arrays.asList(realmDBEntities));
    }

    public static void startBrowsingRealmFiles(@NonNull Activity activity) {
        if (sInstance.mRbEntities == null || sInstance.mRbEntities.size() == 0) {
            throw new RuntimeException("RealmBrowser not initialized. Call " +
                    "RealmBrowser.initialize(Activity activity, RBEntity... realmDBEntities).");
        } else {
            FilesActivity.start(activity);
        }
    }

    public static void startBrowsingRealmTables(@NonNull Activity activity, @NonNull Integer realmDBEntityIndex) {
        if (sInstance.mRbEntities == null || sInstance.mRbEntities.size() == 0) {
            throw new RuntimeException("RealmBrowser not initialized. " +
                    "Call RealmBrowser.initialize(Activity activity, RBEntity... realmDBEntities).");

        } else if (realmDBEntityIndex < 0) {
            throw new RuntimeException("Invalid 'realmDBEntityIndex'. 'realmDBEntityIndex' should be greater " +
                    "or equal than 0. Call RealmBrowser.getMaxRealmDBEntityIndex() to get max valid value.");

        } else if (sInstance.mRbEntities.size() >= realmDBEntityIndex) {
            throw new RuntimeException("Invalid 'realmDBEntityIndex'. Call RealmBrowser.getMaxRealmDBEntityIndex() " +
                    "to get max valid value.");

        } else {
            TablesActivity.start(activity, realmDBEntityIndex, false);
        }
    }

    public static int getMaxRealmDBEntityIndex() {
        if (sInstance.mRbEntities == null || sInstance.mRbEntities.size() == 0) {
            throw new RuntimeException("RealmBrowser not initialized. Call RealmBrowser.initialize(Activity activity, RBEntity... realmDBEntities).");
        }
        return sInstance.mRbEntities.size() - 1;
    }

    private static void showRealmFilesBrowsingNotification(@NonNull Activity activity) {
        Log.d(TAG, "showRealmDBFilesNotification...");
        Intent intent = new Intent(activity, FilesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        showRealmNotification(activity, pendingIntent);
    }

    private static void showRealmTablesBrowsingNotification(@NonNull Activity activity, @NonNull Integer realmDBEntityIndex) {
        Log.d(TAG, "showRealmDBModelsNotification -> realmDBEntityIndex: " + realmDBEntityIndex);
        Intent intent = new Intent(activity, TablesActivity.class);
        intent.putExtra(BaseActivity.EXTRAS_REALM_DB_ENTITY_INDEX, realmDBEntityIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        showRealmNotification(activity, pendingIntent);
    }


    private static void showRealmNotification(@NonNull Activity activity, PendingIntent notifyPendingIntent) {
        showRealmNotification(activity, activity.getString(R.string.action_start_rb), notifyPendingIntent);
    }

    private static void showRealmNotification(@NonNull Activity activity, String message, PendingIntent notifyPendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.ic_database_white_24dp)
                .setContentTitle(getApplicationName(activity))
                .setContentText(message)
                .setAutoCancel(false);

        if (notifyPendingIntent != null) {
            builder.setContentIntent(notifyPendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

}
