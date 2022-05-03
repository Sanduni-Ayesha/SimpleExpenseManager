package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper instance;

    public static SQLHelper getInstance(Context context){
        if (instance==null){
            instance = new SQLHelper(context);
        }
        return instance;
    }

    public SQLHelper(@Nullable Context context) {

        super(context, "190064G", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS 'accounts' ('accountNo' varchar(20) NOT NULL PRIMARY KEY, 'bank' varchar(20), 'name' varchar(50), 'initialBalance' double);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS 'transactions'('id' integer PRIMARY KEY AUTOINCREMENT, 'date' datetime(6),'accountNo' varchar(20), 'expenseType' varchar(8), 'amount' double, CONSTRAINT account_trans FOREIGN KEY (accountNo) REFERENCES accounts(accountNo));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sqLiteDatabase);
    }
}
