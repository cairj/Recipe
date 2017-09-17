package com.recipe.r.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recipe.r.entity.SearchHistory;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 */

public class SearchDbUtil {
    private Context context;

    public SearchDbUtil(Context context) {
        this.context = context;
    }

    //插入数据
    public void insertData(String username, String input) {
        SearchDbHelper searchDbHelper = new SearchDbHelper(context, SearchDbHelper.TABLE, null, 1);
        SQLiteDatabase database = searchDbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + SearchDbHelper.TABLE_NAME
                + " where " + SearchDbHelper.USER_NAME + "=?"
                + " and " + SearchDbHelper.INPUT_NAME + "=?", new String[]{username, input});
        if (cursor.getCount() <= 0) {//不存在输入的数据时
            ContentValues values = new ContentValues();
            values.put(SearchDbHelper.USER_NAME, username);
            values.put(SearchDbHelper.INPUT_NAME, input);
            database.insert(SearchDbHelper.TABLE_NAME, null, values);
            cursor.close();
            database.close();
        } else {//已经存在
            database.delete(SearchDbHelper.TABLE_NAME, SearchDbHelper.USER_NAME + "=?"
                    + " and " + SearchDbHelper.INPUT_NAME + "=?", new String[]{username, input});
            ContentValues values = new ContentValues();
            values.put(SearchDbHelper.USER_NAME, username);
            values.put(SearchDbHelper.INPUT_NAME, input);
            database.insert(SearchDbHelper.TABLE_NAME, null, values);
            cursor.close();
            database.close();
        }
    }

    //删除单条数据
    public void deleteData(String input) {

    }

    //删除所有数据
    public int deletaAll(String username) {
        SearchDbHelper searchDbHelper = new SearchDbHelper(context, SearchDbHelper.TABLE, null, 1);
        SQLiteDatabase database = searchDbHelper.getWritableDatabase();
        int result = database.delete(SearchDbHelper.TABLE_NAME, SearchDbHelper.USER_NAME + "=?", new String[]{username});
        database.close();
        return result;
    }

    //查询数据
    public ArrayList<SearchHistory> queryData(String username) {
        SearchDbHelper searchDbHelper = new SearchDbHelper(context, SearchDbHelper.TABLE, null, 1);
        SQLiteDatabase database = searchDbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + SearchDbHelper.TABLE_NAME
                + " where " + SearchDbHelper.USER_NAME + "=?"
                + "order by id desc", new String[]{username});
        ArrayList<SearchHistory> list = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String input = cursor.getString(cursor.getColumnIndex(SearchDbHelper.INPUT_NAME));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user = cursor.getString(cursor.getColumnIndex(SearchDbHelper.USER_NAME));
            SearchHistory searchHistory = new SearchHistory(id, user, input);
            list.add(searchHistory);
        }
        cursor.close();
        database.close();
        return list;
    }
}
