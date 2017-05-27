package com.udacity.stockhawk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.stockhawk.data.Contract.Quote;


class DbHelper extends SQLiteOpenHelper {


    private static final String NAME = "StockHawk.db";
    private static final int VERSION = 2;


    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createQuoteTable(db);
        createHistoryTable(db);
    }

    public void createQuoteTable(SQLiteDatabase db){
        String builder = "CREATE TABLE " + Quote.TABLE_NAME + " ("
                + Quote._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Quote.COLUMN_SYMBOL + " TEXT NOT NULL, "
                + Quote.COLUMN_PRICE + " REAL NOT NULL, "
                + Quote.COLUMN_ABSOLUTE_CHANGE + " REAL NOT NULL, "
                + Quote.COLUMN_PERCENTAGE_CHANGE + " REAL NOT NULL, "
                + Quote.COLUMN_HISTORY + " TEXT NOT NULL, "
                + "UNIQUE (" + Quote.COLUMN_SYMBOL + ") ON CONFLICT REPLACE);";

        db.execSQL(builder);

    }

    public void createHistoryTable(SQLiteDatabase db){
        String builder = "CREATE TABLE " + Contract.StockHistory.TABLE_NAME + " ("
                + Contract.StockHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.StockHistory.COLUMN_SYMBOL + " TEXT NOT NULL, "
                + Contract.StockHistory.COLUMN_TIMESTAMP + " REAL NOT NULL, "
                + Contract.StockHistory.COLUMN_PRICE + " REAL NOT NULL, "
                + "FOREIGN KEY (" + Contract.StockHistory.COLUMN_SYMBOL + ") "
                +"REFERENCES " + Contract.Quote.TABLE_NAME + " (" + Quote.COLUMN_SYMBOL + "));";


        db.execSQL(builder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + Quote.TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + Contract.StockHistory.TABLE_NAME);
        onCreate(db);
    }
}
