package com.troyApart.serverbag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ServerBag_Database_Adapter {
	// Database Static Strings
	public static final String SB_DATABASE = "sbDataBase";
	public static final int DATABASE_VER = 1;

//	// Hash Table Static Strings
//	public static final String BOOL_TABLE = "boolTable";
//	public static final String BOOL_KEY = "key";
//	public static final String BOOL_VALUE = "value";
//	public static final String BOOL_VIRGIN_FLAG = "vCard";
	
//	//Contact List Use Table Static Strings
//	public static final String CONTACT_TABLE = "contactTable";
//	public static final String CONTACT_KEY = "key";
//	public static final String CONTACT_VALUE = "value";
//	public static final String CONTACT_IS_OK = "isOk";
//	public static final String CONTACT_VIRGIN_FLAG = "vCard";

	// Check Table Static Strings
	public static final String CHECK_TABLE = "checkTable";
	public static final String CHECK_ID = "ID";
	public static final String CHECK_DATE = "dateTime";
	public static final String CHECK_CHECK_CASH = "checkCash";
	public static final String CHECK_CHECK_CREDIT = "checkCredit";
	public static final String CHECK_TIP_CASH = "tipCash";
	public static final String CHECK_TIP_CREDIT = "tipCredit";
	public static final String CHECK_IS_PM = "isPM";
	public static final String CHECK_NOTE = "note";
	
	// Email Table Static Strings
	public static final String EMAIL_TABLE = "emailTable";
	public static final String EMAIL_ID = "ID";
	public static final String EMAIL = "email";

	private final Context context;
	private MySqlHelper dbHelper;
	private SQLiteDatabase db;

	public ServerBag_Database_Adapter(Context _context) {
		this.context = _context;
		dbHelper = new MySqlHelper(context, SB_DATABASE, null, DATABASE_VER);
	}

	public void openReadableDatabase() {
		if (db == null) {
			db = dbHelper.getReadableDatabase();
		} else {
			db.close();
			db = dbHelper.getReadableDatabase();
		}
	}

	public void openWriteableDatabase() {
		if (db == null) {
			db = dbHelper.getWritableDatabase();
		} else {
			db.close();
			db = dbHelper.getWritableDatabase();
		}
	}

	public void close() {
		if (db != null) {
			db.close();
		}

	}

//	public boolean isVirginRun() {
////		String query = "SELECT * FROM " + BOOL_TABLE + " WHERE " + BOOL_KEY
////				+ " = '" + BOOL_VIRGIN_FLAG + "'";
////
////		Cursor boolCursor = db.rawQuery(query, null);
////
////		if (boolCursor.moveToFirst()) {
////			String valueString = boolCursor.getString(boolCursor
////					.getColumnIndex(BOOL_VALUE));
////
////			if (valueString.matches("true")) {
////				return true;
////
////			} else {
////				return false;
////			}
////		}
////
////		insertVirginBool();
////		return true;
		
//		String query = "SELECT * FROM " + CONTACT_TABLE + " WHERE " + CONTACT_KEY
//				+ " = '" + CONTACT_VIRGIN_FLAG + "'";
//
//		Cursor contactCursor = db.rawQuery(query, null);
//
//		if (contactCursor.moveToFirst()) {
//			int value = contactCursor.getInt(contactCursor.getColumnIndex(CONTACT_VALUE));
//
//			if (value != 0) {
//				return true;
//
//			} else {
//				return false;
//			}
//		}
//
//		insertVirginBool();
//		return true;
//	}
	
//	public boolean isOkRun() {
//		String query = "SELECT * FROM " + CONTACT_TABLE + " WHERE " + CONTACT_KEY
//				+ " = '" + CONTACT_VIRGIN_FLAG + "'";
//
//		Cursor contactCursor = db.rawQuery(query, null);
//		
//		if (contactCursor.moveToFirst()) {
//			int value = contactCursor.getInt(contactCursor.getColumnIndex(CONTACT_IS_OK));
//
//			if (value != 0) {
//				return true;
//
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}

	public Cursor getAllNightChecksCursor() {
		String query = "SELECT * FROM " + CHECK_TABLE + " WHERE " + CHECK_IS_PM
				+ " = 'true'";

		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public Cursor getAllDayChecksCursor() {
		String query = "SELECT * FROM " + CHECK_TABLE + "WHERE " + CHECK_IS_PM
				+ " = 'false'";

		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public Cursor getAllChecksCursor() {
		String query = "SELECT * FROM " + CHECK_TABLE;

		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}
	
	public Cursor getAllChecksByDateCursor(String date) {
		String query = "SELECT * FROM " + CHECK_TABLE + " WHERE " + CHECK_DATE
				+ " = '" + date + "'";

		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public void insertChecks(String date, boolean isPM, double checkCash,
			double checkCredit, double tipCash, double tipCredit, String note) {
		ContentValues values = new ContentValues();
		values.put(CHECK_DATE, date);
		if (isPM) {
			values.put(CHECK_IS_PM, 1);
		} else {
			values.put(CHECK_IS_PM, 0);
		}
		values.put(CHECK_CHECK_CASH, checkCash);
		values.put(CHECK_CHECK_CREDIT, checkCredit);
		values.put(CHECK_TIP_CASH, tipCash);
		values.put(CHECK_TIP_CREDIT, tipCredit);
		values.put(CHECK_NOTE, note);

		db.insert(CHECK_TABLE, null, values);
	}

	public boolean deleteCheck(int id) {
		return db.delete(CHECK_TABLE, CHECK_ID + "=" + id, null) > 0;
	}

	public boolean deleteAllEntries() {
		return db.delete(CHECK_TABLE, null, null) > 0;
	}
	
	public Cursor getAllEmails() {
		String query = "SELECT * FROM " + EMAIL_TABLE;

		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}
	
	public void insertEmail(String email) {
		ContentValues values = new ContentValues();
		values.put(EMAIL, email);
		
		db.insert(EMAIL_TABLE, null, values);
	}
	
	public boolean deleteEmail(String email) {
		return db.delete(CHECK_TABLE, EMAIL + "= '" + email + "'", null) > 0;
	}

	public boolean deleteAllEmails() {
		return db.delete(EMAIL_TABLE, null, null) > 0;
	}

//	public void insertVirginBool() {
////		ContentValues values = new ContentValues();
////		values.put(BOOL_KEY, BOOL_VIRGIN_FLAG);
////		values.put(BOOL_VALUE, "true");
////		db.insert(BOOL_TABLE, null, values);
//
//		ContentValues values = new ContentValues();
//		values.put(CONTACT_KEY, CONTACT_VIRGIN_FLAG);
//		values.put(CONTACT_VALUE, 1);
//		values.put(CONTACT_IS_OK, 0);
//		db.insert(CONTACT_TABLE, null, values);
//	}

//	public void devirginize(boolean isOk) {
//
////		Log.d("SB", "devirginize");
////		ContentValues values = new ContentValues();
////		values.put(BOOL_VALUE, "false");
////		db.update(BOOL_TABLE, values, BOOL_KEY + " = '" + BOOL_VIRGIN_FLAG
////				+ "'", null);
////
////		Log.d("SB", "should have lost vCard");
//		ContentValues values = new ContentValues();
//		values.put(CONTACT_VALUE, 0);
//		if (isOk) {
//			values.put(CONTACT_IS_OK, 1);
//		}
//		db.update(CONTACT_TABLE, values, CONTACT_KEY + " = '" + CONTACT_VIRGIN_FLAG
//				+ "'", null);
//	}

	public static class MySqlHelper extends SQLiteOpenHelper {
		public MySqlHelper(Context context, String databaseName,
				CursorFactory factory, int version) {
			super(context, databaseName, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			// Create the Key Value Boolean table
			// db.execSQL(" CREATE TABLE " + BOOL_TABLE + " (" + BOOL_KEY
			// + " TEXT NOT NULL," + BOOL_VALUE + " TEXT NOT NULL );");

//			db.execSQL(" CREATE TABLE " + CONTACT_TABLE + " (" + CONTACT_KEY + " CHAR(5) NOT NULL, " + CONTACT_VALUE + " BIT(0) NOT NULL, " + CONTACT_IS_OK + " BIT(0) NOT NULL);");
			
			db.execSQL(" CREATE TABLE " + CHECK_TABLE + " (" + CHECK_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHECK_DATE
					+ " VARCHAR(10) NOT NULL, " + CHECK_CHECK_CASH
					+ " DECIMAL(5,2) NOT NULL, " + CHECK_CHECK_CREDIT
					+ " DECIMAL(5,2) NOT NULL, " + CHECK_TIP_CASH
					+ " DECIMAL(5,2) NOT NULL, " + CHECK_TIP_CREDIT
					+ " DECIMAL(5,2) NOT NULL, " + CHECK_IS_PM
					+ " BIT(0) NOT NULL, " + CHECK_NOTE + " TEXT);");
			
			db.execSQL(" CREATE TABLE " + EMAIL_TABLE + " (" + EMAIL_ID 
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

//			db.execSQL("DROP TABLE IF EXISTS " + BOOL_TABLE);
//			db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
			onCreate(db);
		}
	}
}
