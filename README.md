# VideoDownloadAndPlay
[ ![Download](https://api.bintray.com/packages/chandilsachin/ace/videodownloadandplay/images/download.svg) ](https://bintray.com/chandilsachin/ace/videodownloadandplay/_latestVersion)

Library to play video while downloading, with same data stream. No double data cost.

When you have a use case of playing a video while it is still downloading, this lib is for you. It also saves data cost as only one stream is used to download and downloaded data is then used to play video.


**Add Dependency in your build.gradle file**

    dependencies {
        ...
        ...
        compile 'com.sachinchandil:videodownloadandplay:1.0.0'
    }


**How to use:**

```java
private VideoDownloadAndPlayService videoService;
    
private void startServer()
{
    videoService = VideoDownloadAndPlayService.startServer(getActivity(), videoPath,outFilePath, serverPath, new VideoDownloadAndPlayService.VideoStreamInterface()
    {
        @Override
        public void onServerStart(String videoStreamUrl)
        {
            // use videoStreamUrl to play video through media player
        }
    });
}
```

Call startServer() to start downloading. 

And,

Don't forget to stop video server onStop() of activity.

```java
@Override
public void onStop()
{
    super.onStop();
    if(videoService != null)
        videoService.stop();
}
```