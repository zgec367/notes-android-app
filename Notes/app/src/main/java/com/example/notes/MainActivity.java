package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static Resources resources;
    RecyclerView recycleView;
    NoteDatabase noteDatabase;
    Adapter adapter;
    TextView noData;
    List<Note> allNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        recycleView = findViewById(R.id.listNotes);
        noteDatabase = new NoteDatabase(this);
        allNotes = noteDatabase.getAllNotes();
        displayList(allNotes);
        noData = findViewById(R.id.noDataView);
        resources = getResources();
    }

    private void displayList(List<Note> allNotes) {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, allNotes);
        recycleView.setAdapter(adapter);
    }

    private void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putString("language", localeCode);
            editor.apply();

        } else {
            configuration.locale = new Locale(localeCode.toLowerCase());
            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putString("language", localeCode);
            editor.apply();

        }
        resources.updateConfiguration(configuration, displayMetrics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Note> getAllNotes = noteDatabase.getAllNotes();
        if (getAllNotes.isEmpty()) {
            this.noData.setText(resources.getString(R.string.no_data));
        } else {
            displayList(getAllNotes);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(this, AddNote.class);
                startActivity(i);
                break;
            case R.id.de:
                setAppLocale("de");
                recreate();
                return true;
            case R.id.en:
                setAppLocale("en");
                recreate();
                return true;
            case R.id.it:
                setAppLocale("it");
                recreate();
                return true;
            case R.id.hr:
                setAppLocale("hr");
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
