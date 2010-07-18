package com.totsp.bookworm.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

public class ImageManagerTest extends AndroidTestCase {

   private ImageManager dih;
   
   public void setUp() throws Exception {
      super.setUp();
      // have to do this in setup, per test, can't use ctor (strange errors)
      this.dih = new ImageManager(this.getContext());
   }
   
   public void testSaveImage() {      
      
      Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), android.R.drawable.btn_star_big_on);
      this.dih.storeBitmap(bitmap, "testing1", 0L);
      
   }
   
}
