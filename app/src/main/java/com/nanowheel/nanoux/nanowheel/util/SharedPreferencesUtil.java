package com.nanowheel.nanoux.nanowheel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nanowheel.nanoux.nanowheel.FragmentDiagnotics;


public class SharedPreferencesUtil{

    public static final String METRIC_UNITS = "metricUnits";
    public static final String LOG_TRIPS = "mapTraceMode";
    public static final String FLOAT_STAT = "floatingStatus";
    public static final String DARK_NIGHT_MODE = "darkNightMode";
    private static final String DEBUG_WINDOW = "debugWindow";
    public static final String OW_MAC_ADDRESS = "ow_mac_address";
    public static final String OW_MAC_NAME = "ow_mac_name";
    public static final String STATUS_MODE = "status_mode";
    public static final String FLOAT_POS_X = "floatPosX";
    public static final String FLOAT_POS_Y = "floatPosY";
    public static final String FLOAT_EXTEND = "floatExtended";
    private static final String CHARGEDISTVAL = "charge_distance_value";
    private static final String CHARGEDISTBAT = "change_distance_battery_level";
    public static final String RECORD_SPEED = "alltime_record_speed";
    public static final String RECORD_DIST = "alltime_record_distance";
    public static final String TOTAL_DIST = "alltime_distance";
    private static final String DIALOG_ACCEPTED = "data_dialog";
    private static final String GEMINI_DIALOG_ACCEPTED = "gemini_data_dialog";
    private static final String CHARGING_NOT = "warningCharge";
    private static final String CHARGING_NOT_VALUE = "warningChargeValue";
    private static final String TURN_NOT = "warningTurn";
    private static final String TURN_NOT_VALUE = "warningTurnThresh";
    private static final String BATT1_NOT = "warningBatt1";
    private static final String BATT1_NOT_VALUE = "warningBattThresh1";
    private static final String BATT2_NOT = "warningBatt2";
    private static final String BATT2_NOT_VALUE = "warningBattThresh2";
    private static final String SPEED_NOT = "warningSpeed";
    private static final String SPEED_NOT_VALUE = "warningSpeedThresh";
    private static final String SPEED2_NOT = "warningSpeed2";
    private static final String SPEED2_NOT_VALUE = "warningSpeedThresh2";
    private static final String REGEN_NOT = "warningRegen";
    private static final String REGEN_NOT_VALUE = "warningRegenThresh";
    private static final String SENSOR_NOT = "notificationSensor";
    private static final String SPEED_REC_NOT = "notificationSpeed";
    private static final String CHALLENGE_EPOCH = "callengeEpoch";

    private SharedPreferences androidSharedPreferences;


