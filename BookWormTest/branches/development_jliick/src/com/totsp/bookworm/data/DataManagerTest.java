package com.totsp.bookworm.data;


import java.util.ArrayList;

import com.totsp.bookworm.data.DataManager;
import com.totsp.bookworm.model.Author;
import com.totsp.bookworm.model.Book;

import android.content.Context;
import android.test.AndroidTestCase;




/**
 *	DataManager integration tests. 
 *  The full data manager integration is required to test database operations involving multiple tables.
 */
public class DataManagerTest  extends AndroidTestCase  {
	
	private static final String BOOK_TITLE_1 = "Unit Tests for Dummys";
	private static final String BOOK_TITLE_2 = "Programming For Mobile Devices";
	private static final String BOOK_TITLE_3 = "Careers for the 21st Century";

	private static final String ISBN10_1 = "2345678901";
	private static final String ISBN13_1 = "23456789ABC01";
	private static final String ISBN10_2 = "0123456789";
	private static final String ISBN13_2 = "0123456789ABC";
	private static final String ISBN10_3 = "1234567890";
	private static final String ISBN13_3 = "123456789ABC0";

	private static final String AUTHOR_1 = "Tess Dumas";
	private static final String AUTHOR_2 = "Anne Droyd";
	private static final String AUTHOR_3 = "Dee Bugher";
	
	private Context context;
	private DataManager dataMgr;
    
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

        dataMgr = new DataManager(context);
        dataMgr.deleteAllDataYesIAmSure();
        
    	book1 = new Book(BOOK_TITLE_1);
       	book1.authors.add(new Author(AUTHOR_1));   	
     	book1.isbn10 = ISBN10_1;
    	book1.isbn13 = ISBN13_1;   	
 	   	
       	book2 = new Book(BOOK_TITLE_2);
    	book2.authors.add(new Author(AUTHOR_2));   	
    	book2.isbn10 = ISBN10_2;
    	book2.isbn13 = ISBN13_2;   	
    	
     	book3 = new Book(BOOK_TITLE_3);
       	book3.authors.add(new Author(AUTHOR_2));   	
      	book3.authors.add(new Author(AUTHOR_3));   	
     	book3.isbn10 = ISBN10_3;
    	book3.isbn13 = ISBN13_3;  
        
    }

    /**
     * Tests the initial values of key objects in the app under test, to ensure the initial
     * conditions make sense. If one of these is not initialized correctly, then subsequent
     * tests are suspect and should be ignored.
     */
    public void testPreconditions() {
    	assertNotNull(context);
    	assertNotNull(dataMgr);   	
    	
    }	
 
    
    
    public void testInsertBook() {
    	ArrayList<Book> books;
    	Book book;
    	    	    	
    	dataMgr.insertBook(book1);
    	dataMgr.insertBook(book2);
    	dataMgr.insertBook(book3);
    	
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_1);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_1, book.title);
    	assertEquals(ISBN10_1, book.isbn10);
    	assertEquals(ISBN13_1, book.isbn13);    	   	
    	assertEquals(1, book.authors.size());
    	assertEquals(AUTHOR_1, book.authors.get(0).name);
 
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_2);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_2, book.title);
    	assertEquals(ISBN10_2, book.isbn10);
    	assertEquals(ISBN13_2, book.isbn13);    	   	
    	assertEquals(1, book.authors.size());
    	assertEquals(AUTHOR_2, book.authors.get(0).name);
       	
    	books = dataMgr.selectAllBooksByTitle(BOOK_TITLE_3);
    	assertEquals(1, books.size());
    	book = books.get(0);
    	assertEquals(BOOK_TITLE_3, book.title);
    	assertEquals(ISBN10_3, book.isbn10);
    	assertEquals(ISBN13_3, book.isbn13);    	   	
    	assertEquals(2, book.authors.size());
       	assertTrue(book.authors.get(0).name.equals(AUTHOR_2) || book.authors.get(0).name.equals(AUTHOR_3));
       	assertTrue(book.authors.get(1).name.equals(AUTHOR_2) || book.authors.get(1).name.equals(AUTHOR_3));
    	    	
    	books = dataMgr.selectAllBooks();
    	assertEquals(3, books.size());
       	
    }  
    
}    
   
    
 