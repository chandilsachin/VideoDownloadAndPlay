package com.sachinchandil.videodownloadandplay;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BBI-M1025 on 15/05/17.
 */

public class VideoDownloader extends AsyncTask<String, Integer, Void>
{
    public static final int DATA_READY = 1;
    public static final int DATA_NOT_READY = 2;
    public static final int DATA_CONSUMED = 3;
    public static final int DATA_NOT_AVAILABLE = 4;


    public static int dataStatus = -1;

    public static boolean isDataReady(){
        dataStatus = -1;
        boolean res = false;
        if(fileLength == readb){
            dataStatus = DATA_CONSUMED;
            res = false;
        }else if(readb > consumedb){
            dataStatus = DATA_READY;
            res = true;
        }else if(readb <= consumedb){
            dataStatus = DATA_NOT_READY;
            res = false;
        }else if(fileLength == -1){
            dataStatus = DATA_NOT_AVAILABLE;
            res = false;
        }
        return res;
    }

    /**
     * Keeps track of read bytes while serving to video player client from server
     */
    public static int consumedb = 0;

    /**
     * Keeps track of all bytes read on each while iteration
     */
    private static int readb = 0;

    /**
     * Length of file being downloaded.
     */
    static int fileLength = -1;

    @Override
    protected Void doInBackground(String... params)
    {


        BufferedInputStream input = null;
        try
        {
            final FileOutputStream out = new FileOutputStream(params[1]);

            try
            {
                URL url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    throw new RuntimeException("response is not http_ok");
                }
                fileLength = connection.getContentLength();

                input = new BufferedInputStream(connection.getInputStream());
                byte data[] = new byte[1024 * 50];
                long readBytes = 0;
                int len;
                boolean flag = true;

                while ((len = input.read(data)) != -1)
                {
                    out.write(data, 0, len);
                    out.flush();
                    readBytes += len;
                    readb += len;
                    Log.w("download", (readb / 1024) + "kb of " + (fileLength / 1024) + "kb");
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (out != null)
                {
                    out.flush();
                    out.close();
                }
                if (input != null)
                    input.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        Log.w("download", "Done");
    }
}