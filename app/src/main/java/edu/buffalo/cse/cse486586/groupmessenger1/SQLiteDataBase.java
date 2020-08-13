package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class SQLiteDataBase extends SQLiteOpenHelper {

    private static final String TABLE_Name = "groupmessenger";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String KEY_FIELD = "'key'";
    private static final String VALUE_FIELD = "value";
    ContentValues cValues;
    public SQLiteDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = "CREATE TABLE  groupmessenger ("+ VALUE_FIELD +" TEXT ,"+  KEY_FIELD +" TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void insertGroupMessages(ContentValues cv){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
       /* ContentValues cValues = new ContentValues();
        cValues.put(key, key);
        cValues.put(value, value);
       */ // Insert the new row, returning the primary key value of the new row
        System.out.println(cv.get("key"));

        long newRowId = db.insert(TABLE_Name,null, cv);
        db.close();
    }
    public Cursor getValues(String[] projection, String selection, String[] selectionArgs,
                            String sortOrder){
        SQLiteDatabase db = this.getWritableDatabase();
       // ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT  VALUE FROM groupmessenger";
        Cursor cursor = db.rawQuery(query,null);

       /* while (cursor.moveToNext()){
           // HashMap<String,String> user = new HashMap<>();
             cValues = new ContentValues();
           // cValues.put("KEY1", cursor.getString(cursor.getColumnIndex("KEY1")));
            cValues.put("VALUE", cursor.getString(cursor.getColumnIndex("VALUE")));
           // userList.add(user);
        }
        return cValues;
*/
       return cursor;
    }
}
