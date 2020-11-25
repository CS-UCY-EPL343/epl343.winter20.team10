package com.example.reminders;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
/**
 * This class is for edit or add a reminder.
 *
 */
public class EditReminder extends AppCompatActivity {

    private Button saveButton, setPicButton;
    private TextView nameText, date, time;
    private String videoPath;
    private VideoView videoView;
    private static final int SAVE_OP=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_reminder);

        videoView = (VideoView) findViewById(R.id.videoView);
        setPicButton = (Button) findViewById(R.id.setPicButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        nameText = (TextView) findViewById(R.id.nameText);
        date = (TextView) findViewById(R.id.date_text);
        time = (TextView) findViewById(R.id.time_text);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPath == null && getIntent().getIntExtra("requestCode",-1)==-1) {
                    AlertDialog.Builder err = new AlertDialog.Builder(EditReminder.this);
                    err.setTitle("No video selected!!");
                    err.setMessage("Please select video to continue");
                    err.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    err.show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("name", nameText.getText().toString() + " ," +
                             date.getText().toString() + " ," + time.getText().toString());
                    bundle.putSerializable("video", videoPath);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        setPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoFromGallery();
            }
        });

        if(handleRequestCode()==1005) {//edit
            videoView.setVisibility(View.GONE);
            setPicButton.setVisibility(View.GONE);
        }
    }

    /**
     * This method handles if the request is for add or edit.
     *
     */
    private int handleRequestCode(){
        Intent intent = this.getIntent();
        if(intent.getIntExtra("requestCode",-1)==1005){    //edit reminder
            String name = intent.getStringExtra("name");
            String path = intent.getStringExtra("video");
            String[] arr = name.split(",");
            nameText.setText(arr[0]);
            date.setText(arr[1]);
            time.setText(arr[2]);
//            videoView.setVideoURI(Uri.parse(path));
//            MediaController med = new MediaController(this);
//            med.setAnchorView(videoView);
//            videoView.setVideoURI(Uri.parse(videoPath));
//            videoView.setMediaController(med);
//            videoView.start();
        }
        return intent.getIntExtra("requestCode",-1);
    }

    /**
     * This method opens the gallery for user, to choose a video.
     *
     */
    private void pickVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), SAVE_OP);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SAVE_OP) {
            //select video
            Uri uri = null;
            uri = uri.parse(data.getData() + "");
            videoPath = data.getData().toString();
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }

}