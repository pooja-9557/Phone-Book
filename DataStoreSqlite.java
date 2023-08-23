package com.tuition.createcontactform;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataStoreSqlite extends SQLiteOpenHelper {

    private final static String dataBaseName = "UpdatedContactData";
    private final static String tableName = "UpdatedContact";
    private final static String firstName = "fNameText";
    private final static String lastName = "lNameText";
    private final static String companyName = "cNAmeText";
    private final static String phoneNumber = "pNumberText";
    private final static String emailId = "eIdText";
    private final static String calender = "cIdText";
    private final static String image = "imageURL";

    public DataStoreSqlite(@Nullable Context context) {
        super(context, dataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + firstName + " Text NOT NULL," + lastName + " Text ," + companyName + " Text ," + phoneNumber + " Text Primary key," + emailId + " Text ," + calender + " Text ," + image + " Text)";
        Log.d("", "" + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE " + tableName;
        db.execSQL(query);
    }

    public void addData(String firstName, String lastName, String companyName, String phoneNumber, String emailId, String calender, String imageValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fNameText", firstName);
        values.put("lNameText", lastName);
        values.put("cNameText", companyName);
        values.put("pNumberText", phoneNumber);
        values.put("eIdText", emailId);
        values.put("cIdText", calender);
        values.put("imageURL", imageValue);

        db.insert(tableName, null, values);
    }

    public ArrayList<DataModelClass> FetchData() {
        SQLiteDatabase db = this.getReadableDatabase();
        AbstractWindowedCursor cursor = getSqlCursorWithIncreasedWindowSize(db.rawQuery("SELECT * FROM " + tableName, null));

        ArrayList<DataModelClass> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new DataModelClass(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    AbstractWindowedCursor getSqlCursorWithIncreasedWindowSize(Cursor cursor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CursorWindow cw = new CursorWindow(null, 10000000L);
            ((AbstractWindowedCursor) cursor).setWindow(cw);
        }
        return (AbstractWindowedCursor) cursor;
    }

    public ArrayList<DataModelClass> FetchUniqueData(String uniquePhoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = String.format("SELECT * FROM %s where pNumberText = ?", tableName);
        AbstractWindowedCursor cursor = getSqlCursorWithIncreasedWindowSize( db.rawQuery(queryString, new String[]{uniquePhoneNumber}));
        ArrayList<DataModelClass> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new DataModelClass(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void DataUpdate(String name, String lName, String cName, String phoneNumberData, String mail, String cal, String imageValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(firstName, name);
        values.put(lastName, lName);
        values.put(companyName, cName);
        values.put(phoneNumber, phoneNumberData);
        values.put(emailId, mail);
        values.put(calender, cal);
        values.put(image, imageValue);
        db.update(tableName, values, "pNumberText=?", new String[]{phoneNumberData});
        db.close();
    }

    public void DeleteData(String phoneNumberDelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, "pNumberText=?", new String[]{phoneNumberDelete});
        db.close();
    }

}
