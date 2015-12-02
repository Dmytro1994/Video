package com.example.dmytro.tryvideo;

import android.content.Context;
import android.os.Environment;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmytro_Ubogiy on 13.11.2015.
 */
public class VideoWork {

    public static String cutSecondPart(String inputPath, String outputPath, int startPosition, int duration) {
        return "-i " + inputPath + " -ss " + convertSecondsToHMmSs(startPosition) + " -to " + convertSecondsToHMmSs(duration) +
                " -c copy " + outputPath;
    }

    public static String cutFirstPart(String inputPath, String outputPath, int startPosition) {
        return "-i " + inputPath + " -ss 00:00:00 -to " + convertSecondsToHMmSs(startPosition) +
                " -c copy " + outputPath;
    }

    private static String convertSecondsToHMmSs(int millis) {

        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private static File createFileForConcat(String videoPath1, String videoPath2) {
        File concatVideoFile = null;
        try
        {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraVideo");
            if (!root.exists()) {
                root.mkdirs();
            }
            concatVideoFile = new File(root, "videos.txt");
            FileWriter writer = new FileWriter(concatVideoFile);
            writer.append("file " + "\'" + videoPath1 + "\'\n");
            writer.append("file " + "\'" + videoPath2 + "\'");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return concatVideoFile;
    }

    public static String concatVideo(String videoPath1, String videoPath2, String resultPathVideo) {
        File txtFile = createFileForConcat(videoPath1, videoPath2);
        String txtFilePath = txtFile.getAbsolutePath();
        return "-f concat -i " + txtFilePath + " -c copy " + resultPathVideo;
    }

    public static void workVideo(String cmd, Context context) {
        FFmpeg cutVideo = FFmpeg.getInstance(context);
        try {
            cutVideo.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                }

                @Override
                public void onFinish() {
                }
            });

        }

        catch(FFmpegNotSupportedException e) {
            e.printStackTrace();
        }

        try {
            cutVideo.execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(String message) {

                }

                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onSuccess(String message) {
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}
