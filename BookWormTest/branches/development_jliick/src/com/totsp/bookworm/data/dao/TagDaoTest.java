package com.totsp.bookworm.data.dao;


import com.totsp.bookworm.data.DataConstants;
import com.totsp.bookworm.data.dao.TagDAO;
import com.totsp.bookworm.model.Tag;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;




/**
 *	TagDAO class unit tests. 
 *
 */
public class TagDaoTest  extends AndroidTestCase  {

	private static final String DATABASE_NAME = "test.db";
	private static final int DATABASE_VERSION = 12;
	
	private Context context;
	private SQLiteDatabase db;
	private TagDAO tagDAO;
    

    /*
     * Sets up the test environment before each test.
     * @see android.test.ActivityInstrumentationTestCase2#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        // Call the super constructor (required by JUnit)
         super.setUp();
        
        context = getContext();
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();

        tagDAO = new TagDAO(db);
        deleteAllData();
    }

    /**
     * Tests the initial values of key objects in the app under test, to ensure the initial
     * conditions make sense. If one of these is not initialized correctly, then subsequent
     * tests are suspect and should be ignored.
     */
    public void testPreconditions() {
    	assertNotNull(context);
    	assertNotNull(db);
    	assertNotNull(tagDAO);   	
    	
    	assertEquals(0, getCountFromTable(DataConstants.TAG_TABLE, ""));
    	assertEquals(0, getCountFromTable(DataConstants.TAG_BOOKS_TABLE, ""));
    }	
    
    
    public void testInsertTag() {
    	tagDAO.insert(new Tag("Lost?"));
    	tagDAO.insert(new Tag("Series: Travis McGee"));
    	assertEquals(2, getCountFromTable(DataConstants.TAG_TABLE, ""));
    }
  
    
    /**
     * Verifies that the user cannot insert duplicate tags
     */
    public void testDuplicateTag() {
    	int origCount;
    	
    	tagDAO.insert(new Tag("Duplicate"));
    	origCount = getCountFromTable(DataConstants.TAG_TABLE, "");
    	tagDAO.insert(new Tag("Duplicate"));
    	
    	assertEquals(origCount, getCountFromTable(DataConstants.TAG_TABLE, ""));
    }
 
    /**
     * Verifies that the user cannot add tags identical to built-in tags
     */
    public void testInsertBuiltInTag() {
    	int origCount = getCountFromTable(DataConstants.TAG_TABLE, "");
    	
    	tagDAO.insert(new Tag("Own"));
    	tagDAO.insert(new Tag("Read"));
    	
    	assertEquals(origCount, getCountFromTable(DataConstants.TAG_TABLE, ""));
    }
  
    /**
     * Verifies that the user cannot delete built-in tags
     */
    public void testDeleteBuiltInTag() {
    	int origCount = getCountFromTable(DataConstants.TAG_TABLE, "");
    	
    	// Verify that the built-in tags do exist
       	assertEquals(1, getCountFromTable(DataConstants.TAG_TABLE, "where tags.ttext=\"Own\""));
       	assertEquals(1, getCountFromTable(DataConstants.TAG_TABLE, "where tags.ttext=\"Read\""));
       	
       	// Attempt to delete them
    	tagDAO.delete("Own");
    	tagDAO.delete("Read");
    	
    	assertEquals(origCount, getCountFromTable(DataConstants.TAG_TABLE, ""));
    }
    
        
    private int getCountFromTable(final String table, final String whereClause) {
        int result = 0;
        Cursor c = db.rawQuery("select count(*) from " + table + " " + whereClause, null);
        if (c.moveToFirst()) {
           result = c.getInt(0);
        }
        if (!c.isClosed()) {
           c.close();
        }
        return result;
     }
 
    // super delete - clears all tables
    public void deleteAllData() {
       db.beginTransaction();
       try {
          db.delete(DataConstants.TAG_TABLE, null, null);
          db.delete(DataConstants.TAG_BOOKS_TABLE, null, null);
          db.setTransactionSuccessful();
       } finally {
          db.endTransaction();
       }
       db.execSQL("vacuum");
    }
   
    
    private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(final Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			TagDAO.onCreate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        TagDAO.onUpgrade(db, oldVersion, newVersion);
		}
    	
    }
    
}
