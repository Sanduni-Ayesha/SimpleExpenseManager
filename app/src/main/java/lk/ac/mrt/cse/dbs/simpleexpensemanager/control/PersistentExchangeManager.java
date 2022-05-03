package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLTransactionDAO;

public class PersistentExchangeManager extends ExpenseManager{

    public PersistentExchangeManager(Context context){
        try {
            setup(context);
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup(Context context) throws ExpenseManagerException {
        setAccountsDAO(new SQLAccountDAO(context));
        setTransactionsDAO(new SQLTransactionDAO(context));
    }
}
