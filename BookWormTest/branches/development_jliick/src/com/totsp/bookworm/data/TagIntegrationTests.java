package com.totsp.bookworm.data;


import com.totsp.bookworm.data.DataConstants;
import com.totsp.bookworm.data.DataManager;


import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;




/**
 * Tags integration tests.
 * These tests use a fake data set that is distinct from those in {@link DataManagerTest} which requires that the
 * {@link DataManagerTest#testInsertTag()} and {@link DataManagerTest#testTagBook()} tests pass as a precondition. 
 * <br>
 * The test data set includes generated authors, books and tags with tags applied to books in the following patterns:
 * <ul>
 * 		<li> Each tag applied to a single book</li>
 * 		<li> A single book with all tags</li>
 * 		<li> A single book with every other tag applied</li>
 *  	<li> A single book with no tags</li>
 * </ul>
 * TODO: Rewrite the setUp method to use pre-built DB's once DataManager has been refactored to work with arbitrary
 * 	  DB files.
 */
public class TagIntegrationTests  extends AndroidTestCase  {
	
	static private final int NUM_BOOK_ENTRIES = 20; 
	static private final int NUM_AUTHOR_ENTRIES = 20; 
	static private final int NUM_TAG_ENTRIES = 10; 
	
	static private final int ALL_TAGS_BOOK_ID = NUM_TAG_ENTRIES + 1;
	static private final int ALTERNATING_TAGS_BOOK_ID = NUM_TAG_ENTRIES + 2;
	static private final int UNTAGGED_BOOK_ID = NUM_TAG_ENTRIES + 3;	
	
	private Context context;
	private InstrumentedDataManager dataMgr;
	private TestDataSet dataSet;
    

