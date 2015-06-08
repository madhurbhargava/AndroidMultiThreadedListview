package com.example.androidlistviewmultithreading;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter {
	
	private Context mContext;
	private String[] mImageUrlList;
	private String[] mTitle;
	//ThumbnailCache mLruCache;
	
	//cache for the images
	private static LinkedHashMap<String, Bitmap> mLru;
	
	public ImageListAdapter(Context context, String[] urlList, String[] titleList, int cacheSize)
	{
		super();
		mContext = context;
		mImageUrlList = urlList;
		mTitle = titleList;
		System.out.println(" XXXXXX cache size is::"+cacheSize);
		//mLruCache = new ThumbnailCache(cacheSize);
		mLru = new LinkedHashMap<String, Bitmap>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageUrlList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	private class ViewHolder
	{
		TextView title;
		ImageView image;
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//System.out.println("XXXXXXXXXXXXX getvieew called for position::"+position);
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (RelativeLayout)inflater.inflate(R.layout.image_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			holder.image.setTag(position);
			//holder.image.setBackgroundResource(R.drawable.loading);
			convertView.setTag(holder);
			
		}
		else
		{
			//recycled view came in
			holder = (ViewHolder)convertView.getTag();
			holder.image.setTag(position);
		}
		
		holder.title.setText("Item "+position);
		
		//a fucked up implementation in the main thread
		//long startms = System.currentTimeMillis();
		//Bitmap bmp = downloadBitmap(mImageUrlList[position]);
		//System.out.println("XXXXXXXXXXXX bmp for position::"+position+ "is::"+bmp);
		//holder.image.setImageBitmap(bmp);
		//long totaltime  = System.currentTimeMillis() - startms;
		//System.out.println("XXXXXXX total time to dowload image at position::"+position+" is "+totaltime+ " ms" );
		if(mLru.get(mImageUrlList[position]) == null)
		{
			holder.image.setImageResource(R.drawable.loading);
			if(!BitmapDownLoader.isDownloading(mImageUrlList[position]))
			{
				System.out.println("XXXXXXXXXXXX Image being DOWNLOADED for position::"+holder.image.getTag());
				new BitmapDownLoader(mImageUrlList[position], holder.image, position).execute();
			}
			//holder.image.setImageResource(R.drawable.loading);
		}
		else
		{
			System.out.println("XXXXXXXXXXXX Image being SET for position::"+holder.image.getTag());
			//get from cache
			holder.image.setImageBitmap(mLru.get(mImageUrlList[position]));
		}
		
		//notifyDataSetChanged();
		return convertView;
	}
	
	static Bitmap downloadBitmap(final String url) {
		Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }
	
	private static class BitmapDownLoader extends AsyncTask<String, Void, Bitmap>
	{
		private String mImageUrl = null;
		private WeakReference<ImageView> mImageRef = null;
		private int mPosition = -1;
		
		//This will prevent multiple async tasks from spawning for the same url
		private static final HashMap<String, String> sUrls = new HashMap<String, String>();
		
		public BitmapDownLoader(String url, ImageView iview, int pos)
		{
			mImageRef = new WeakReference<ImageView>(iview);
			mImageUrl = url;
			mPosition = pos;
			sUrls.put(mImageUrl, mImageUrl);
		}
		
		public static boolean isDownloading(String url)
		{
			if(sUrls.containsKey(url))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bmp = downloadBitmap(mImageUrl);
			return bmp;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(isCancelled())
			{
				result = null;
				
			}
			
			if(result == null)
			{
				return;
			}
			
			synchronized (mLru) {
				
				if(mImageRef != null )
				{
					ImageView imgView = mImageRef.get();
					if(imgView != null)
					{
						System.out.println("XXXXXXXXXXXXXX Task complete for::"+mPosition+" and " +
								"imgview tag::"+imgView.getTag());
						//if(mPosition == (Integer)imgView.getTag())
						{
							mImageRef.get().setImageBitmap(result);
						}
						//just update the lru
						mLru.put(mImageUrl, result);
						//sUrls.remove();
					}
				}
				
			}
			
		}
		
	}
	
	private class ThumbnailCache extends LruCache<String, Bitmap>
	{

		public ThumbnailCache(int maxSize) {
			super(maxSize);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected int sizeOf(String key, Bitmap value) {
			// TODO Auto-generated method stub
			return value.getByteCount();//super.sizeOf(key, value);
		}
		
	}
	
	
}
