package com.kanilturgut.fizzservice;

import android.content.Context;

import com.kanilturgut.fizzservice.model.Venue;
import com.kanilturgut.fizzservice.task.DownloadAPKTask;
import com.kanilturgut.fizzservice.task.SendInstructionResultTask;
import com.kanilturgut.mylib.Logs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author   : kanilturgut
 * Date     : 01/07/14
 * Time     : 15:46
 */
public class ADB {

    private static final String TAG = ADB.class.getSimpleName();

    public static String FIZZ_FILENAME;
    public static String FIZZ_DOWNLOAD_TARGET;
    public static String FIZZ_DOWNLOAD_URL;

    public static String OP_INSTALL;
    public static String OP_UNINSTALL;
    public static String OP_UNINSTALL_WITH_CACHE;
    public static String OP_LAUNCH_APP;
    public static String OP_SHOW_PROCESS;
    public static String OP_KILL;
    public static String OP_LOGCAT;
    public static String OP_LOGCAT_CLEAR;
    public static String OP_CP_APK_TO_SYSTEM;


    Context context = null;
    public static ADB adb = null;

    public static ADB getInstance(Context context) {
        if (adb == null)
            adb = new ADB(context);

        return adb;
    }

    private ADB(Context context) {
        this.context = context;

        FIZZ_FILENAME = "/Fizz.apk";
        FIZZ_DOWNLOAD_TARGET = context.getFilesDir().getAbsolutePath() + FIZZ_FILENAME;
        FIZZ_DOWNLOAD_URL = "http://kadiranilturgut.com/android/fizz/Fizz.apk";

        OP_INSTALL = "pm install -r /sdcard/Download/Fizz.apk";
        OP_UNINSTALL = "pm uninstall -k com.kanilturgut.fizz";
        OP_UNINSTALL_WITH_CACHE = "pm uninstall com.kanilturgut.fizz";
        OP_LAUNCH_APP = "am start -n \"com.kanilturgut.fizz/.activity.MainActivity\"";
        OP_SHOW_PROCESS = "ps | grep com.kanilturgut.fizz";
        OP_KILL = "am force-stop com.kanilturgut.fizz";
        OP_LOGCAT = "logcat -ds \"Fizz\" > /sdcard/logcat.txt";
        OP_LOGCAT_CLEAR = "logcat -c";
        OP_CP_APK_TO_SYSTEM = "cp " + FIZZ_DOWNLOAD_TARGET + " /sdcard/Download/Fizz.apk";

    }

    public void install(int instruction) {
        execute(instruction, OP_INSTALL);
    }

    public void uninstall(int instruction) {
        execute(instruction, OP_UNINSTALL);
    }

    public void uninstallWithCache(int instruction) {
        execute(instruction, OP_UNINSTALL_WITH_CACHE);
    }

    public void launchApp(int instruction) {
        execute(instruction, OP_LAUNCH_APP);
    }

    public void showProcess(int instruction) {
        execute(instruction, OP_SHOW_PROCESS);
    }

    public void kill(int instruction) {

        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(OP_KILL + "\n");
            os.flush();
            os.close();
            process.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                stringBuilder.append(s);
            }

            sendInstructionResult(instruction, stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void logcatClear(int instruction) {
        execute(instruction, OP_LOGCAT_CLEAR);
    }

    public void logcat(int instruction) {
        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(OP_LOGCAT + "\n");
            os.writeBytes("logcat -c" + "\n");
            os.flush();
            os.close();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("cat /sdcard/logcat.txt" + "\n");
            os.flush();
            os.close();
            process.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                stringBuilder.append(s);
            }

            sendInstructionResult(instruction, stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void changeDownloadURL(int instruction, String url) {
        Logs.d(TAG, "changeDownloadURL started");
        FIZZ_DOWNLOAD_URL = url;
        sendInstructionResult(instruction, "SUCCESS on Change Download URL to " + url);

    }

    public void showDownloadURL(int instruction) {
        Logs.d(TAG, "showDownloadURL started");
        String url = FIZZ_DOWNLOAD_URL;
        sendInstructionResult(instruction, "Download URL is " + url);
    }

    public void downloadNewAPK(int instruction) {

        Logs.d(TAG, "downloadNewAPK started");

        new DownloadAPKTask(context).execute(FIZZ_DOWNLOAD_URL);
    }

    public void copyAPKToSystem() {
        execute(11, OP_CP_APK_TO_SYSTEM);
    }

    private void execute(int instruction, String command) {

        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            os.close();
            process.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            StringBuilder stringBuilder = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                stringBuilder.append(s);
            }

            sendInstructionResult(instruction, stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendInstructionResult(int instruction, String result) {

        Logs.d(TAG, "Instruction : " + String.valueOf(instruction) + ", result is " + result);

        SendInstructionResultTask sendInstructionResultTask = new SendInstructionResultTask(Venue.getInstance().getId(), instruction, result);
        sendInstructionResultTask.execute();
    }
}
