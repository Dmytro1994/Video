package com.example.dmytro.tryvideo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import java.io.File;



public class MainActivity extends FragmentActivity{

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public VideoView videoView;
    private Uri fileUri;
    private Button recordBtn;
    private Button playBtn;
    private Button cutBtn;
    private Button reRecordBtn;
    private Button concatBtn;
    private MediaController mediaController;
    private Video video;
    private Video reRecordVideo;
    private int currPos = 0;
    private int duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                playBtn.setEnabled(true);
                videoView.setVideoURI(data.getData());
                videoView.seekTo(10);
                videoView.setMediaController(mediaController);
            }
        } else  {
            concatBtn.setEnabled(false);
        }
    }

    public void recordVideo(View view) {
        cutBtn.setEnabled(false);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        video = new Video();
        fileUri = FileWork.getOutputMediaFileUri(MEDIA_TYPE_VIDEO, videoName("_main", video));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }


    public void playVideo(View view) {
        if(videoView.isPlaying()){
            cutBtn.setEnabled(true);
            reRecordBtn.setEnabled(true);
            currPos = videoView.getCurrentPosition();
            videoView.pause();
            playBtn.setText("PLAY");
        }else{
            videoView.start();
            playBtn.setText("STOP");
        }
    }

    public void cutVideoBtn(View view) {
        String cmdCut = VideoWork.cutSecondPart(videoOutputFilePath("_main", video), videoOutputFilePath("_part2", video), currPos, duration);
        VideoWork.workVideo(cmdCut, this);
        cutBtn.setEnabled(false);
        reRecordBtn.setEnabled(false);
        concatBtn.setEnabled(false);
    }

    public void reRecord(View view) {
        cutVideoBtn(view);
        concatBtn.setEnabled(true);
        String cmdCut = VideoWork.cutFirstPart(videoOutputFilePath("_main", video), videoOutputFilePath("_part1", video), currPos);
        VideoWork.workVideo(cmdCut, this);
        reRecordVideoCamera();
        reRecordBtn.setEnabled(false);
    }

    public void concat(View view) {
        String inputVideo1Path = videoOutputFilePath("_part1", video);
        String inputVideo2Path = videoOutputFilePath("_rerecord", reRecordVideo);
        String cmdConcat = VideoWork.concatVideo(inputVideo1Path, inputVideo2Path, videoOutputFilePath("_concat", reRecordVideo));
        VideoWork.workVideo(cmdConcat, this);
        concatBtn.setEnabled(false);
    }

    public void reRecordVideoCamera() {
        reRecordVideo = new Video();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = FileWork.getOutputMediaFileUri(MEDIA_TYPE_VIDEO, videoName("_rerecord", reRecordVideo));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    public void executeListVideo(View view) {
        Intent intent = new Intent(this, ListActivityVideo.class);
        startActivity(intent);
    }

    private void init() {
        videoView = (VideoView) findViewById(R.id.videoView);
        recordBtn = (Button) findViewById(R.id.recording);
        playBtn = (Button) findViewById(R.id.play);
        cutBtn = (Button) findViewById(R.id.cutButton);
        reRecordBtn = (Button) findViewById(R.id.buttonReRecord);
        concatBtn = (Button) findViewById(R.id.buttonConcat);
        playBtn.setEnabled(false);
        cutBtn.setEnabled(false);
        reRecordBtn.setEnabled(false);
        concatBtn.setEnabled(false);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playBtn.setText("PLAY");
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                duration = videoView.getDuration();
            }
        });
    }

    private String videoOutputFilePath(String part, Video video) {
        return FileWork.getStorageMediaPath() + File.separator + video.getName() + part + video.getExpand();
    }

    private String videoName(String part, Video video)  {
        return video.getName() + part + video.getExpand();
    }

}
