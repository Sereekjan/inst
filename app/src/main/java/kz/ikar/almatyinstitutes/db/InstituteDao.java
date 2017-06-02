package kz.ikar.almatyinstitutes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import kz.ikar.almatyinstitutes.classes.Category;
import kz.ikar.almatyinstitutes.classes.Institute;
import kz.ikar.almatyinstitutes.classes.Point;
import kz.ikar.almatyinstitutes.classes.Type;

/**
 * Created by VH on 26.05.2017.
 */

public class InstituteDao {
    private Context context;
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private static String[] allColumns = {
            DBHelper.COLUMN_INSTITUTE_ID, DBHelper.COLUMN_INSTITUTE_NAME,
            DBHelper.COLUMN_INSTITUTE_ADDRESS, DBHelper.COLUMN_INSTITUTE_PHONE,
            DBHelper.COLUMN_INSTITUTE_POINT, DBHelper.COLUMN_INSTITUTE_HEAD,
            DBHelper.COLUMN_INSTITUTE_TYPE, DBHelper.COLUMN_INSTITUTE_CATEGORY,
            DBHelper.COLUMN_INSTITUTE_ISGOV
    };

    public InstituteDao() {

    }

    public InstituteDao(Context context) {
        sqLiteOpenHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public Institute addInstitute(Institute institute) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_INSTITUTE_NAME, institute.getName());
        cv.put(DBHelper.COLUMN_INSTITUTE_ADDRESS, institute.getAddress());
        cv.put(DBHelper.COLUMN_INSTITUTE_PHONE, institute.getPhone());
        cv.put(DBHelper.COLUMN_INSTITUTE_POINT, institute.getPoint().getId());
        cv.put(DBHelper.COLUMN_INSTITUTE_HEAD, institute.getHead());
        cv.put(DBHelper.COLUMN_INSTITUTE_TYPE, institute.getType().getId());
        cv.put(DBHelper.COLUMN_INSTITUTE_ISGOV, institute.isGov());
        int insetId = (int) sqLiteDatabase.insert(DBHelper.TABLE_INSTITUTE, null, cv);
        return institute;
    }


    public Category getCategory(int id) {
        TypeCategoryPointDao tcp = new TypeCategoryPointDao(context);
        tcp.open();
        Category category = tcp.getCategoryById(id);
        tcp.close();
        return category;
    }

    public Type getType(int id) {
        TypeCategoryPointDao tcp = new TypeCategoryPointDao(context);
        tcp.open();
        Type type = tcp.getTypeById(id);
        tcp.close();
        return type;
    }

    public Boolean isGov(int i) {
        if (i == 0) {
            return false;
        }
        return true;
    }

    public Point getPoint(int id) {
        TypeCategoryPointDao tcp = new TypeCategoryPointDao(context);
        tcp.open();
        Point p = tcp.getPointById(id);
        tcp.close();
        return p;
    }

    public List<Institute> getAllInstitutes() {
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INSTITUTE, allColumns, null, null, null, null, null);
        List<Institute> instituteList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Institute institute = new Institute(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_PHONE)),
                        getPoint(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_POINT))),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_HEAD)),
                        getType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_TYPE))),
                        null,
                        isGov(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ISGOV))));
                instituteList.add(institute);
            }
        }
        return instituteList;
    }

    public List<Institute> getByType(int id) {
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INSTITUTE, allColumns,
                DBHelper.COLUMN_INSTITUTE_TYPE + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        List<Institute> instituteList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Institute institute = new Institute(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_PHONE)),
                        getPoint(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_POINT))),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_HEAD)),
                        getType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_TYPE))),
                        null,
                        isGov(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ISGOV))));
                instituteList.add(institute);
            }
        }
        return instituteList;
    }
    public List<Institute> getByGov(boolean bool) {
        int i=0;
        if (bool){
            i=1;
        }
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INSTITUTE, allColumns,
                DBHelper.COLUMN_INSTITUTE_ISGOV + "=?", new String[]{String.valueOf(i)}, null, null, null);
        List<Institute> instituteList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Institute institute = new Institute(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_PHONE)),
                        getPoint(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_POINT))),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_HEAD)),
                        getType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_TYPE))),
                        null,
                        isGov(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ISGOV))));
                instituteList.add(institute);
            }
        }
        return instituteList;
    }

    public Institute getByName(String name) {
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INSTITUTE, allColumns,
                DBHelper.COLUMN_INSTITUTE_NAME + " like ?", new String[]{"'" + name + "'"}, null, null, null);
        Institute institute = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            institute = new Institute(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_PHONE)),
                    getPoint(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_POINT))),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_HEAD)),
                    getType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_TYPE))),
                    null,
                    isGov(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ISGOV))));
        }
        return institute;
    }
}
