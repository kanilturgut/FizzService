package com.kanilturgut.fizzservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.kanilturgut.mylib.Logs;

public class MyService extends Service {

    Context context = this;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Logs("Fizz");

        //TODO degisecek
        LoginTask loginTask = new LoginTask(context);
        loginTask.execute("fizz@fizz.com", "123");

        return START_STICKY;
    }
}
