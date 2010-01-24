package com.totsp.bookworm.test;

import android.os.Handler;
import android.os.Message;
import android.test.AndroidTestCase;
import android.util.Log;

import com.totsp.bookworm.Splash;
import com.totsp.bookworm.data.HTTPRequestHelper;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequestHelperTest extends AndroidTestCase {

   private HTTPRequestHelper requestHelper;
   private Handler handler;

   public HTTPRequestHelperTest() {
      super();
   }

   public void setUp() throws Exception {
      super.setUp();

      // handler
      this.handler = new Handler() {
         public void handleMessage(final Message msg) {
            Log.i("BookWorm TEST", "MESSAGE PASSED");
            String responseError = msg.getData().getString(HTTPRequestHelper.HTTP_RESPONSE_ERROR);
            if (responseError != null) {
               Log.i("BookWorm TEST", "HTTP RESPONSE_ERROR- \n" + responseError);
            }
            String response = msg.getData().getString(HTTPRequestHelper.HTTP_RESPONSE);
            Log.d(Splash.APP_NAME, "HANDLER returned with msg - " + msg);
            Log.d(Splash.APP_NAME, " response - " + response);
            if (response != null) {
               Log.i("BookWorm TEST", "HTTP RESPONSE - \n" + response);
            }
         }
      };
      Log.i("BookWorm TEST", "handler instance at test - " + this.handler);

      // helper
      this.requestHelper = new HTTPRequestHelper(this.handler);
   }

   public void testGet() throws Exception {
      Log.i("BookWorm TEST", "TEST HTTP GET");

      String url = "http://www.yahoo.com";
      this.requestHelper.performGet(url);

      // test by looking at response in logs (response logged from HTTPRequestHelper)
      // can't seem to get the Message response here from other Thread, it's sent, never arrives
      // this is ONLY a problem in the test case though, works at run on emulator and device

      // wait on async operation, don't just finish
      // TODO figure out how this should be done - and test still doesn't work
      Thread.sleep(3000);
   }
   
   public void testGetSecure() throws Exception {

      Log.i("BookWorm TEST", "TEST HTTP GET SECURE");
     
      String url = "https://mail.google.com";     
      this.requestHelper.performGet(url);

      // wait on async operation, don't just finish
      Thread.sleep(3000);
   }

   public void testPost() throws Exception {

      Log.i("BookWorm TEST", "TEST HTTP POST");
     
      String url = "http://www.snee.com/xml/crud/posttest.html";
      Map<String, String> params = new HashMap<String, String>();
      params.put("fname", "first");
      params.put("lname", "lirst");
      this.requestHelper.performPost(url, params);

      // wait on async operation, don't just finish
      Thread.sleep(3000);
   }
   
   

}
