package com.example.notes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Edit extends AppCompatActivity implements FeelingDialog.FeelingDialogListener, MapDialog.MapDialogListener {
    long nId;
    NoteDatabase db;
    Double latitude, longitude;
    final int REQUEST_PERMISSION_CODE = 1000;
    int PICK_IMAGE = 0;
    EditText nTitle, nContent;
    VideoView videoView;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ImageView imageView, colorView;
    String title, content, photo = "", last_modification, creation, record = "";
    TextView latLong, createdView, lastModification, feelingView, locationLabel, feelingLabel, createdLabel, updatedLabel;
    String feeling;
    ImageButton removeFeeling, removeLocation;
    int selectedColor;
    ImageButton recordBtn;
    Boolean recording = false;
    MediaRecorder mediaRecorder;
    TextView recordPlayer;
    ImageButton removeRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        Intent i = getIntent();
        nId = i.getLongExtra("ID", 0);
        db = new NoteDatabase(this);
        Note note = db.getNote(nId);
        title = note.getTitle();
        content = note.getContent();
        photo = note.getPhoto();
        latitude = note.getLatitude();
        longitude = note.getLongitude();
        feeling = note.getFeeling();
        creation = note.getCreation();
        selectedColor = note.getColor();
        record = note.getRecord();
        last_modification = note.getLast_modification();
        actionBar.setTitle(title);
        locationLabel = findViewById(R.id.locationLabel);
        feelingLabel = findViewById(R.id.feelingView);
        recordPlayer = findViewById(R.id.musicPlayer);
        colorView = findViewById(R.id.colorView);
        nTitle = findViewById(R.id.noteTitle);
        recordBtn = findViewById(R.id.recordBtn);
        createdLabel = findViewById(R.id.createdLabel);
        updatedLabel = findViewById(R.id.updatedLabel);
        removeRecord = findViewById(R.id.removeRecord);
        recordPlayer.setText(getResources().getString(R.string.record_player));
        recordPlayer.setVisibility(View.INVISIBLE);
        removeRecord.setVisibility(View.INVISIBLE);
        recordBtn.setVisibility(View.INVISIBLE);
        videoView = findViewById(R.id.recordView);
        videoView.setBackgroundResource(R.color.colorPrimary);
        nContent = findViewById(R.id.noteDetails);
        imageView = findViewById(R.id.imageView);
        latLong = findViewById(R.id.latLongView);
        feelingView = findViewById(R.id.feeling);
        createdView = findViewById(R.id.creationView);
        lastModification = findViewById(R.id.updatedView);
        removeLocation = findViewById(R.id.removeLocation);
        removeFeeling = findViewById(R.id.removeFeeling);
        removeLocation.setVisibility(View.INVISIBLE);
        removeFeeling.setVisibility(View.INVISIBLE);
        locationLabel.setVisibility(View.INVISIBLE);
        feelingLabel.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);


        createdLabel.getResources().getString(R.string.created);
        updatedLabel.getResources().getString(R.string.updated);

        String creationAgo = TimeAgo.getTimeAgo(Long.parseLong(creation), getResources());
        createdView.setText(creationAgo);

        if (last_modification != null) {
            String updatedModification = TimeAgo.getTimeAgo(Long.parseLong(last_modification), getResources());
            lastModification.setText(updatedModification);
        }

        feelingView.setText(feeling);
        nTitle.setText(title);
        nContent.setText(content);


        if (note.getPhoto() != null) {
            imageView.setImageURI(Uri.parse(photo));
        }
        if (feeling != null) {
            feelingLabel.setVisibility(View.VISIBLE);
            removeFeeling.setVisibility(View.VISIBLE);
        }
        if (longitude != null || latitude != null) {
            locationLabel.setVisibility(View.VISIBLE);
            removeLocation.setVisibility(View.VISIBLE);
            latLong.setText(String.format("lat: %.3f\nlong: %.3f ", latitude, longitude));
        }
        if (!record.isEmpty()) {
            recordPlayer.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(record);
            removeRecord.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(Edit.this);
            videoView.setMediaController(mediaController);
        }
        if (selectedColor != 0) {
            colorView.setBackgroundColor(selectedColor);
        }
        removeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = null;
                longitude = null;
                latLong.setText("");
                removeLocation.setVisibility(View.INVISIBLE);
                locationLabel.setVisibility(View.INVISIBLE);

            }
        });
        removeFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeling = "";
                feelingView.setText("");
                removeFeeling.setVisibility(View.INVISIBLE);
                feelingLabel.setVisibility(View.INVISIBLE);
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Edit.this)
                        .setTitle("Remove picture")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photo = "";
                                imageView.setImageURI(null);
                            }
                        });
                builder.show();
                return true;
            }
        });
        removeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record = "";
                removeRecord.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                recordPlayer.setVisibility(View.INVISIBLE);
            }
        });
        colorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Edit.this)
                        .setTitle("Remove color")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedColor = 0;
                                colorView.setBackgroundColor(Color.WHITE);
                            }
                        });
                builder.show();
                return true;

            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = !recording;
                if (recording) {
                    Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
                    recordBtn.setImageResource(R.drawable.ic_stop_black_24dp);
                    record = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    mediaRecorder.stop();
                    recordPlayer.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Recording stopped.\nPlease click on bottom toolbar to play record.", Toast.LENGTH_LONG).show();
                    recordBtn.setVisibility(View.INVISIBLE);
                    removeRecord.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(record);
                    MediaController mediaController = new MediaController(Edit.this);
                    videoView.setMediaController(mediaController);
                }
            }
        });


        nTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (nTitle.getText().toString().isEmpty() && nContent.getText().toString().isEmpty() && photo.isEmpty()) {
                    builder = new AlertDialog.Builder(Edit.this);
                    builder.setTitle("Error!");
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setMessage("You cant save empty note.");
                    builder.setNegativeButton("Okay", null);
                    dialog = builder.create();
                    dialog.show();
                } else {
                    last_modification = String.valueOf(System.currentTimeMillis());

                    Note note = new Note(nId, nTitle.getText().toString(), nContent.getText().toString(), photo, latitude, longitude, feeling, creation, last_modification, record, selectedColor);
                    Log.d("EDITED", "edited: before saving id -> " + note.getID());
                    NoteDatabase db = new NoteDatabase(getApplicationContext());
                    long id = db.editNote(note, Edit.this);
                    Log.d("EDITED", "EDIT: id " + id);
                    goToMain();
                }
                break;
            case R.id.delete:
                builder = new AlertDialog.Builder(Edit.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteDatabase db = new NoteDatabase(getApplicationContext());
                        db.deleteNote(nId, Edit.this);
                        goToMain();
                    }
                });
                builder.setCancelable(false);
                builder.setNegativeButton("No", null);
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.photo:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
                break;
            case R.id.location:
                if (locationPermission()) {
                    mapDialog();
                } else {
                    requestLocationPermission();
                }
                break;
            case R.id.feeling:
                openDialog();
                break;
            case R.id.record:
                if (checkSelfPermission() && record.isEmpty()) {
                    recordBtn.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);

                } else {
                    requestPermission();
                }
                break;
            case R.id.color:
                colorDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkSelfPermission() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

    private void mapDialog() {
        MapDialog dialog = new MapDialog(Edit.this, latitude, longitude);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Map dialog");
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(record);
    }

    private boolean locationPermission() {
        int fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        return fineLocation == PackageManager.PERMISSION_GRANTED && coarseLocation == PackageManager.PERMISSION_GRANTED;
    }

    private void colorDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(Edit.this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                colorView.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_PERMISSION_CODE);
        mapDialog();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private void openDialog() {
        FeelingDialog dialog = new FeelingDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Feeling dialog");
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            photo = String.valueOf(data.getData());
            imageView.setImageURI(Uri.parse(photo));
        }
    }

    @Override
    public void applyFeeling(String rating) {
        if (rating != null) {
            feelingLabel.setVisibility(View.VISIBLE);
            feelingView.setText(rating);
            removeFeeling.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void applyCordinates(Double lati, Double longi) {
        if (lati != null & longi != null) {
            latitude = lati;
            longitude = longi;
            latLong.setText(String.format("lat: %.5f\nlong: %.5f ", latitude, longitude));
            removeLocation.setVisibility(View.VISIBLE);
            locationLabel.setVisibility(View.VISIBLE);
        }
    }
}