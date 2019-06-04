package com.example.projectfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UserActivityLogBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.codinginflow.SAVE_USER_ACTIVITY".equals(intent.getAction())) {
            String receivedText = intent.getStringExtra("activity");
            UserActivitesDataBaseHelper dbHelper = new UserActivitesDataBaseHelper(context);
            String str = "from broad cast receiver";
            str += dbHelper.addData(receivedText);
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }
}
