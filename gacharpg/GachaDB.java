package com.zybooks.gacharpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GachaDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "roster.db";
    private static final int VERSION = 1;
    private static GachaDB sInstance;
    private static Context ctx;

    public static synchronized GachaDB getInstance(Context context) {
        if(sInstance == null)
            sInstance = new GachaDB(context.getApplicationContext());
        return sInstance;
    }

    protected GachaDB(Context context){
        super(context, DATABASE_NAME, null, VERSION);
        ctx = context;
    }

    private static final class RosterTable implements BaseColumns {
        private static final String TABLE = "roster";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "name";
        private static final String COL_ELEMENT = "element";
        private static final String COL_ROLE = "role";
        private static final String COL_LEVEL = "level";
        private static final String COL_UNLOCKED = "unlocked";
        private static final String COL_PARTY1 = "party1";
        private static final String COL_PARTY2 = "party2";
        private static final String COL_PARTY3 = "party3";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RosterTable.TABLE + " (" +
                RosterTable.COL_ID + " integer primary key autoincrement, " +
                RosterTable.COL_NAME + " text unique, " +
                RosterTable.COL_ELEMENT + " text, " +
                RosterTable.COL_ROLE + " text, " +
                RosterTable.COL_LEVEL + " text, " +
                RosterTable.COL_UNLOCKED + " text, " +
                RosterTable.COL_PARTY1 + " text, " +
                RosterTable.COL_PARTY2 + " text, " +
                RosterTable.COL_PARTY3 + " text)");

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(ctx.getAssets().open("default_roster.csv")));
            String reader = "";

            while ((reader = in.readLine()) != null) {
                String[] data = reader.split(",");
                String name = data[0];
                String element = data[1];
                String role = data[2];
                String level = data[3];
                String unlocked = data[4];

                ContentValues values = new ContentValues();
                values.put(RosterTable.COL_NAME, name);
                values.put(RosterTable.COL_ELEMENT, element);
                values.put(RosterTable.COL_ROLE, role);
                values.put(RosterTable.COL_LEVEL, level);
                values.put(RosterTable.COL_UNLOCKED, unlocked);

                db.insert(RosterTable.TABLE, null, values);
            }
            in.close();
        } catch(Exception e) {
            //drop it like it's hot
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + RosterTable.TABLE);
        onCreate(db);
    }

    public void resetDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("drop table if exists " + RosterTable.TABLE);
        onCreate(db);
    }

    public String[] getAll() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> sb = new ArrayList<>();

        String projection[] = { RosterTable.COL_NAME };
        Cursor cursor = db.query(RosterTable.TABLE, projection, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                sb.add(name);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return sb.toArray(new String[sb.size()]);
    }

    public String[] getNames() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> sb = new ArrayList<>();

        String projection[] = { RosterTable.COL_NAME };
        String selection = RosterTable.COL_UNLOCKED + "=?";
        String[] selectionArgs = { "y" };
        Cursor cursor = db.query(RosterTable.TABLE, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                sb.add(name);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return sb.toArray(new String[sb.size()]);
    }

    public String[] getStats(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String[] sb = new String[4];

        String projection[] = {
                RosterTable.COL_ID,
                RosterTable.COL_ELEMENT,
                RosterTable.COL_ROLE,
                RosterTable.COL_LEVEL
        };

        String selection = RosterTable.COL_NAME + " =?";
        String[] selectionArgs = { name };

        Cursor cursor = db.query(RosterTable.TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if(cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getLong(0));
                String element = cursor.getString(1);
                String role = cursor.getString(2);
                String level = cursor.getString(3);

                sb[0] = id;
                sb[1] = element;
                sb[2] = role;
                sb[3] = level;
            }while (cursor.moveToNext());
        }
        cursor.close();

        return sb;
    }

    public void unlockCharacter(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RosterTable.COL_UNLOCKED, "y");

        db.update(RosterTable.TABLE, values, RosterTable.COL_NAME + " =?", new String[] { name });
    }
}
