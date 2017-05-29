package kz.ikar.almatyinstitues.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import kz.ikar.almatyinstitues.classes.Category;
import kz.ikar.almatyinstitues.classes.Institute;
import kz.ikar.almatyinstitues.classes.Point;
import kz.ikar.almatyinstitues.classes.Type;

/**
 * Created by VH on 26.05.2017.
 */

public class InstituteDao {
    private Context context;
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private static String[] allColumns={
            DBHelper.COLUMN_INSTITUTE_ID,DBHelper.COLUMN_INSTITUTE_NAME,
            DBHelper.COLUMN_INSTITUTE_ADDRESS,DBHelper.COLUMN_INSTITUTE_PHONE,
            DBHelper.COLUMN_INSTITUTE_POINT,DBHelper.COLUMN_INSTITUTE_HEAD,
            DBHelper.COLUMN_INSTITUTE_TYPE,DBHelper.COLUMN_INSTITUTE_CATEGORY,
            DBHelper.COLUMN_INSTITUTE_ISGOV
    };

    public InstituteDao(){

    }
    public InstituteDao(Context context){
        sqLiteOpenHelper=new DBHelper(context);
        this.context=context;
    }

    public void open(){
        sqLiteDatabase=sqLiteOpenHelper.getWritableDatabase();
    }
    public void close(){
        sqLiteDatabase.close();
    }

    public Institute addInstitute(Institute institute){
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_INSTITUTE_NAME,institute.getName());
        cv.put(DBHelper.COLUMN_INSTITUTE_ADDRESS,institute.getAddress());
        cv.put(DBHelper.COLUMN_INSTITUTE_PHONE,institute.getPhone());
        //cv.put(DBHelper.COLUMN_INSTITUTE_POINT,institute.getPoint().getId());
        //cv.put(DBHelper.co);
        cv.put(DBHelper.COLUMN_INSTITUTE_HEAD,institute.getHead());
        cv.put(DBHelper.COLUMN_INSTITUTE_TYPE,institute.getType().getId());
        cv.put(DBHelper.COLUMN_INSTITUTE_ISGOV,institute.isGov());
        long insetId=sqLiteDatabase.insert(DBHelper.TABLE_INSTITUTE,null,cv);
        return  institute;
    }

    public Point getPoint(){
        return null;
    }
    public Category getCategory(int id){
        TypeCategoryPointDao tcp=new TypeCategoryPointDao(context);
        tcp.open();
        Category category=tcp.getCategoryById(id);
        return category;
    }
    public Type getType(){
        return null;
    }
    public Boolean isGov(int i){
        if (i==0){
            return false;
        }
        return true;
    }


    public List<Institute> getAllInstitutes(){
        Cursor cursor=sqLiteDatabase.query(DBHelper.TABLE_INSTITUTE,allColumns,null,null,null,null,null);
        List<Institute> instituteList=new ArrayList<>();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Institute institute=new Institute(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_PHONE)),
                        getPoint(),
                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_HEAD)),
                        getType(),
                        getCategory(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_CATEGORY))),
                        isGov(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INSTITUTE_ISGOV))));

                instituteList.add(institute);
            }
        }
        return  instituteList;
    }




}