    /*
     * Sets up the test environment before each test.
     * @see android.test.ActivityInstrumentationTestCase2#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        // Call the super constructor (required by JUnit)
         super.setUp();
        
        context = getContext();

        dataMgr = new InstrumentedDataManager(context);
        dataMgr.deleteAllDataYesIAmSure();
        
        dataSet = new TestDataSet();
    	dataSet.createFakeAuthors(NUM_AUTHOR_ENTRIES);
    	dataSet.createFakeBooks(NUM_BOOK_ENTRIES);
    	dataSet.createFakeTags(NUM_TAG_ENTRIES);
    	
    	for (int i = 0; i < dataSet.getBooks().size(); i++) {
    		dataMgr.insertBook(dataSet.getBooks().get(i));
    	}
        
    	for (int i = 0; i < dataSet.getTags().size(); i++) {
    		dataMgr.insertTag(dataSet.getTags().get(i));
    	}
    	
    	// Add one tag to each book in range
    	for (int i = 1; i < NUM_TAG_ENTRIES+1; i++) {
        	dataMgr.addTagToBook(i, i);    		
    	}
    	
    	// Add all tags to a single book
    	for (int i = 1; i < NUM_TAG_ENTRIES+1; i++) {
        	dataMgr.addTagToBook(i, ALL_TAGS_BOOK_ID);    		
    	}
    	
    	// Add every other tag to a single book
    	for (int i = 1; i < NUM_TAG_ENTRIES+1; i += 2) {
        	dataMgr.addTagToBook(i, ALTERNATING_TAGS_BOOK_ID);    		
    	}
 }

    /**
     * Tests the initial values of key objects in the app under test, to ensure the initial conditions make sense. 
     * If one of these is not initialized correctly, then subsequent tests are suspect and should be ignored.
     */
    public void testPreconditions() {
    	assertNotNull(context);
    	assertNotNull(dataMgr);   	
    	
    	// Confirm that enough books exist in the DB for the test
    	assertTrue(NUM_BOOK_ENTRIES >= UNTAGGED_BOOK_ID);

    	// Note that since authors are randomly assigned to books, not every created author is guaranteed to exist in DB
       	Log.v("BookWormTest", "Author Count: " 
		              + String.valueOf(dataMgr.countFromTable(DataConstants.AUTHOR_TABLE, "")));
       	Log.v("BookWormTest", "Book/Author Count: " 
		              + String.valueOf(dataMgr.countFromTable(DataConstants.BOOKAUTHOR_TABLE, "")));

       	assertEquals(NUM_BOOK_ENTRIES, dataMgr.countFromTable(DataConstants.BOOK_TABLE, ""));
       	assertEquals(NUM_TAG_ENTRIES, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
   }	
 
    
    /**
     * Utility method to create assert fail messages for {@link #testTagSelectionCursor} method.
     * 
     * @param bookId  ID of book that which had the assertion failure
     * @param cursor  Tag selection cursor at the row where the assertion failed
     * @return		  A string containing the location of the failure
     */
	private String failTagMsg(long bookId, Cursor cursor) {
		return String.format("Book: %d   Tag: %d", bookId, cursor.getInt(cursor.getColumnIndex("_id")));
	}
   
    /**
     * Verifies that the cursor used to populate tag multi-selection ListView for assigning tags to an item is setup
     * correctly.
     */
    public void testTagSelectionCursor() {    	
    	Cursor c;
    	   	
    	// Verify that one tag checked for each book in range
    	for (int b = 1; b < NUM_TAG_ENTRIES+1; b++) {
        	c = dataMgr.getTagSelectorCursor(b);
        	// Each selector cursor must have a row for every tag
        	assertEquals(NUM_TAG_ENTRIES, c.getCount());
        	
			c.moveToFirst();
			for (int t = 1; t < NUM_TAG_ENTRIES+1; t++) {
				// Must compare by column _id since cursor is ordered by tag text rather than tag ID
				if (b == c.getInt(c.getColumnIndex("_id"))) {
					assertEquals(failTagMsg(b, c), 1, c.getInt(c.getColumnIndex("tagged")));
				}
				else {
					assertEquals(failTagMsg(b, c), 0, c.getInt(c.getColumnIndex("tagged")));
				}
				c.moveToNext();
			}
			if (!c.isClosed()) {
				c.close();
			}
    	}

    	// Verify that all tags are checked for book 
    	c = dataMgr.getTagSelectorCursor(ALL_TAGS_BOOK_ID);
    	// Each selector cursor must have a row for every tag
    	assertEquals(NUM_TAG_ENTRIES, c.getCount());
    	
		c.moveToFirst();
		for (int t = 1; t < NUM_TAG_ENTRIES+1; t++) {
			assertEquals(failTagMsg(ALL_TAGS_BOOK_ID, c), 1, c.getInt(c.getColumnIndex("tagged")));
			c.moveToNext();
		}
		if (!c.isClosed()) {
			c.close();
		}
    	
		// Verify that every other tag is checked for book
    	c = dataMgr.getTagSelectorCursor(ALTERNATING_TAGS_BOOK_ID);
    	// Each selector cursor must have a row for every tag
    	assertEquals(NUM_TAG_ENTRIES, c.getCount());
    	
		c.moveToFirst();
		for (int t = 1; t < NUM_TAG_ENTRIES; t++) {
			// Must compare by column _id since cursor is ordered by tag text rather than tag ID
			if (c.getInt(c.getColumnIndex("_id")) % 2 == 0) {
				assertEquals(failTagMsg(ALTERNATING_TAGS_BOOK_ID, c), 0, c.getInt(c.getColumnIndex("tagged")));
			}
			else {
				assertEquals(failTagMsg(ALTERNATING_TAGS_BOOK_ID, c), 1, c.getInt(c.getColumnIndex("tagged")));
			}
			c.moveToNext();
		}
		if (!c.isClosed()) {
			c.close();
		}
		
		// Verify that no tags are checked for the un-tagged book
    	c = dataMgr.getTagSelectorCursor(UNTAGGED_BOOK_ID);
    	// Each selector cursor must have a row for every tag
    	assertEquals(NUM_TAG_ENTRIES, c.getCount());
    	
		c.moveToFirst();
		for (int t = 1; t < NUM_TAG_ENTRIES; t++) {
			assertEquals(failTagMsg(ALTERNATING_TAGS_BOOK_ID, c), 0, c.getInt(c.getColumnIndex("tagged")));
			c.moveToNext();
		}
		if (!c.isClosed()) {
			c.close();
		}
    	
    }
    
    
    /**
     *  Verifies the generated text listing all applied tags is correct
     */
    public void testTagsString() {
    	StringBuilder sb = new StringBuilder();
    	
    	// Verify that one tag is included for each book in range 
    	// NOTE: dataSet tags use 0-based index while the DB is 1-based, so we add on for book ID
    	for (int t = 0; t < NUM_TAG_ENTRIES; t++) {
    		assertEquals(dataSet.getTags().get(t).text, dataMgr.getBookTagsString(t+1));
    	}

    	// Verify that all tags are checked for book 
    	for (int t = 0; t < NUM_TAG_ENTRIES; t++) {
    		if (t > 0) {
    			sb.append(", ");
    		}
    		sb.append(dataSet.getTags().get(t).text);
     	}
   		assertEquals(sb.toString(), dataMgr.getBookTagsString(ALL_TAGS_BOOK_ID));
    	
		// Verify that every other tag is included for book
   		sb.delete(0, sb.length());
    	for (int t = 0; t < NUM_TAG_ENTRIES; t += 2) {
    		if (t > 0) {
    			sb.append(", ");
    		}
    		sb.append(dataSet.getTags().get(t).text);
     	}
   		assertEquals(sb.toString(), dataMgr.getBookTagsString(ALTERNATING_TAGS_BOOK_ID));
		
		// Verify that no tags are included for the un-tagged book
   		assertEquals("", dataMgr.getBookTagsString(UNTAGGED_BOOK_ID));
    	
    }
    
    
    /**
     *  Sub-class of DataManager, exposing protected interfaces for testing. 
     *
     */
    private class InstrumentedDataManager extends DataManager {
		public InstrumentedDataManager(Context context) {
			super(context);
		} 
		
		public int countFromTable(String table, final String whereClause) {
			return getCountFromTable(table, whereClause);
		}
    }

    
    
    
}    
   
    
 