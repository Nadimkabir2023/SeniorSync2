package com.example.seniorsync2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

/**
 * This class extends SQLiteOpenHelper to manage database creation and version management.
 */
public class MedReminderDB extends SQLiteOpenHelper {
    private static MedReminderDB instance;

    /**
     * Returns a singleton instance of the database helper object.
     */
    public static synchronized MedReminderDB getInstance(Context context) {
        if (instance == null) {
            instance = new MedReminderDB(context.getApplicationContext());
        }
        return instance;
    }

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor for creating a database helper object.
     */
    public MedReminderDB(Context context) {
        super(context, "health_database", null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS MEDICINE (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MED_NAME TEXT NOT NULL, " +
                "QTY INTEGER NOT NULL, " +
                "DATE_TIME TEXT NOT NULL, " +
                "DAYS TEXT NOT NULL, " +
                "ENABLE INTEGER NOT NULL);";
        db.execSQL(sqlCreateTable);
    }

    /**
     * Called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data message here when upgrading
    }

    /**
     * Adds a new medicine record to the database.
     */
    public void insertMedicine(SQLiteDatabase db, @NonNull String medicineName, @NonNull int quantity, @NonNull String dateTime, @NonNull String days) {
        ContentValues values = new ContentValues();
        values.put("MED_NAME", medicineName);
        values.put("QTY", quantity);
        values.put("DATE_TIME", dateTime);
        values.put("DAYS", days);
        values.put("ENABLE", 0);

        db.insert("MEDICINE", null, values);
    }

    /**
     * Deletes a medicine record from the database.
     */
    public void removeMedicine(SQLiteDatabase db, int medicineId) {
        db.delete("MEDICINE", "_id=?", new String[]{String.valueOf(medicineId)});
    }

    /**
     * Updates the enabled state of a specific medicine record.
     */
    public void updateMedicineEnabled(SQLiteDatabase db, int medicineId, int x) {
        ContentValues values = new ContentValues();
        values.put("ENABLE", x);
        db.update("MEDICINE", values, "_id=?", new String[]{"" + medicineId});
    }

    /**
     * Retrieves all medicine records from the database.
     */
    public Cursor fetchAllMedicines(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM MEDICINE", null);
    }

    /**
     * Retrieves a specific medicine record by its ID.
     */
    public Cursor fetchMedicineById(SQLiteDatabase db, int medicineId) {
        return db.rawQuery("SELECT * FROM MEDICINE WHERE _id = ?", new String[]{String.valueOf(medicineId)});
    }
}