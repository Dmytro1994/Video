package com.example.dmytro.tryvideo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dmytro_Ubogiy on 10.11.2015.
 */
public class Video {
    private String name;
    private String expand;

    public Video() {
        name = generateName();
        expand = ".mp4";
    }

    private String generateName() {
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
        return "VID_" + timeStamp;
    }

    public String getExpand() {
        return expand;
    }

    public String getName() {
        return name;
    }
}
