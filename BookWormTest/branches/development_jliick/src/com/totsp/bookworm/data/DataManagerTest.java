package com.totsp.bookworm.data;


import java.util.ArrayList;

import com.totsp.bookworm.data.DataConstants;
import com.totsp.bookworm.data.DataManager;
import com.totsp.bookworm.model.Author;
import com.totsp.bookworm.model.Book;
import com.totsp.bookworm.model.Tag;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;




/**
 *	DataManager integration tests. 
 *  The full data manager integration is required to test database operations involving multiple tables.
 */
public class DataManagerTest  extends AndroidTestCase  {
	
	private static final String BOOK_TITLE_1 = "Unit Tests for Dummys";
	private static final String BOOK_TITLE_2 = "Programming For Mobile Devices";
	private static final String BOOK_TITLE_3 = "Careers for the 21st Century";

	private static final String ISBN_10_1 = "2345678901";
	private static final String ISBN_13_1 = "23456789ABC01";
	private static final String ISBN_10_2 = "0123456789";
	private static final String ISBN_13_2 = "0123456789ABC";
	private static final String ISBN_10_3 = "1234567890";
	private static final String ISBN_13_3 = "123456789ABC0";

	private static final String AUTHOR_1 = "Tess Dumas";
	private static final String AUTHOR_2 = "Anne Droyd";
	private static final String AUTHOR_3 = "Dee Bugher";
	
	private Context context;
	private InstrumentedDataManager dataMgr;
    
	// Define books objects that can be used for multiple tests
	private Book book1;
	private Book book2;
	private Book book3;

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
                
    	book1 = new Book(BOOK_TITLE_1);
       	book1.authors.add(new Author(AUTHOR_1));   	
     	book1.isbn10 = ISBN_10_1;
    	book1.isbn13 = ISBN_13_1;   	
 	   	
       	book2 = new Book(BOOK_TITLE_2);
    	book2.authors.add(new Author(AUTHOR_2));   	
    	book2.isbn10 = ISBN_10_2;
    	book2.isbn13 = ISBN_13_2;   	
    	
