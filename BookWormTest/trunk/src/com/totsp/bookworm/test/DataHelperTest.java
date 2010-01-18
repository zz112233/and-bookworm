package com.totsp.bookworm.test;

import android.test.AndroidTestCase;

import com.totsp.bookworm.data.DataHelper;
import com.totsp.bookworm.model.Book;

import java.util.Date;

public class DataHelperTest extends AndroidTestCase {

   private DataHelper dh;
   
   public void setUp() throws Exception {
      super.setUp();
      // have to do this in setup, per test, can't use ctor (strange errors)
      this.dh = new DataHelper(this.getContext());
   }
   
   public void testBook() {      
      Book b1 = new Book(0, "1231", "title1", 0, new Date());
      dh.insertBook(b1);      
   }
   
}
