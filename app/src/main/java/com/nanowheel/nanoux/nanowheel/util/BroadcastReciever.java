package com.nanowheel.nanoux.nanowheel.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case "com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL":
               cancel(context);
               break;

            case "com.nanowheel.nanoux.nanowheel.LIGHTS":
                lights();
                break;
        }
    }
    void cancel(Context context){
        BluetoothService.killService(context,false);
    }
    void lights(){
        if (BluetoothService.mOWDevice.isLit){
            BluetoothService.mOWDevice.setLights(BluetoothService.getBluetoothUtil(), 0);
        }else{
            BluetoothService.mOWDevice.setLights(BluetoothService.getBluetoothUtil(), 1);
        }
    }
}
