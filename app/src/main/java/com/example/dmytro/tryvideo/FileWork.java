package com.example.dmytro.tryvideo;

import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Dmytro_Ubogiy on 13.11.2015.
 */

//some comments to commit to git
//this is my second commit
    //commit fot video1
public class FileWork {
    private static File mediaFile;
    private static File storageMedia;

    private static  void createStorageDir() {
        storageMedia = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraVideo");
        if (!storageMedia.exists()) {
            storageMedia.mkdir();
        }
    }

    private static File getOutputMediaFile(int type, String nameFile) {
        createStorageDir();
        if (type == MainActivity.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(storageMedia.getPath() + File.separator + nameFile);
        } else {
            return null;
        }
        return mediaFile;
    }

    public static  boolean deleteVideoFile(String path) {
        File deleteFile = new File(path);
        return deleteFile.delete();
    }

    public static  Uri getOutputMediaFileUri(int type, String nameFile) {
        return Uri.fromFile(getOutputMediaFile(type, nameFile));
    }

    public static  String getStorageMediaPath() {
        createStorageDir();
        return storageMedia.getAbsolutePath();
    }
}