     	book3 = new Book(BOOK_TITLE_3);
       	book3.authors.add(new Author(AUTHOR_2));   	
      	book3.authors.add(new Author(AUTHOR_3));   	
     	book3.isbn10 = ISBN_10_3;
    	book3.isbn13 = ISBN_13_3;  
        
    }

    /**
     * Tests the initial values of key objects in the app under test, to ensure the initial
     * conditions make sense. If one of these is not initialized correctly, then subsequent
     * tests are suspect and should be ignored.
     */
    public void testPreconditions() {
    	assertNotNull(context);
    	assertNotNull(dataMgr);   	
    	
    	assertEquals(0, dataMgr.countFromTable(DataConstants.BOOK_TABLE, ""));
    	assertEquals(0, dataMgr.countFromTable(DataConstants.BOOKAUTHOR_TABLE, ""));
    	assertEquals(0, dataMgr.countFromTable(DataConstants.AUTHOR_TABLE, ""));
    	assertEquals(0, dataMgr.countFromTable(DataConstants.BOOKUSERDATA_TABLE, ""));
    	
    	assertEquals(0, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
    	assertEquals(0, dataMgr.countFromTable(DataConstants.TAG_BOOKS_TABLE, ""));
    }	
 
    
    
    public void testInsertBook() {
    	ArrayList<Book> books;
    	Book book;
    	
    	// Get initial counts from DB, so that test will work even with a non-empty DB
       	int initBookCount = dataMgr.countFromTable(DataConstants.BOOK_TABLE, "");
       	int initAuthorCount = dataMgr.countFromTable(DataConstants.AUTHOR_TABLE, "");
       	int initBookAuthorCount = dataMgr.countFromTable(DataConstants.BOOKAUTHOR_TABLE, "");
       	int initUserBookCount = dataMgr.countFromTable(DataConstants.BOOKUSERDATA_TABLE, "");
    	    	
    	dataMgr.insertBook(book1);
    	dataMgr.insertBook(book2);
    	dataMgr.insertBook(book3);
    	
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_1);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_1, book.title);
    	assertEquals(ISBN_10_1, book.isbn10);
    	assertEquals(ISBN_13_1, book.isbn13);    	   	
    	assertEquals(1, book.authors.size());
    	assertEquals(AUTHOR_1, book.authors.get(0).name);
 
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_2);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_2, book.title);
    	assertEquals(ISBN_10_2, book.isbn10);
    	assertEquals(ISBN_13_2, book.isbn13);    	   	
    	assertEquals(1, book.authors.size());
    	assertEquals(AUTHOR_2, book.authors.get(0).name);
       	
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_3);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_3, book.title);
    	assertEquals(ISBN_10_3, book.isbn10);
    	assertEquals(ISBN_13_3, book.isbn13);    	   	
    	assertEquals(2, book.authors.size());
       	assertTrue(book.authors.get(0).name.equals(AUTHOR_2) || book.authors.get(0).name.equals(AUTHOR_3));
       	assertTrue(book.authors.get(1).name.equals(AUTHOR_2) || book.authors.get(1).name.equals(AUTHOR_3));
    	    	
       	assertEquals(initBookCount+3, dataMgr.countFromTable(DataConstants.BOOK_TABLE, ""));
     	assertEquals(initAuthorCount+3, dataMgr.countFromTable(DataConstants.AUTHOR_TABLE, ""));
     	assertEquals(initBookAuthorCount+4, dataMgr.countFromTable(DataConstants.BOOKAUTHOR_TABLE, ""));
     	assertEquals(initUserBookCount+3, dataMgr.countFromTable(DataConstants.BOOKUSERDATA_TABLE, ""));
    }
  
    
    
    
    public void testInsertTag() {
    	int initCount = dataMgr.countFromTable(DataConstants.TAG_TABLE, "");
    	dataMgr.insertTag(new Tag("Lost?"));
    	dataMgr.insertTag(new Tag("Series: Travis McGee"));
    	assertEquals(initCount+2, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
    }
  
    
    /**
     * Verifies that the user cannot insert duplicate tags
     */
    public void testDuplicateTag() {
    	int origCount;
    	
       	origCount = dataMgr.countFromTable(DataConstants.TAG_TABLE, "");
       	assertFalse(dataMgr.insertTag(new Tag("Duplicate")) == 0);
     	assertTrue(dataMgr.insertTag(new Tag("Duplicate")) == 0);
    	
    	assertEquals(origCount+1, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
    }
 
    
    /**
     * Verifies that the user cannot add tags identical to built-in tags
     */
    public void testInsertBuiltInTag() {
    	int origCount = dataMgr.countFromTable(DataConstants.TAG_TABLE, "");
    	
    	dataMgr.insertTag(new Tag("Own"));
    	dataMgr.insertTag(new Tag("Read"));
    	
    	assertEquals(origCount, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
    }
  
    
    /**
     * Verifies that the user cannot delete tags  built-in tags
     */
    public void testDeleteBuiltInTag() {
    	int origCount = dataMgr.countFromTable(DataConstants.TAG_TABLE, "");
    	Tag tag;
    	
    	// Verify that the built-in tags do exist
       	assertEquals(1, dataMgr.countFromTable(DataConstants.TAG_TABLE, "where tags.ttext=\"Own\""));
       	assertEquals(1, dataMgr.countFromTable(DataConstants.TAG_TABLE, "where tags.ttext=\"Read\""));
       	
       	// Attempt to delete them
       	tag = dataMgr.selectTag("Own"); 
    	dataMgr.deleteTag(tag.id);
    	
       	tag = dataMgr.selectTag("Read"); 
    	dataMgr.deleteTag(tag.id);
    	
    	assertEquals(origCount, dataMgr.countFromTable(DataConstants.TAG_TABLE, ""));
    }
    
 
    public void testTagBook() {
    	long tag1Id;
    	long tag2Id;
    	long book1Id;
    	long book2Id;
    	long book3Id;
     	int initCount = dataMgr.countFromTable(DataConstants.TAG_BOOKS_TABLE, "");
    	    	
     	tag1Id = dataMgr.insertTag(new Tag("Programming"));    	
     	tag2Id = dataMgr.insertTag(new Tag("Mine"));
    	
    	book1Id = dataMgr.insertBook(book1);
       	dataMgr.addTagToBook(tag1Id, book1Id);
       	dataMgr.addTagToBook(tag2Id, book1Id);
       	
       	book2Id = dataMgr.insertBook(book2);
       	dataMgr.addTagToBook(tag1Id, book2Id);
       	dataMgr.addTagToBook(tag2Id, book2Id);
       	
       	book3Id = dataMgr.insertBook(book3);
       	dataMgr.addTagToBook(tag2Id, book3Id);
    	  
       	assertTrue(dataMgr.isTagged(tag1Id, book1Id));
       	assertTrue(dataMgr.isTagged(tag1Id, book2Id));
       	assertFalse(dataMgr.isTagged(tag1Id, book3Id));
       	
       	assertTrue(dataMgr.isTagged(tag2Id, book1Id));       	
       	assertTrue(dataMgr.isTagged(tag2Id, book2Id));
       	assertTrue(dataMgr.isTagged(tag2Id, book3Id));
       	
    	assertEquals(initCount+5, dataMgr.countFromTable(DataConstants.TAG_BOOKS_TABLE, ""));
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
   
    
 