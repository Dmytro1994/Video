package com.example.dmytro.tryvideo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmytro_Ubogiy on 16.11.2015.
 */
public class ListActivityVideo extends ListActivity {
    private ArrayAdapter<String> adapter;
    private static List<String> mediaFiles = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaFiles = getVideos();
        adapter = new ArrayAdapter<String>(this, R.layout.custom_adapter, mediaFiles){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.custom_adapter, parent, false);
                }
                TextView videoName = (TextView) convertView.findViewById(R.id.textView);
                String vName = mediaFiles.get(position);
                videoName.setText(vName);
                return convertView;
            }
        };
        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String absPath = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(absPath));
                intent.setDataAndType(Uri.parse( FileWork.getStorageMediaPath() + File.separator + absPath), "video/mp4");
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String vName = getVideos().get(position);
                new AlertDialog.Builder(ListActivityVideo.this)
                        .setTitle("Deleting video")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                if (FileWork.deleteVideoFile(FileWork.getStorageMediaPath() + File.separator + vName)) {
                                    mediaFiles.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                return true;
            }
        });
    }

    public  List<String> getVideos() {
        File videos = new File(FileWork.getStorageMediaPath());
        File[] videosList = videos.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return (filename.endsWith("part2.mp4") ||filename.endsWith("concat.mp4"));
            }
        });
        List<String> mediaFiles = new ArrayList<>();
        for (int i = 0; i < videosList.length; i++) {
            mediaFiles.add(videosList[i].getName());
        }
        return mediaFiles;
    }
}
