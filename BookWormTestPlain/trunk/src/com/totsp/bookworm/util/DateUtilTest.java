package com.totsp.bookworm.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

// gotta use Junit3 and extend TestCase - to run this in Eclipse without extra config

public class DateUtilTest extends TestCase {

   // TODO many tests here, invalid names, isbns, empty stuff, etc

   public void testParseYearOnly() {
      // sometimes books just have year
      String in = "1997";
      Date out = DateUtil.parse(in);
      Assert.assertEquals(1997, out.getYear() + 1900);
      ///System.out.println("out - " + out);      
   }

   public void testFormatYear() {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 1997);
      cal.set(Calendar.DAY_OF_YEAR, 1);
      cal.set(Calendar.MILLISECOND, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.HOUR, 0);

      String out = DateUtil.format(cal.getTime());
      Assert.assertTrue(out.contains("1997"));
      ///System.out.println("out - " + out);      
   }
}