    public SharedPreferencesUtil(Context context) {
        androidSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putInt(int value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putBool(boolean value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(float value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putLong(long value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setStatusMode(int status){
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(STATUS_MODE,status);
        editor.commit();
    }
    public int getStatusMode(){
        return androidSharedPreferences.getInt(STATUS_MODE,0);
    }

    public boolean isDarkNightMode() {
        return androidSharedPreferences.getBoolean(DARK_NIGHT_MODE, false);
    }

    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        androidSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public Boolean isDebugging() {
        return androidSharedPreferences.getBoolean(DEBUG_WINDOW, false);
    }

    public void removeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        androidSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    public void saveMacAddress(String macAdress, String macAddressName) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putString(OW_MAC_ADDRESS, macAdress);
        editor.putString(OW_MAC_NAME,macAddressName);
        editor.commit();
    }

    public String getMacAddress(){
        return androidSharedPreferences.getString(OW_MAC_ADDRESS,"");
    }

    public boolean isMetric() {
        return androidSharedPreferences.getBoolean(METRIC_UNITS, false);
    }

    public boolean isMap() {
        return androidSharedPreferences.getBoolean(LOG_TRIPS, false);
    }

    public boolean isFloatStat() {
        return androidSharedPreferences.getBoolean(FLOAT_STAT, false);
    }

    public boolean isFloatExtend() {
        return androidSharedPreferences.getBoolean(FLOAT_EXTEND, true);
    }

    public float[] getFloatPos() {
        return new float[] {androidSharedPreferences.getFloat(FLOAT_POS_X, 0),androidSharedPreferences.getFloat(FLOAT_POS_Y, 100)};
    }

    public void setFloatPos(float x, float y){
        putFloat(x,FLOAT_POS_X);
        putFloat(y,FLOAT_POS_Y);
    }

    public void setMap(boolean bool) { putBool(bool,LOG_TRIPS); }

    public void setFloatExtend(boolean bool) { putBool(bool,FLOAT_EXTEND); }

    public void setFloatStat(boolean bool) { putBool(bool,FLOAT_STAT); }


    public boolean getChargeWarning(){
        return androidSharedPreferences.getBoolean(CHARGING_NOT,true);
    }
    public int getChargeWarningValue(){
        String temp = androidSharedPreferences.getString(CHARGING_NOT_VALUE,"100%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getTurnWarning(){
        return androidSharedPreferences.getBoolean(TURN_NOT,true);
    }
    public int getTurnWarningValue(){
        String temp = androidSharedPreferences.getString(TURN_NOT_VALUE,"50%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getBatt1Warning(){
        return androidSharedPreferences.getBoolean(BATT1_NOT,true);
    }
    public int getBatt1WarningValue(){
        String temp = androidSharedPreferences.getString(BATT1_NOT_VALUE,"10%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getBatt2Warning(){
        return androidSharedPreferences.getBoolean(BATT2_NOT,false);
    }
    public int getBatt2WarningValue(){
        String temp = androidSharedPreferences.getString(BATT2_NOT_VALUE,"5%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getSpeedWarning2(){
        return androidSharedPreferences.getBoolean(SPEED2_NOT,false);
    }
    public int getSpeedWarningValue2(){
        String temp = androidSharedPreferences.getString(SPEED2_NOT_VALUE,"488");
        return Util.parseI(temp);
    }
    public boolean getSpeedWarning(){
        return androidSharedPreferences.getBoolean(SPEED_NOT,true);
    }
    public int getSpeedWarningValue(){
        String temp = androidSharedPreferences.getString(SPEED_NOT_VALUE,"413");
        return Util.parseI(temp);
    }
    public boolean getRegenWarning(){
        return androidSharedPreferences.getBoolean(REGEN_NOT,true);
    }
    public int getRegenWarningValue(){
        String temp = androidSharedPreferences.getString(REGEN_NOT_VALUE,"99%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getSensorWarning(){
        return androidSharedPreferences.getBoolean(SENSOR_NOT,true);
    }
    public boolean getSpeedRecordNot(){
        return androidSharedPreferences.getBoolean(SPEED_REC_NOT,true);
    }

    public int getSpeedRecord(){
        return androidSharedPreferences.getInt(RECORD_SPEED,0);
    }
    public void setSpeedRecord(int speed){
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(RECORD_SPEED, speed);
        editor.commit();
    }
    public int getDistanceRecord(){
        return androidSharedPreferences.getInt(RECORD_DIST,0);
    }
    public void setDistanceRecord(int dist){
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(RECORD_DIST, dist);
        editor.commit();
    }
    public int getDistanceTotal(){
        return androidSharedPreferences.getInt(TOTAL_DIST,0);
    }
    public void setDistanceTotal(int dist){
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(TOTAL_DIST, dist);
        editor.commit();
    }

    public float getChargeDistRev(){
        return androidSharedPreferences.getFloat(CHARGEDISTVAL,0);
    }
    public void setChargeDistRev(float a){
        putFloat(a,CHARGEDISTVAL);
    }
    public float getChargeDistBat(){
        return androidSharedPreferences.getFloat(CHARGEDISTBAT,0);
    }
    public void setChargeDistBat(float a){
        putFloat(a,CHARGEDISTBAT);
    }
    public boolean getDialogAccepted(){
        return androidSharedPreferences.getBoolean(DIALOG_ACCEPTED,false);
    }
    public void setDialogAccepted(boolean a){
        putBool(a,DIALOG_ACCEPTED);
    }
    public boolean getGeminiDialogAccepted(){
        return androidSharedPreferences.getBoolean(GEMINI_DIALOG_ACCEPTED,false);
    }
    public void setGeminiDialogAccepted(boolean a){
        putBool(a,GEMINI_DIALOG_ACCEPTED);
    }

}