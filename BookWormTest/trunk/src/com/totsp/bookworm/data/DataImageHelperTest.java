package com.totsp.bookworm.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

import com.totsp.bookworm.data.DataImageHelper;

public class DataImageHelperTest extends AndroidTestCase {

   private DataImageHelper dih;
   
   public void setUp() throws Exception {
      super.setUp();
      // have to do this in setup, per test, can't use ctor (strange errors)
      this.dih = new DataImageHelper(this.getContext(), "BookWorm", "BookWorm Cover Images", true);
   }
   
   public void testSaveImage() {      
      
      Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), android.R.drawable.btn_star_big_on);
      this.dih.saveImage("testing1", bitmap);
      
   }
   
}
