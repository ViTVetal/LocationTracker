package com.gocodes.locationtracker.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import static android.content.Context.MODE_APPEND;

/**
 * Created by vit-vetal- on 02.11.17.
 */

public class LogWriter {
    public static void writeToFile(String data) {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/YourFolder/"
                        );

        if(!path.exists()) {
            path.mkdirs();
        }

        final File file = new File(path, "config.txt");

        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file,true);
            writer.write(Calendar.getInstance().getTime() + " " + data + "\n");
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
