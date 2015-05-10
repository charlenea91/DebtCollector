package android.lehman.debtcollector;
import java.util.HashSet;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by Charlene on 5/2/15.
 */
public class DebtDatabase {


    public static final String KEY_ROWID = "_id";
    public static final String KEY_DEBTOR = "debtor";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_DESCRIPTION = "description";


    public static final String DEBTOR_I = "I owe money";
    public static final String DEBTOR_YOU = "You owe me money";

    private static final String TAG = "DebtsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "FriendlyDebts";
    private static final String SQLITE_TABLE = "DebtTable";
    private static final int DATABASE_VERSION = 1;

    private final android.content.Context mCtx;




    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_DEBTOR + "," +
                    KEY_AMOUNT + "," +
                    KEY_NAME + "," +
                    KEY_DATE + "," +
                    KEY_DESCRIPTION + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(android.content.Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DebtDatabase(android.lehman.debtcollector.MainActivity ctx) {
        this.mCtx = ctx;
    }

    public DebtDatabase open() throws java.sql.SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createDebt(String debtor, String amount, String name,
                           String date, String description) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DEBTOR, debtor);
        initialValues.put(KEY_AMOUNT, amount);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_DESCRIPTION, description);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllDebts() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchAllMyDebts() {

        Cursor myDebtsCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_DEBTOR,
                        KEY_AMOUNT, KEY_NAME, KEY_DATE, KEY_DESCRIPTION},
                KEY_DEBTOR + " = ?", new String[] { DEBTOR_I }, null, null, null);

        if (myDebtsCursor != null) {
            myDebtsCursor.moveToFirst();
        }

        return myDebtsCursor;
    }

    public Cursor fetchAllYourDebts() {

        Cursor yourDebtsCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_DEBTOR,
                        KEY_AMOUNT, KEY_NAME, KEY_DATE, KEY_DESCRIPTION},
                KEY_DEBTOR + " = ?", new String[] { DEBTOR_YOU }, null, null, null);

        if (yourDebtsCursor != null) {
            yourDebtsCursor.moveToFirst();
        }

        return yourDebtsCursor;
    }



    public void insertTestDebts() {

        createDebt(DEBTOR_I, "20", "Charlene", "1/1/2015", "Movies ");
        createDebt(DEBTOR_YOU, "100", "Manny", "5/2/2015", "Food");
        createDebt(DEBTOR_I, "50","Charlene", "3/12/2015", "Concert");
        createDebt(DEBTOR_YOU, "25", "Paul", "1/31/2015", "Food and Drinks");
        createDebt(DEBTOR_YOU, "10", "Aracely", "5/1/2015", "Living Social");
        createDebt(DEBTOR_YOU, "19", "Alex", "8/7/2014", "Groupon");
        createDebt(DEBTOR_I, "70", "Charlene", "2/14/2015", "Drink");

    }


}
