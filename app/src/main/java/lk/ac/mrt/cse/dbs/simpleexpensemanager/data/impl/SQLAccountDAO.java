package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLAccountDAO implements AccountDAO {

    private SQLHelper sqlHelper;

    public SQLAccountDAO(Context context) {
        sqlHelper = SQLHelper.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT accountNo FROM accounts",null);

        List<String> accountNos = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo =  cursor.getString(cursor.getColumnIndex("accountNo"));
            accountNos.add(accountNo);
        }
        return accountNos;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts;",null);

        List<Account> accounts = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo =  cursor.getString(cursor.getColumnIndex("accountNo"));
            String bank =  cursor.getString(cursor.getColumnIndex("bank"));
            String name =  cursor.getString(cursor.getColumnIndex("name"));
            Double balance =  cursor.getDouble(cursor.getColumnIndex("initialBalance"));
            accounts.add(new Account(accountNo,bank,name,balance));
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts WHERE accountNo=?",new String[] {accountNo});

        if (cursor.moveToFirst()) {
            String bank = cursor.getString(cursor.getColumnIndex("bank"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double balance = cursor.getDouble(cursor.getColumnIndex("initialBalance"));
            Account account = new Account(accountNo, bank, name, balance);
            return account;
        }else {
            throw new InvalidAccountException("Invalid account Number");
        }
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("name", account.getAccountHolderName());
        contentValues.put("bank",account.getBankName());
        contentValues.put("initialBalance", account.getBalance());

        db.insert("accounts", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        String[] accounts = {accountNo};
        db.delete("accounts", "accountNo=?", accounts);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        Account account = getAccount(accountNo);

        ContentValues contentValues = new ContentValues();

        if (expenseType==ExpenseType.EXPENSE){
            contentValues.put("initialBalance", account.getBalance()-amount);
        }else {
            contentValues.put("initialBalance", account.getBalance()+amount);
        }

        String[] accounts = {accountNo};
        db.update("accounts", contentValues, "accountNo=?",accounts);
    }
}
