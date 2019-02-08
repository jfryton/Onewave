package com.nanowheel.nanoux.nanowheel.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import com.nanowheel.nanoux.nanowheel.App;
import com.nanowheel.nanoux.nanowheel.MainActivity;
import com.nanowheel.nanoux.nanowheel.R;

import androidx.core.app.NotificationCompat;

import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicBatteryInitial;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicCharging;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad1;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad2;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryRemaining;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicSpeedRpm;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicStatusError;
import static com.nanowheel.nanoux.nanowheel.util.BluetoothService.mOWDevice;

public class NotificationBuilder {

    public static final int SATICJOBID = 2400;
    public static final int NORMJOBID = 2442;

    public static final int CHARGEWARNID = 2401;
    public static final int TURNWARNID = 2402;
    public static final int BATT1WARNID = 2403;
    public static final int BATT2WARNID = 2404;
    public static final int REGENWARNID = 2405;
    public static final int SPEEDRECID = 2406;
    public static final int SENSORWARNID = 2407;
    public static final int SPEEDWARNID = 2408;
    public static final int SPEED2WARNID = 2409;
    public static final int GENERICID = 2410;

    static int lastBatteryLevel = -1;

    static void setupCallabcks(Context context){
        canNotifySensor = true;
        canNotifySpeed = true;

        final Context mContext = context;
        mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                chargeWarning(mContext);
                turnWarning(mContext);
                batt1Warning(mContext);
                batt2Warning(mContext);
                regenWarning(mContext);

                lastBatteryLevel = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            }
        });
    }

    static Notification createScanningNot(Context context, String text, boolean post){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelStatic(context);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,i,0);
        NotificationCompat.Builder mBuilder;

        Intent ic = new Intent(context,BroadcastReciever.class);
        ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
        PendingIntent pic = PendingIntent.getBroadcast(context,0,ic,0);
        mBuilder = new NotificationCompat.Builder(context, SATICJOBID + "")
                //.setContentTitle(text)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_status_bar_scan)
                .setChannelId(SATICJOBID + "")
                .setContentIntent(pi)
                .addAction(R.drawable.ic_bottom_diagnotics,"Cancel",pic)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification not = mBuilder.build();
        if(post){
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.notify(SATICJOBID, not);
        }
        return not;
    }

    static void createStatusNot(int percent, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelStatic(context);

            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            Notification.Builder mBuilder;

            Icon icon = getBatIcon(percent,context);

            Intent ic = new Intent(context, BroadcastReciever.class);
            ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
            PendingIntent pic = PendingIntent.getBroadcast(context, 0, ic, 0);
            Notification.Action action = new Notification.Action(R.drawable.ic_status_bar_static, "Disconnect", pic);

            Intent ic2 = new Intent(context, BroadcastReciever.class);
            ic2.setAction("com.nanowheel.nanoux.nanowheel.LIGHTS");
            PendingIntent pic2 = PendingIntent.getBroadcast(context, 0, ic2, 0);
            Notification.Action action2 = new Notification.Action(R.drawable.ic_status_bar_static, "Lights", pic2);

            mBuilder = new Notification.Builder(context, SATICJOBID + "")
                    .setContentTitle("Battery: ")
                    .setContentText(percent + "%")
                    .setChannelId(SATICJOBID + "")
                    .setContentIntent(pi)
                    .addAction(action)
                    .addAction(action2);

            if (icon != null) {
                mBuilder.setSmallIcon(icon);
            }else{
                mBuilder.setSmallIcon(R.drawable.num100);
            }
            mBuilder.setProgress(100, percent, false);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.notify(SATICJOBID, mBuilder.build());
        }else {
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            NotificationCompat.Builder mBuilder;

            Intent ic = new Intent(context, BroadcastReciever.class);
            ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
            PendingIntent pic = PendingIntent.getBroadcast(context, 0, ic, 0);

            Intent ic2 = new Intent(context, BroadcastReciever.class);
            ic2.setAction("com.nanowheel.nanoux.nanowheel.LIGHTS");
            PendingIntent pic2 = PendingIntent.getBroadcast(context, 0, ic2, 0);

            mBuilder = new NotificationCompat.Builder(context, SATICJOBID + "")
                    .setContentTitle("Battery: ")
                    .setContentText(percent + "%")
                    .setSmallIcon(R.drawable.ic_status_bar_static)
                    .setChannelId(SATICJOBID + "")
                    .setContentIntent(pi)
                    .addAction(R.drawable.ic_bottom_diagnotics, "Disconnect", pic)
                    .addAction(R.drawable.ic_bottom_diagnotics, "Lights", pic2)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            mBuilder.setProgress(100, percent, false);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.notify(SATICJOBID, mBuilder.build());
        }
    }

    final static String CHANNEL_NAME = "Board Status Service";
    final static String CHANNEL_DESCRIPTION = "Service notification that displays board status when connected.";
    static void createChannelStatic(Context context){
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        CharSequence name = CHANNEL_NAME;
        String description = CHANNEL_DESCRIPTION;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(SATICJOBID + "", name, importance);
        channel.setDescription(description);
        channel.setSound(null,null);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(channel);
    }

    final static String CHANNEL_NAME2 = "Board Status Notifications";
    final static String CHANNEL_DESCRIPTION2 = "Notifications about board status, such as turn-around and battery warnings.";
    static void createChannelNorm(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        CharSequence name = CHANNEL_NAME2;
        String description = CHANNEL_DESCRIPTION2;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NORMJOBID + "", name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    public static Icon getBatIcon(int battery, Context context) {
        char batteryL = (battery + "").charAt(0);
        char batteryR = (battery + "").charAt((battery + "").length() - 1);
        int leftID = R.drawable.num0;
        int rightID = R.drawable.num0;
        if (battery < 100) {
            switch (batteryR) {
                case '1':
                    rightID = R.drawable.num1;
                    break;
                case '2':
                    rightID = R.drawable.num2;
                    break;
                case '3':
                    rightID = R.drawable.num3;
                    break;
                case '4':
                    rightID = R.drawable.num4;
                    break;
                case '5':
                    rightID = R.drawable.num5;
                    break;
                case '6':
                    rightID = R.drawable.num6;
                    break;
                case '7':
                    rightID = R.drawable.num7;
                    break;
                case '8':
                    rightID = R.drawable.num8;
                    break;
                case '9':
                    rightID = R.drawable.num9;
                    break;
            }

            if (battery > 9) {
                switch (batteryL) {
                    case '1':
                        leftID = R.drawable.num1;
                        break;
                    case '2':
                        leftID = R.drawable.num2;
                        break;
                    case '3':
                        leftID = R.drawable.num3;
                        break;
                    case '4':
                        leftID = R.drawable.num4;
                        break;
                    case '5':
                        leftID = R.drawable.num5;
                        break;
                    case '6':
                        leftID = R.drawable.num6;
                        break;
                    case '7':
                        leftID = R.drawable.num7;
                        break;
                    case '8':
                        leftID = R.drawable.num8;
                        break;
                    case '9':
                        leftID = R.drawable.num9;
                        break;
                }
            }
            Bitmap left = BitmapFactory.decodeResource(context.getResources(), leftID);
            Bitmap right = BitmapFactory.decodeResource(context.getResources(), rightID);
            left = left.copy(Bitmap.Config.ARGB_8888, true);
            right = right.copy(Bitmap.Config.ARGB_8888, true);
            Canvas comboImage = new Canvas(left);
            comboImage.drawBitmap(right, 50f, 0f, null);

            Icon icon = Icon.createWithBitmap(left);
            return icon;
        }else{
            return null;
        }
    }
    static void chargeWarning(Context context){
        if (App.INSTANCE.getSharedPreferences().getChargeWarning() && lastBatteryLevel >= 0){
            String charging = mOWDevice.characteristics.get(MockOnewheelCharacteristicCharging).value.get();
            if (charging == null || Boolean.parseBoolean(charging)){
                int battery = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
                int thresh = App.INSTANCE.getSharedPreferences().getChargeWarningValue();
                if (battery == thresh && lastBatteryLevel < thresh){
                    createChannelNorm(context);

                    Intent i = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                    NotificationCompat.Builder mBuilder;

                    mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                            //.setContentTitle(text)
                            .setContentText("Battery has charged to " + battery + "%!")
                            .setSmallIcon(R.drawable.ic_status_bar_batt_charge)
                            .setChannelId(NORMJOBID + "")
                            .setContentIntent(pi)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.notify(CHARGEWARNID, mBuilder.build());
                }
            }
        }
    }
    public static void test(Context context){
        createChannelNorm(context);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder;

        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                //.setContentTitle(text)
                .setContentText("Battery has charged to " + "fuck" + "%!")
                .setSmallIcon(R.drawable.ic_status_bar_batt_charge)
                .setChannelId(NORMJOBID + "")
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(CHARGEWARNID, mBuilder.build());


        //Intent i = new Intent(context, MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        //NotificationCompat.Builder mBuilder;

        Intent ic = new Intent(context, BroadcastReciever.class);
        ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
        PendingIntent pic = PendingIntent.getBroadcast(context, 0, ic, 0);

        Intent ic2 = new Intent(context, BroadcastReciever.class);
        ic2.setAction("com.nanowheel.nanoux.nanowheel.LIGHTS");
        PendingIntent pic2 = PendingIntent.getBroadcast(context, 0, ic2, 0);

        mBuilder = new NotificationCompat.Builder(context, SATICJOBID + "")
                .setContentTitle("Battery: ")
                .setContentText(30 + "%")
                .setSmallIcon(R.drawable.ic_status_bar_static)
                .setChannelId(SATICJOBID + "")
                .setContentIntent(pi)
                .addAction(R.drawable.ic_bottom_diagnotics, "Disconnect", pic)
                .addAction(R.drawable.ic_bottom_diagnotics, "Lights", pic2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setProgress(100, 30, false);

        //NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(TURNWARNID, mBuilder.build());
    }

    static void turnWarning(Context context){
        if (App.INSTANCE.getSharedPreferences().getTurnWarning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = App.INSTANCE.getSharedPreferences().getTurnWarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("Turn Around! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_turn)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.notify(TURNWARNID, mBuilder.build());
            }
        }
    }

    static void batt1Warning(Context context){
        if (App.INSTANCE.getSharedPreferences().getBatt1Warning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = App.INSTANCE.getSharedPreferences().getBatt1WarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("WARNING! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_warn1)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.notify(BATT1WARNID, mBuilder.build());
            }
        }
    }

    static void batt2Warning(Context context){
        if (App.INSTANCE.getSharedPreferences().getBatt2Warning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = App.INSTANCE.getSharedPreferences().getBatt2WarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("WARNING! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_warn2)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.notify(BATT2WARNID, mBuilder.build());
            }
        }
    }

    static void regenWarning(Context context){
        if (App.INSTANCE.getSharedPreferences().getRegenWarning() && lastBatteryLevel >= 0){
            String charging = mOWDevice.characteristics.get(MockOnewheelCharacteristicCharging).value.get();
            if (charging != null && !Boolean.parseBoolean(charging)) {
                int battery = Util.parseI(mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
                int thresh = App.INSTANCE.getSharedPreferences().getRegenWarningValue();
                if (battery == thresh && lastBatteryLevel < thresh) {
                    createChannelNorm(context);

                    Intent i = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                    NotificationCompat.Builder mBuilder;

                    mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                            //.setContentTitle(text)
                            .setContentText("Regeneration Warning! Battery at " + battery + "%!")
                            .setSmallIcon(R.drawable.ic_status_bar_batt_regen)
                            .setChannelId(NORMJOBID + "")
                            .setContentIntent(pi)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.notify(REGENWARNID, mBuilder.build());
                }
            }
        }
    }

    static boolean canWarnSpeed = true;
    public static void speedWarning(Context context){
        if (App.INSTANCE.getSharedPreferences().getSpeedWarning()){
            String speed = mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > App.INSTANCE.getSharedPreferences().getSpeedWarningValue()){
                    if (canWarnSpeed) {
                        canWarnSpeed = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("WARNING! Going too fast!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(SPEEDWARNID, mBuilder.build());
                    }
                }else{
                    canWarnSpeed = true;
                }
            }
        }
    }

    static boolean canWarnSpeed2 = true;
    public static void speedWarning2(Context context){
        if (App.INSTANCE.getSharedPreferences().getSpeedWarning2()){
            String speed = mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > App.INSTANCE.getSharedPreferences().getSpeedWarningValue2()){
                    if (canWarnSpeed2) {
                        canWarnSpeed2 = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("WARNING! Going too fast!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(SPEED2WARNID, mBuilder.build());
                    }
                }else{
                    canWarnSpeed2 = true;
                }
            }
        }
    }

    static boolean canNotifySensor = true;
    public static void sensorWarning(Context context){
        if (App.INSTANCE.getSharedPreferences().getSensorWarning()){
            String pad = mOWDevice.characteristics.get(OnewheelCharacteristicStatusError).value.get();
            String pad1 = mOWDevice.characteristics.get(MockOnewheelCharacteristicPad1).value.get();
            String pad2 = mOWDevice.characteristics.get(MockOnewheelCharacteristicPad2).value.get();
            String speed = mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (pad != null && pad1 != null && pad2 != null && speed != null){
                boolean s = Boolean.parseBoolean(pad);
                boolean s1 = Boolean.parseBoolean(pad1);
                boolean s2 = Boolean.parseBoolean(pad2);
                int sp = Util.parseI(speed);
                if (sp > 30 && (!s || !s1 || !s2)){
                    if (canNotifySensor) {
                        canNotifySensor = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("Warning, Footpad Sensor off at speed!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(SENSORWARNID, mBuilder.build());
                    }
                }else if (s && s1 && s2){
                    canNotifySensor = true;
                }
            }
        }
    }

    static boolean canNotifySpeed = true;
    public static void speedNotification(Context context){
        if (App.INSTANCE.getSharedPreferences().getSpeedRecordNot()){
            String speed = mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > App.INSTANCE.getSharedPreferences().getSpeedRecord()){
                    if (canNotifySpeed) {
                        canNotifySpeed = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("Congrats, New Speed Record! Don't Die!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(SPEEDRECID, mBuilder.build());
                    }
                }else{
                    canNotifySpeed = true;
                }
            }
        }
    }
    public static void generalNotification (Context context, String message){
        createChannelNorm(context);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder;

        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                //.setContentTitle(text)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_status_bar)
                .setChannelId(NORMJOBID + "")
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(GENERICID, mBuilder.build());
    }
}
