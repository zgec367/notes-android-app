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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddNote extends AppCompatActivity implements FeelingDialog.FeelingDialogListener, MapDialog.MapDialogListener {

    Double latitude, longitude;
    final int REQUEST_PERMISSION_CODE = 1000;
    int PICK_IMAGE = 0, selectedColor;
    EditText noteTitle, noteDetails;
    TextView latLong, feelingView, locationLabel, feelingLabel, recordPlayer;
    String creation, selectedPhoto = "", feeling, record = "";
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ImageView imageView, colorView;
    ImageButton removeFeeling, removeLocation, recordingBtn, removeRecord;
    boolean recording = false;
    MediaRecorder mediaRecorder;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        imageView = findViewById(R.id.imageView);
        latLong = findViewById(R.id.latLongView);
        feelingView = findViewById(R.id.feeling);
        feelingLabel = findViewById(R.id.feelingView);
        recordPlayer = findViewById(R.id.musicPlayer);
        recordPlayer.setText(getResources().getString(R.string.record_player));
        locationLabel = findViewById(R.id.locationLabel);
        removeLocation = findViewById(R.id.removeLocation);
        removeFeeling = findViewById(R.id.removeFeeling);
        recordingBtn = findViewById(R.id.recordingBtn);
        removeRecord = findViewById(R.id.removeRecord);
        colorView = findViewById(R.id.colorView);
        videoView = findViewById(R.id.videoView);
        removeRecord.setVisibility(View.INVISIBLE);
        recordingBtn.setVisibility(View.INVISIBLE);
        recordPlayer.setVisibility(View.INVISIBLE);
        locationLabel.setVisibility(View.INVISIBLE);
        feelingLabel.setVisibility(View.INVISIBLE);
        removeLocation.setVisibility(View.INVISIBLE);
        removeFeeling.setVisibility(View.INVISIBLE);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_note));


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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNote.this)
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

        recordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = !recording;
                if (recording) {
                    Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
                    recordingBtn.setImageResource(R.drawable.ic_stop_black_24dp);
                    record = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Recording stopped. Please click on bottom toolbar to play record.", Toast.LENGTH_LONG).show();
                    recordingBtn.setVisibility(View.INVISIBLE);
                    mediaRecorder.stop();
                    videoView.setVideoPath(record);
                    MediaController mediaController = new MediaController(AddNote.this);
                    videoView.setMediaController(mediaController);
                    removeRecord.setVisibility(View.VISIBLE);
                    recordPlayer.setVisibility(View.VISIBLE);

                }
            }
        });


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNote.this)
                        .setTitle("Remove picture")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedPhoto = "";
                                imageView.setImageURI(null);
                            }
                        });
                builder.show();
                return true;
            }
        });

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
        inflater.inflate(R.menu.add_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (noteTitle.getText().toString().isEmpty() && noteDetails.getText().toString().isEmpty() && selectedPhoto.isEmpty()) {
                    builder = new AlertDialog.Builder(AddNote.this);
                    builder.setTitle("Error!");
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setMessage("You cant save empty note.");
                    builder.setNegativeButton("Okay", null);
                    dialog = builder.create();
                    dialog.show();
                } else {
                    creation = String.valueOf(System.currentTimeMillis());
                    Note note = new Note(
                            noteTitle.getText().toString(),
                            noteDetails.getText().toString(),
                            latitude, longitude, selectedPhoto,
                            feeling, creation, record, selectedColor);
                    NoteDatabase db = new NoteDatabase(this);
                    db.addNote(note, AddNote.this);
                    goToMain();
                }
                break;
            case R.id.delete:
                noteDetails.setText("");
                noteTitle.setText("");
                latLong.setText("");
                selectedPhoto = "";
                feeling = "";
                feelingView.setText("");
                latitude = null;
                longitude = null;
                imageView.setImageURI(null);
                getSupportActionBar().setTitle("");
                removeFeeling.setVisibility(View.INVISIBLE);
                removeLocation.setVisibility(View.INVISIBLE);
                locationLabel.setVisibility(View.INVISIBLE);
                feelingLabel.setVisibility(View.INVISIBLE);
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
                    recordingBtn.setVisibility(View.VISIBLE);
                    videoView.setBackgroundResource(R.color.colorPrimary);
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

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(record);
    }

    private void colorDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(AddNote.this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
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

    private boolean locationPermission() {
        int fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocation == PackageManager.PERMISSION_GRANTED && coarseLocation == PackageManager.PERMISSION_GRANTED;
    }

    private void openDialog() {
        FeelingDialog dialog = new FeelingDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Feeling dialog");
    }


    private void mapDialog() {
        MapDialog dialog = new MapDialog(AddNote.this, latitude, longitude);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Map dialog");
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_PERMISSION_CODE);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            selectedPhoto = String.valueOf(data.getData());
            imageView.setImageURI(Uri.parse(selectedPhoto));
        }
    }

    @Override
    public void applyFeeling(String rating) {
        if (!rating.isEmpty()) {
            feelingLabel.setVisibility(View.VISIBLE);
            feelingView.setText(rating);
            feeling = rating;
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

