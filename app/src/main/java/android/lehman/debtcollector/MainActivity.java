package android.lehman.debtcollector;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ActionBarActivity {


    private android.lehman.debtcollector.DebtDatabase dbHelper;
    private SimpleCursorAdapter myDebtsDataAdapter;
    private SimpleCursorAdapter yourDebtsDataAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new android.lehman.debtcollector.DebtDatabase(this);
        try {
            dbHelper.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        dbHelper.deleteAllDebts();


        dbHelper.insertTestDebts();

        generateListViews();

    }
    private void generateListViews() {

        Cursor myDebtsCursor = dbHelper.fetchAllMyDebts();
        Cursor yourDebtsCursor = dbHelper.fetchAllYourDebts();

        String[] columns = new String[] {
                android.lehman.debtcollector.DebtDatabase.KEY_DEBTOR,
                android.lehman.debtcollector.DebtDatabase.KEY_AMOUNT,
                android.lehman.debtcollector.DebtDatabase.KEY_NAME,
                android.lehman.debtcollector.DebtDatabase.KEY_DATE,
                android.lehman.debtcollector.DebtDatabase.KEY_DESCRIPTION
        };



        int[] to = new int[] {
                R.id.debtor,
                R.id.amount,
                R.id.name,
                R.id.date,
                R.id.description,
        };


        setupMyDebtsCursor(myDebtsCursor, columns, to);

        setupYourDebtsCursor(yourDebtsCursor, columns, to);

    }

    private void setupMyDebtsCursor(Cursor myDebtsCursor, String[] columns, int[] to)
    {
        myDebtsDataAdapter = new SimpleCursorAdapter(
                this, R.layout.debtor,
                myDebtsCursor,
                columns,
                to,
                0);

        ListView myDebtsListView = (ListView) findViewById(R.id.I_owe_listView);

        myDebtsListView.setAdapter(myDebtsDataAdapter);
    }

    private void setupYourDebtsCursor(Cursor yourDebtsCursor, String[] columns, int[] to)
    {
        yourDebtsDataAdapter = new SimpleCursorAdapter(
                this, R.layout.debtor,
                yourDebtsCursor,
                columns,
                to,
                0);

        ListView yourDebtsListView = (ListView) findViewById(R.id.you_owe_listView);


        yourDebtsListView.setAdapter(yourDebtsDataAdapter);
    }

}