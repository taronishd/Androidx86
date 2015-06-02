/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.content.pm.PackageManager;           //Added
import android.content.ActivityNotFoundException;   //Added
import android.content.Context;                     //Added
import android.content.BroadcastReceiver;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.WindowManager;
import android.content.Context;

// import com.android.systemui.statusbar.phone.SettingsPanelView;  //Added
// import com.android.systemui.statusbar.phone.StatusBarWindowView;  //Added


import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;



public class SystemUIService extends Service {
    private static final String TAG = "SystemUIService";

    /**
     * The classes of the stuff to start.
     */
    private final Class<?>[] SERVICES = new Class[] {
            com.android.systemui.recent.Recents.class,
            com.android.systemui.statusbar.SystemBars.class,
            com.android.systemui.usb.StorageNotification.class,
            com.android.systemui.power.PowerUI.class,
            com.android.systemui.media.RingtonePlayer.class,
            com.android.systemui.settings.SettingsUI.class,
        };

    /**
     * Hold a reference on the stuff we start.
     */
    private final SystemUI[] mServices = new SystemUI[SERVICES.length];

    @Override
    public void onCreate() {
        HashMap<Class<?>, Object> components = new HashMap<Class<?>, Object>();
        final int N = SERVICES.length;
        for (int i=0; i<N; i++) {
            Class<?> cl = SERVICES[i];
            Log.d(TAG, "loading: " + cl);
            try {
                mServices[i] = (SystemUI)cl.newInstance();
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            }
            mServices[i].mContext = this;
            mServices[i].mComponents = components;
            Log.d(TAG, "running: " + mServices[i]);
            mServices[i].start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        for (SystemUI ui: mServices) {
            ui.onConfigurationChanged(newConfig);
        }
    }

    /**
     * Nobody binds to us.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (args == null || args.length == 0) {
            for (SystemUI ui: mServices) {
                pw.println("dumping service: " + ui.getClass().getName());
                ui.dump(fd, pw, args);
            }
        } else {
            String svc = args[0];
            for (SystemUI ui: mServices) {
                String name = ui.getClass().getName();
                if (name.endsWith(svc)) {
                    ui.dump(fd, pw, args);
                }
            }
        }
    }




    // public class ActionReceiver extends BroadcastReceiver {
    //     @Override
    //     public void onReceive(Context context, Intent intent) {
    //         if ("packages.apps.Trebuchet.menuButtonClick.shutdown".equalsIgnoreCase(intent.getAction())) {
    //             final AlertDialog dialog = new AlertDialog.Builder(context)
    //                 .setTitle(com.android.internal.R.string.power_off)
    //                 .setMessage(com.android.internal.R.string.shutdown_confirm_question)
    //                 .setPositiveButton(com.android.internal.R.string.yes,
    //                     new DialogInterface.OnClickListener() {
    //                     public void onClick(DialogInterface dialog, int which) {
    //                         // Send request to start ShutdownActivity
    //                         Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
    //                         intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
    //                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //                         context.startActivity(intent);
    //                     }
    //                 })
    //                 .setNegativeButton(com.android.internal.R.string.no, null)
    //                 .create();
    //                 dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    //                 dialog.show();
    //         }
    //     }
    // }





    public void onClickZlaunch (View v) {
        
        // Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");

        // try {
        //     startActivity(launchIntent);  
        // }
        // catch(ActivityNotFoundException e){
        //     Log.w(TAG, "Activity not found for " + launchIntent.getAction());
        // }

        
        // TODO send to just launcher
        Intent it = new Intent("com.android.SystemUI.showallapps");
        sendBroadcast(it);

        
        // SettingsPanelView mSettingsPanel;
        // StatusBarWindowView mStatusBarWindow;
        // mSettingsPanel = (SettingsPanelView) mStatusBarWindow.findViewById(R.id.settings_panel);
        // mSettingsPanel.expand();

        
    }
}

