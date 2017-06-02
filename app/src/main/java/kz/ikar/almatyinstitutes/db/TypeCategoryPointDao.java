package kz.ikar.almatyinstitutes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kz.ikar.almatyinstitutes.classes.Category;
import kz.ikar.almatyinstitutes.classes.Point;
import kz.ikar.almatyinstitutes.classes.Type;

/**
 * Created by VH on 26.05.2017.
 */

public class TypeCategoryPointDao {
    private Context context;
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private static String[] typeColums = {
            DBHelper.COLUMN_TYPE_ID, DBHelper.COLUMN_TYPE_NAME
    };

    private static String[] categoryColumns = {
            DBHelper.COLUMN_CATEGORY_ID, DBHelper.COLUMN_CATEGORY_NAME
    };
    private static String[] pointsColumns = {
            DBHelper.COLUMN_POINT_ID, DBHelper.COLUMN_POINT_LATITUDE, DBHelper.COLUMN_POINT_LONGITUDE
    };

    public TypeCategoryPointDao(Context context) {
        sqLiteOpenHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public Category getCategoryById(int id) {
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_CATEGORY, categoryColumns,
                DBHelper.COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        Category category = new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return category;
    }

    public Type getTypeById(int id) {
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_TYPE, typeColums,
                DBHelper.COLUMN_TYPE_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Type type = new Type(cursor.getInt(0), cursor.getString(1));
        return type;
    }

    public Point addPoint(Point point) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_POINT_LATITUDE, point.getLatitude());
        cv.put(DBHelper.COLUMN_POINT_LONGITUDE, point.getLongitude());
        int insertId = (int) sqLiteDatabase.insert(DBHelper.TABLE_POINT, null, cv);
        point.setId(insertId);
        return point;
    }

    public Point getPointById(int id){
        Cursor cursor=sqLiteDatabase.query(DBHelper.TABLE_POINT,pointsColumns,
                DBHelper.COLUMN_POINT_ID+" = ?",new String[]{String.valueOf(id)},
                null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Point point=new Point(cursor.getInt(0),cursor.getDouble(1),cursor.getDouble(2));
        return point;
    }

}
