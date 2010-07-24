package com.totsp.bookworm.data;


import java.util.ArrayList;
import java.util.Random;

import com.totsp.bookworm.model.Author;
import com.totsp.bookworm.model.Book;
import com.totsp.bookworm.model.Tag;


/**
 * Utility class for creating fake data sets to be used in automated tests.
 */
public class TestDataSet {
	
	static final private int MAX_NUM_AUTHORS = 4;
	static final private int MIN_NUM_AUTHORS = 1;
	private ArrayList<Book> books;
	private ArrayList<Author> authors;
	private ArrayList<Tag> tags;
	private Random rnd;
	
	public TestDataSet() {
		authors = new ArrayList<Author>();
		books = new ArrayList<Book>();
		tags = new ArrayList<Tag>();
		rnd = new Random();
	}
	
	
	public ArrayList<Author> getAuthors()
	{
		return authors;
	}
	
	public ArrayList<Book> getBooks()
	{
		return books;
	}
	
	public ArrayList<Tag> getTags()
	{
		return tags;
	}
	
	public Author getRandomAuthor() {
		return authors.get(rnd.nextInt(authors.size()));
	}
	
	/**
	 * Create the specified number of fake books.
	 */
	public void createFakeBooks(final int numBooks) {
		Book book = new Book();
		int numAuthors;
		
		for (int i = 0; i < numBooks; i++) {
			
	    	book = new Book(String.format("Fake Book %d", i));
	    	numAuthors = rnd.nextInt(MAX_NUM_AUTHORS-MIN_NUM_AUTHORS) + MIN_NUM_AUTHORS;
	    	for (int n = 0; n < numAuthors; n++) {
	    		book.authors.add(getRandomAuthor());
	    	}
	    	
	     	book.isbn10 = String.format("%010d", i);
	    	book.isbn13 = String.format("%013d", i);
	 
	    	books.add(book);
		}
	}
	
	
	public void createFakeAuthors(final int numAuthors) {
		for (int i = 0; i < numAuthors; i++) {
			authors.add(new Author(String.format("John Doe%d", i)));
		}
	}
	
	
	public void createFakeTags(final int numTags) {
		for (int i = 0; i < numTags; i++) {
			tags.add(new Tag(String.format("Tag %d", i)));
		}
	}
}
