package com.sachinchandil.videodownloadandplay;

import android.app.Activity;

import java.io.File;

/**
 * Created by BBI-M1025 on 15/05/17.
 */

public class VideoDownloadAndPlayService
{
    private static LocalFileStreamingServer server;

    private VideoDownloadAndPlayService(LocalFileStreamingServer server)
    {
        this.server = server;
    }

    public static VideoDownloadAndPlayService startServer(final Activity activity, String videoUrl, String pathToSaveVideo, final String ipOfServer, final VideoStreamInterface callback)
    {
        new VideoDownloader().execute(videoUrl, pathToSaveVideo);
        server = new LocalFileStreamingServer(new File(pathToSaveVideo));
        server.setSupportPlayWhileDownloading(true);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                server.init(ipOfServer);

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        server.start();
                        callback.onServerStart(server.getFileUrl());
                    }
                });
            }
        }).start();

        return new VideoDownloadAndPlayService(server);
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop();
    }

    public static interface VideoStreamInterface{
        public void onServerStart(String videoStreamUrl);
    }
}
