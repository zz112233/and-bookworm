

import com.totsp.bookworm.model.Book;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

// gotta use Junit3 and extend TestCase - to run this in Eclipse without extra config

public class BookTest extends TestCase {

   // TODO many tests here, invalid names, isbns, empty stuff, etc
   
   public void testBookList() {
      
      //Author a1 = new Author("author1");      
      
      Book b1 = new Book("1231565699", "title1");     
      Book b2 = new Book("1232565699", "title2");      
      Book b3 = new Book("1233565699", "title3");
      List<Book> books1 = new ArrayList<Book>();
      books1.add(b1);
      books1.add(b2);
      books1.add(b3);     
      
      System.out.println("b1 - " + b1);      
   }   
}