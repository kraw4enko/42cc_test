package com.krawa.test42cc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DB{

	  private static final String DB_NAME = "myDB";
	  private static final int DB_VERSION = 1;
	  private static final String DB_TABLE = "myinfo";
	  
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_SURNAME = "surname";
	  public static final String COLUMN_BIRTH = "birth";
	  public static final String COLUMN_BIO = "bio";
	  public static final String COLUMN_CONTACTS = "contacts";
	  public static final String COLUMN_FOTO = "foto";
	  
	  private static final String DB_CREATE = 
			    "create table " + DB_TABLE + "(" +
			      COLUMN_ID + " integer primary key autoincrement, " +
			      COLUMN_NAME + " text, " +
			      COLUMN_SURNAME + " text, " +
			      COLUMN_BIRTH + " date, " +
			      COLUMN_BIO + " text, " +
			      COLUMN_CONTACTS + " text, " +
			      COLUMN_FOTO + " text " +
			    ");";
	  
	  private final Context mCtx;	  	  
	  private DBHelper mDBHelper;
	  private SQLiteDatabase mDB;
	  
	  public DB(Context ctx) {
	    mCtx = ctx;
	  }
	    
	  
	  // открыть подключение
	  public void open() {
		  mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
		  mDB = mDBHelper.getWritableDatabase();
	  }
	    
	  // закрыть подключение
	  public void close() {
	    if (mDBHelper!=null) mDBHelper.close();
	  }
	  
	  // получить все данные из таблицы DB_TABLE
	  public Cursor getAllData() {
	    return mDB.query(DB_TABLE, null, null, null, null, null, null);
	  }
	  
	  // добавить запись в DB_TABLE
	  public void addRec(String name, String surname, String birth, String bio, String contacts, String foto) {
	    ContentValues cv = new ContentValues();
	    cv.put(COLUMN_NAME, name);
	    cv.put(COLUMN_SURNAME, surname);
	    cv.put(COLUMN_BIRTH, birth);
	    cv.put(COLUMN_BIO, bio);
	    cv.put(COLUMN_CONTACTS, contacts);
	    cv.put(COLUMN_FOTO, foto);
	    mDB.insert(DB_TABLE, null, cv);
	  }
	  
	  // класс по созданию и управлению БД
	  private class DBHelper extends SQLiteOpenHelper {

		  public DBHelper(Context context, String name, CursorFactory factory,int version) {
			  super(context, name, factory, version);
		  }

		  //создаем и заполняем БД
		  @Override
		  public void onCreate(SQLiteDatabase db) {
			  db.execSQL(DB_CREATE);
		  }

		  @Override
		  public void onUpgrade(SQLiteDatabase db, int oldVersion,
				  int newVersion) {				
		  }
	  }
}
