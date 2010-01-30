package com.totsp.bookworm.data;

import android.test.AndroidTestCase;

import com.totsp.bookworm.model.Author;
import com.totsp.bookworm.model.Book;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

public class DataHelperTest extends AndroidTestCase {

   private DataHelper dh;

   public void setUp() throws Exception {
      super.setUp();
      // have to do this in setup, per test, can't use ctor (strange errors)
      this.dh = new DataHelper(this.getContext());
   }   
   
   public void testAuthor() {
      // more of an integration test for now, but, does the job

      // in case prev failure, clean up at start too (doesn't hurt)
      this.dh.deleteAuthor("Samuel Clemens");
      this.dh.deleteAuthor("Jack London");
      this.dh.deleteAuthor("Ernest Hemingway");
      this.dh.deleteAuthor("JD Salinger");

      // insert
      Author a1 = new Author("Samuel Clemens");
      Author a2 = new Author("Jack London");
      Author a3 = new Author("Ernest Hemingway");
      Author a4 = new Author("JD Salinger");

      this.dh.insertAuthor(a1);
      this.dh.insertAuthor(a2);
      this.dh.insertAuthor(a3);
      this.dh.insertAuthor(a4);

      // select
      Author a1r = this.dh.selectAuthor("Samuel Clemens");
      Author a2r = this.dh.selectAuthor("Jack London");
      Author a3r = this.dh.selectAuthor("Ernest Hemingway");
      Author a4r = this.dh.selectAuthor("JD Salinger");
      Assert.assertEquals("Samuel Clemens", a1r.getName());
      Assert.assertEquals("Jack London", a2r.getName());
      Assert.assertEquals("Ernest Hemingway", a3r.getName());
      Assert.assertEquals("JD Salinger", a4r.getName());

      // select all
      Set<Author> authors = this.dh.selectAllAuthors();
      Assert.assertEquals(4, authors.size());

      // delete
      this.dh.deleteAuthor("Samuel Clemens");
      this.dh.deleteAuthor("Jack London");
      this.dh.deleteAuthor("Ernest Hemingway");
      this.dh.deleteAuthor("JD Salinger");
      a1r = this.dh.selectAuthor("Samuel Clemens");
      a2r = this.dh.selectAuthor("Jack London");
      a3r = this.dh.selectAuthor("Ernest Hemingway");
      a4r = this.dh.selectAuthor("JD Salinger");
      Assert.assertNull(a1r);
      Assert.assertNull(a2r);
      Assert.assertNull(a3r);
      Assert.assertNull(a4r);
   }

   public void testBook() {
      // more of an integration test for now, but, does the job

      // in case prev failure, clean up at start too (doesn't hurt)
      this.dh.deleteBook("1231");
      this.dh.deleteBook("1232");
      this.dh.deleteBook("1233");

      // insert
      Author a1 = new Author("Samuel Clemens");
      Author a2 = new Author("Jack London");
      Author a3 = new Author("Ernest Hemingway");
      Author a4 = new Author("JD Salinger");

      // (2 authors will already exist, 2 new, on first pass)
      this.dh.insertAuthor(a1);
      this.dh.insertAuthor(a2);

      Set<Author> authors1 = new HashSet<Author>();
      authors1.add(a1);
      authors1.add(a2);

      Set<Author> authors2 = new HashSet<Author>();
      authors2.add(a3);
      authors2.add(a4);
      
      Set<Author> authors3 = new HashSet<Author>();
      authors3.addAll(authors1);
      authors3.addAll(authors2);

      Book b1 = new Book("1231", "book1");
      b1.setDescription("desc");
      b1.setFormat("format");
      b1.setOverviewUrl("overviewUrl");
      b1.setPublisher("pub");
      b1.setSubject("subject");
      b1.setAuthors(authors1);
      this.dh.insertBook(b1);

      Book b2 = new Book("1232", "book2");
      b2.setDescription("desc");
      b2.setFormat("format");
      b2.setOverviewUrl("overviewUrl");
      b2.setPublisher("pub");
      b2.setSubject("subject");
      b2.setAuthors(authors2);
      this.dh.insertBook(b2);

      Book b3 = new Book("1233", "book3");
      b3.setDescription("desc");
      b3.setFormat("format");
      b3.setOverviewUrl("overviewUrl");
      b3.setPublisher("pub");
      b3.setSubject("subject");
      b3.setAuthors(authors3);
      this.dh.insertBook(b3);
      
      // select
      Book b1r = this.dh.selectBook("1231");
      Assert.assertEquals("book1", b1r.getTitle());
      Assert.assertEquals("desc", b1r.getDescription());
      Assert.assertEquals("format", b1r.getFormat());
      Assert.assertEquals("overviewUrl", b1r.getOverviewUrl());
      Assert.assertEquals("pub", b1r.getPublisher());
      Assert.assertEquals("subject", b1r.getSubject());
      Assert.assertEquals(2, b1r.getAuthors().size());
      for (Author a : b1r.getAuthors()) {
         Assert.assertTrue(a.getName().equals("Samuel Clemens") || a.getName().equals("Jack London"));
      }

      Book b2r = this.dh.selectBook("1232");
      Assert.assertEquals("book2", b2r.getTitle());
      Assert.assertEquals("desc", b2r.getDescription());
      Assert.assertEquals("format", b2r.getFormat());
      Assert.assertEquals("overviewUrl", b2r.getOverviewUrl());
      Assert.assertEquals("pub", b2r.getPublisher());
      Assert.assertEquals("subject", b2r.getSubject());
      Assert.assertEquals(2, b2r.getAuthors().size());
      for (Author a : b2r.getAuthors()) {
         Assert.assertTrue(a.getName().equals("Ernest Hemingway") || a.getName().equals("JD Salinger"));
      }

      Book b3r = this.dh.selectBook("1233");
      Assert.assertEquals("book3", b3r.getTitle());
      Assert.assertEquals("desc", b3r.getDescription());
      Assert.assertEquals("format", b3r.getFormat());
      Assert.assertEquals("overviewUrl", b3r.getOverviewUrl());
      Assert.assertEquals("pub", b3r.getPublisher());
      Assert.assertEquals("subject", b3r.getSubject());
      Assert.assertEquals(4, b3r.getAuthors().size());
      for (Author a : b3r.getAuthors()) {
         Assert.assertTrue(a.getName().equals("Samuel Clemens") || a.getName().equals("Jack London")
                  || a.getName().equals("Ernest Hemingway") || a.getName().equals("JD Salinger"));
      }

      // select all
      Set<Book> books = this.dh.selectAllBooks();
      Assert.assertEquals(3, books.size());

      // delete
      /*
      this.dh.deleteBook("1231");
      this.dh.deleteBook("1232");
      this.dh.deleteBook("1233");
      b1r = this.dh.selectBook("1231");
      b2r = this.dh.selectBook("1232");
      b3r = this.dh.selectBook("1233");
      Assert.assertNull(b1r);
      Assert.assertNull(b2r);
      Assert.assertNull(b3r);   
      */
   }

}
