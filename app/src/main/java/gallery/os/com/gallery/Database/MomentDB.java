package gallery.os.com.gallery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MomentDB extends SQLiteOpenHelper {
    private static final String database_NAME = "MomentDB";
    private static final String TABLENAME = "Moment";
    private static final String MOMENTID = "momentid";
    private static final String FILEPATH = "filepath";


    private static final String MOMENTLIST = "Momentlist";
    private static final String MOMENTNAME = "momentname";
    private static final String MOMENTTHUMBIMAGE = "momentThumbImage";
    private static final String MOMENTDATE = "momentDate";

    Context context;
    private SQLiteDatabase db, sql;

    public MomentDB(Context context) {
        super(context, database_NAME, null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  IF NOT EXISTS Moment ( id INTEGER PRIMARY KEY AUTOINCREMENT,  momentid TEXT , filepath TEXT )");
        db.execSQL("CREATE TABLE  IF NOT EXISTS Momentlist ( id INTEGER PRIMARY KEY AUTOINCREMENT, momentname TEXT, momentThumbImage TEXT, momentDate LONG )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void open() {
        this.db = getWritableDatabase();
        this.sql = getReadableDatabase();
    }

    public void close() {
        this.db.close();
        this.sql.close();
    }


    public void createMoment(String name, ArrayList<String> pathlist, String momentImage) {
        if (FindMoment(name)) {
            CreateNewMoment(name, momentImage);
            String momentID = FindMomentID(name);
            for (int i = 0; i < pathlist.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(MOMENTID, momentID);
                values.put(FILEPATH, pathlist.get(i));
                this.db.insert(TABLENAME, null, values);
            }
        } else {
            Toast.makeText(context, "Moment Allready Created", Toast.LENGTH_SHORT).show();
        }
    }


    private void CreateNewMoment(String name, String momentimage) {
        ContentValues values = new ContentValues();
        values.put(MOMENTNAME, name);
        values.put(MOMENTTHUMBIMAGE, momentimage);
        values.put(MOMENTDATE, System.currentTimeMillis());
        this.db.insert(MOMENTLIST, null, values);
    }


    private Boolean FindMoment(String name) {
        Cursor cursor = sql.query(MOMENTLIST, null, null, null, null, null, null);
        try {
            do {
                try {
                    if (cursor.getString(cursor.getColumnIndex("momentname")).equalsIgnoreCase(name)) {
                        return false;
                    }
                } catch (Exception ignored) {
                }
            } while (cursor.moveToNext());
        } catch (Exception e) {
            return true;
        }
        return true;

        /*String query = "SELECT * FROM  Momentlist WHERE momentname = " + name;
        try {
            Cursor cursor = db.rawQuery(query, null);
            return cursor.getCount() == 0;
        } catch (Exception e) {
            return true;
        }*/
    }

    private String FindMomentID(String name) {
        Cursor cursor = sql.query(MOMENTLIST, null, null, null, null, null, null);
        try {
            do {
                try {
                    if (cursor.getString(cursor.getColumnIndex("momentname")).equalsIgnoreCase(name)) {
                        return cursor.getString(cursor.getColumnIndex("id"));
                    }
                } catch (Exception ignored) {
                }
            } while (cursor.moveToNext());
        } catch (Exception e) {
            return null;
        }
        return null;
       /* String query = "SELECT * FROM  Momentlist WHERE 'momentname' = " + name + " ;";
        try {
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("id"));
        } catch (Exception e) {
            return null;
        }*/
    }


    public Cursor getMomentlistdata(String momentId) {
        String query = "SELECT * FROM Moment WHERE momentid = " + momentId;
        try {
            Cursor cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e) {
            return null;
        }
    }


    public void insertMomentImage(String momentId, String filePath) {
        try {

            ContentValues values = new ContentValues();
            values.put(MOMENTID, momentId);
            values.put(FILEPATH, filePath);
            this.db.insert(TABLENAME, null, values);
        }catch (Exception e){e.printStackTrace();}
    }


    public void deleteMomentImage(String momentId, String filePath) {
        try {
            String s = "DELETE FROM " + TABLENAME + " WHERE " + MOMENTID + " = '" + momentId + "' AND " + FILEPATH + " ='" + filePath + "';";
            this.db.execSQL(s);
        }catch (Exception e){e.printStackTrace();}

       }



    public void updateMomentName(String newName, String id) {
        try {
            this.db.execSQL("UPDATE " + MOMENTLIST + " SET " + MOMENTNAME + " = '" + newName + "' WHERE id = '" + id + "'");

        }catch (Exception e){e.printStackTrace();}
        }

    public void updateMomentTitleIcon(String newicon, String id) {
        try {
            this.db.execSQL("UPDATE " + MOMENTLIST + " SET " + MOMENTTHUMBIMAGE + " = '" + newicon + "' WHERE id = '" + id + "'");

        }catch (Exception e){e.printStackTrace();}       }


    public Cursor getMomentlist() {

        return sql.query(MOMENTLIST, null, null, null, null, null, null);
    }

    public Cursor tempgetMomentlist() {
        return sql.query(TABLENAME, null, null, null, null, null, null);
    }

    public void removeMoment(String id) {
        try {
            String s = "DELETE FROM " + TABLENAME + " WHERE " + MOMENTID + " = '" + id + "';";
            this.db.execSQL(s);
        } catch (Exception ignored) {
        }
        try {
            String s = "DELETE FROM " + MOMENTLIST + " WHERE id = '" + id + "';";
            this.db.execSQL(s);
        } catch (Exception ignored) {
        }

    }


    public void deleteAllRecord() {
        try {
            db.delete(TABLENAME, null, null);
            db.delete(MOMENTLIST, null, null);
        }catch (Exception e){e.printStackTrace();}


       }

}
