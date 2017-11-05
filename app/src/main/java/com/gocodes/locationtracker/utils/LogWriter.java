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
    public static void writeToFile(String data)
    {
     /*   // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/YourFolder/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "config.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file,true);
            writer.write(Calendar.getInstance().getTime() + " " + data + "\n");
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        } */
    }
}
