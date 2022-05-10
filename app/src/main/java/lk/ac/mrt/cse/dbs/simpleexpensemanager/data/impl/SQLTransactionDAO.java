package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLTransactionDAO implements TransactionDAO {

    private SQLHelper sqlHelper;

    public SQLTransactionDAO(Context context) {
        sqlHelper = SQLHelper.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate= dateFormat.format(date);

        ContentValues contentValues = new ContentValues();

        contentValues.put("date", stringDate);
        contentValues.put("accountNo", accountNo);
        contentValues.put("expenseType", expenseType.toString());
        contentValues.put("amount", amount);

        db.insert("transactions", null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions;",null);

        List<Transaction> transactions = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Date date = null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String accountNo =  cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            ExpenseType type = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("expenseType")));
            Double amount =  cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

            transactions.add(new Transaction(date,accountNo,type,amount));
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions LIMIT "+limit+"",null);

        List<Transaction> transactions = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Date date = null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String accountNo =  cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            ExpenseType type = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("expenseType")));
            Double amount =  cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

            transactions.add(new Transaction(date,accountNo,type,amount));
        }
        return transactions;
    }
}
