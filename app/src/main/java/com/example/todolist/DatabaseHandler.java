package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    //  Constructor
    public DatabaseHandler(Context context){
        super(context, "ToDoDB",null,1);
    }

    //  Oncreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todo (id INTEGER PRIMARY KEY, title VARCHAR(100), content VARCHAR(10000),status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS todo");
        onCreate(db);
    }

//  Delete Note
    void deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        String id_string = "id = '" + id + "'";

        int deleted = db.delete("todo", id_string, null);

        db.close();

    }


//  Delete Note
    void deleteAllNote(){
        SQLiteDatabase db = getWritableDatabase();

        int deleted = db.delete("todo", null, null);

        db.close();
    }

//  Add Note
    void addNote(String title, String content, int status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("content",content);
        contentValues.put("status",status);

        long added = db.insert("todo",null,contentValues);
        db.close();
    }

//  update Note
    void updateNote(int id, String title, String content){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id_string = "id = '" + id + "'";
        contentValues.put("title",title);
        contentValues.put("content",content);

        int update = db.update("todo",contentValues,id_string,null);
    }

//  update Status
    void updateTask(int id, int status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id_string = "id = '" + id + "'";
        contentValues.put("status",status);

        int update = db.update("todo",contentValues,id_string,null);
    }

//  Showing the List
    ArrayList<ExampleItem> getAllNotes(){
        ArrayList<ExampleItem> itemArray = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM todo", null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                int status = cursor.getInt(3);
                ExampleItem exampleItem = new ExampleItem(id,title,content,status);
                itemArray.add(exampleItem);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return itemArray;
    }
}
