package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notes_db";
    private static final String TABLE_NAME = "Notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_FEELING = "feeling";
    private static final String KEY_CREATION = "creation";
    private static final String KEY_LAST_MODIFICATION = "last_modification";
    private static final String KEY_RECORD = "record";
    private static final String KEY_COLOR = "color";


    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_LATITUDE + " DOUBLE," +
                KEY_LONGITUDE + " DOUBLE," +
                KEY_PHOTO + " TEXT," +
                KEY_FEELING + " TEXT," +
                KEY_CREATION + " TEXT, " +
                KEY_LAST_MODIFICATION + " TEXT, " +
                KEY_RECORD + " TEXT, " +
                KEY_COLOR + " INTEGER "
                + " )";
        db.execSQL(createDb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Note note, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_TITLE, note.getTitle());
        value.put(KEY_CONTENT, note.getContent());
        value.put(KEY_LATITUDE, note.getLatitude());
        value.put(KEY_LONGITUDE, note.getLongitude());
        value.put(KEY_PHOTO, note.getPhoto());
        value.put(KEY_FEELING, note.getFeeling());
        value.put(KEY_CREATION, note.getCreation());
        value.put(KEY_RECORD, note.getRecord());
        if (note.getColor() != 0) {
            value.put(KEY_COLOR, note.getColor());
        }
        long ID = db.insert(TABLE_NAME, null, value);
        Toast.makeText(context, MainActivity.resources.getText(R.string.added_note), Toast.LENGTH_SHORT).show();
        return ID;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_PHOTO, KEY_LATITUDE, KEY_LONGITUDE, KEY_FEELING, KEY_CREATION, KEY_LAST_MODIFICATION, KEY_RECORD, KEY_COLOR};
        Cursor cursor = db.query(TABLE_NAME, query, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Double latitude = null;
        Double longitude = null;
        if (!cursor.isNull(4) || !cursor.isNull(5)) {
            latitude = cursor.getDouble(4);
            longitude = cursor.getDouble(5);
        }
        return new Note(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                latitude,
                longitude,
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getInt(10));
    }

    public List<Note> getAllNotes() {
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setPhoto(cursor.getString(5));
                note.setCreation(cursor.getString(7));
                note.setLast_modification(cursor.getString(8));
                note.setColor(cursor.getInt(10));
                allNotes.add(note);
            } while (cursor.moveToNext());
        }
        return allNotes;
    }

    public int editNote(Note note, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_TITLE, note.getTitle());
        value.put(KEY_CONTENT, note.getContent());
        value.put(KEY_PHOTO, note.getPhoto());
        value.put(KEY_LATITUDE, note.getLatitude());
        value.put(KEY_LONGITUDE, note.getLongitude());
        value.put(KEY_FEELING, note.getFeeling());
        value.put(KEY_LAST_MODIFICATION, note.getLast_modification());
        value.put(KEY_CREATION, note.getCreation());
        value.put(KEY_RECORD, note.getRecord());
        value.put(KEY_COLOR, note.getColor());
        Toast.makeText(context, MainActivity.resources.getText(R.string.edited_note), Toast.LENGTH_SHORT).show();
        return db.update(TABLE_NAME, value, KEY_ID + "=?", new String[]{String.valueOf(note.getID())});

    }

    void deleteNote(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        Toast.makeText(context, MainActivity.resources.getText(R.string.deleted_note), Toast.LENGTH_SHORT).show();
    }
}
