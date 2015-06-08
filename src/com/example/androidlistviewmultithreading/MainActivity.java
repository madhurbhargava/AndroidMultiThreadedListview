package com.example.androidlistviewmultithreading;


import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


public class MainActivity extends Activity {
	
	private String[] months = {"http://www.tivix.com/uploads/blog_pics/Android-logo.png",
			"http://crackberry.com/sites/crackberry.com/files/styles/large/public/topic_images/2013/ANDROID.png",
			"http://cdn2.ubergizmo.com/wp-content/uploads/2015/04/backup-android.jpg",
			"http://www.androidcentral.com/sites/androidcentral.com/files/postimages/684/podcast_featured_3.jpg",
			"http://www.theinquirer.net/IMG/331/193331/google-android-logo-green-black.jpg",
			"http://www.robadanerds.it/wp-content/uploads/2014/03/Android-Root.jpg",
			"http://farm8.staticflickr.com/7315/9046944633_881f24c4fa_s.jpg",
			"http://farm4.staticflickr.com/3777/9049174610_bf51be8a07_s.jpg",
			"http://farm8.staticflickr.com/7324/9046946887_d96a28376c_s.jpg",
			"http://farm3.staticflickr.com/2828/9046946983_923887b17d_s.jpg",
			"http://farm4.staticflickr.com/3810/9046947167_3a51fffa0b_s.jpg",
			"http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg",
			"http://farm4.staticflickr.com/3781/9046945893_f27db35c7e_s.jpg",
			"http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg",
			"http://farm8.staticflickr.com/7307/9046947621_67e0394f7b_s.jpg",
			"http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg",
			"http://farm4.staticflickr.com/3752/9046946459_a41fbfe614_s.jpg",
			"http://farm8.staticflickr.com/7403/9046946715_85f13b91e5_s.jpg"};
	
	public static final String[] sUrlList = {
		"http://www.tivix.com/uploads/blog_pics/Android-logo.png",
		"http://crackberry.com/sites/crackberry.com/files/styles/large/public/topic_images/2013/ANDROID.png",
		"http://cdn2.ubergizmo.com/wp-content/uploads/2015/04/backup-android.jpg",
		"http://www.androidcentral.com/sites/androidcentral.com/files/postimages/684/podcast_featured_3.jpg",
		"http://www.theinquirer.net/IMG/331/193331/google-android-logo-green-black.jpg",
		"http://www.robadanerds.it/wp-content/uploads/2014/03/Android-Root.jpg",
		"http://farm8.staticflickr.com/7315/9046944633_881f24c4fa_s.jpg",
		"http://farm4.staticflickr.com/3777/9049174610_bf51be8a07_s.jpg",
		"http://farm8.staticflickr.com/7324/9046946887_d96a28376c_s.jpg",
		"http://farm3.staticflickr.com/2828/9046946983_923887b17d_s.jpg",
		"http://farm4.staticflickr.com/3810/9046947167_3a51fffa0b_s.jpg",
		"http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg",
		"http://farm4.staticflickr.com/3781/9046945893_f27db35c7e_s.jpg",
		"http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg",
		"http://farm8.staticflickr.com/7307/9046947621_67e0394f7b_s.jpg",
		"http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg",
		"http://farm4.staticflickr.com/3752/9046946459_a41fbfe614_s.jpg",
		"http://farm8.staticflickr.com/7403/9046946715_85f13b91e5_s.jpg"
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView)findViewById(R.id.list);
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        System.out.println("XXXXXXXXX mem class::"+am.getMemoryClass());
        int availMemBytes = am.getMemoryClass()*1024*1024;
        System.out.println("XXXXX:: availmen :: "+availMemBytes);
        ImageListAdapter adapter = new ImageListAdapter(this, sUrlList, months, availMemBytes/4);
        list.setAdapter(adapter);
    }
    
    

    
}
