package kz.ikar.almatyinstitutes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VH on 26.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "OpenStrMapDB.db";


    public static final String TABLE_CATEGORY = "categories";
    public static final String COLUMN_CATEGORY_ID = "_id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    public static final String TABLE_POINT = "points";
    public static final String COLUMN_POINT_ID = "_id";
    public static final String COLUMN_POINT_LATITUDE = "latitude";
    public static final String COLUMN_POINT_LONGITUDE = "longitude";

    public static final String TABLE_TYPE = "types";
    public static final String COLUMN_TYPE_ID = "_id";
    public static final String COLUMN_TYPE_NAME = "name";


    public static final String TABLE_INSTITUTE = "institutes";
    public static final String COLUMN_INSTITUTE_ID = "_id";
    public static final String COLUMN_INSTITUTE_NAME = "name";
    public static final String COLUMN_INSTITUTE_ADDRESS = "address";
    public static final String COLUMN_INSTITUTE_PHONE = "phone";
    public static final String COLUMN_INSTITUTE_ISGOV = "is_gov";
    public static final String COLUMN_INSTITUTE_HEAD = "head";
    public static final String COLUMN_INSTITUTE_POINT = "point_id";
    public static final String COLUMN_INSTITUTE_TYPE = "type_id";
    public static final String COLUMN_INSTITUTE_CATEGORY = "category_id";


    public static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORY
            + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME + " TEXT " +
            ")";
    public static final String CREATE_POINTS_TABLE = "CREATE TABLE " + TABLE_POINT
            + "("
            + COLUMN_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_POINT_LATITUDE + " DOUBLE, "
            + COLUMN_POINT_LONGITUDE + " DOUBLE "
            + ")";
    public static final String CREATE_TYPES_TABLE="CREATE TABLE "+TABLE_TYPE
            +"("
            +COLUMN_TYPE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_TYPE_NAME+" TEXT "
            +")";
    public static final String CREATE_INSTITUTE_TABLE="CREATE TABLE "+TABLE_INSTITUTE
            +"("
            +COLUMN_INSTITUTE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_INSTITUTE_NAME+" TEXT, "
            +COLUMN_INSTITUTE_ADDRESS+" TEXT, "
            +COLUMN_INSTITUTE_PHONE+" TEXT, "
            +COLUMN_INSTITUTE_HEAD+" TEXT, "
            +COLUMN_INSTITUTE_ISGOV+" INTEGER, "
            +COLUMN_INSTITUTE_POINT+" INTEGER REFERENCES "+TABLE_POINT+"("+COLUMN_POINT_ID+"), "
            +COLUMN_INSTITUTE_TYPE+" INTEGER REFERENCES "+TABLE_TYPE+"("+COLUMN_TYPE_ID+"), "
            +COLUMN_INSTITUTE_CATEGORY+" INTEGER REFERENCES "+TABLE_CATEGORY+"("+COLUMN_CATEGORY_ID+")"

            /*+COLUMN_INSTITUTE_POINT+" INTEGER, FOREIGN KEY ("+COLUMN_INSTITUTE_POINT+") REFERENCES "+TABLE_POINT+"("+COLUMN_POINT_ID+"), "
            +COLUMN_INSTITUTE_TYPE+" INTEGER, FOREIGN KEY ("+ COLUMN_INSTITUTE_TYPE+") REFERENCES "+TABLE_TYPE+"("+COLUMN_TYPE_ID+"), "
            +COLUMN_INSTITUTE_CATEGORY+" INTEGER, FOREIGN KEY ("+ COLUMN_INSTITUTE_CATEGORY+") REFERENCES "+TABLE_CATEGORY+"("+COLUMN_CATEGORY_ID+")"*/
            +")";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_POINTS_TABLE);
        db.execSQL(CREATE_TYPES_TABLE);
        db.execSQL(CREATE_INSTITUTE_TABLE);
        this.addOnCreate(db,"kindergarten");
        this.addOnCreate(db,"school");
        this.addOnCreate(db,"college");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_POINT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_INSTITUTE);
        onCreate(db);
    }

    public void addOnCreate(SQLiteDatabase db,String value){
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TYPE_NAME,value);
        db.insert(TABLE_TYPE,null,cv);
    }

    public static boolean isExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }
}
