package com.example.codercoral.interview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

class ImageLoader {
    private ImageView mImageView;
    private String mUrl;
    private LruCache<String, Bitmap> mCache;
    private ListView mListView;
    private Set<NewsAsyncTask> mTasks = new HashSet<>();

    ImageLoader(ListView listView) {
        mListView = listView;
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存时调用
                return value.getByteCount();
            }
        };
    }

    //添加到缓存
    private void addBitmapToCache(String Url, Bitmap bitmap) {
        if (getBitmapFromCache(Url) == null) {
            mCache.put(Url, bitmap);
        }
    }

    //从缓存中获取数据
    private Bitmap getBitmapFromCache(String mUrl) {
        return mCache.get(mUrl);
    }

/*    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl))
                mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };*/

    /**
     * 创建静态内部类
     */
    private class MyHandler extends Handler {
        //持有弱引用,GC回收时会被回收掉.
        private final WeakReference<ImageLoader> mActivty;

        MyHandler(ImageLoader imageLoader) {
            mActivty = new WeakReference<>(imageLoader);
        }

        @Override
        public void handleMessage(Message msg) {
            ImageLoader activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                //执行业务逻辑
                if (mImageView.getTag().equals(mUrl))
                    mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    }
    //url在NewsAdapter传入
    void showImageByThread(ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyHandler myHandler=new MyHandler(ImageLoader.this);
                Bitmap bm = getBitmapFromURL(url);
                Message message = Message.obtain();
                message.obj = bm;
                myHandler.sendMessage(message);

            }
        }.start();
    }

    //加载从start到end的所有图片
    void loadImages(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = NewsAdapter.URLS[i];
            //从缓存中取出对应图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中没有，那么去下载
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTasks.add(task);
            } else {
                ImageView imageView = mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    void showImageByAsyncTask(ImageView imageView, String Url) {
        //从缓存中取出对应图片
        Bitmap bitmap = getBitmapFromCache(Url);
        //如果缓存中没有，那么去下载
        if (bitmap == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    void cancelAllTasks() {
        if (mTasks != null) {
            for (NewsAsyncTask task : mTasks) {
                task.cancel(false);
            }
        }
    }

    private  class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {

        NewsAsyncTask(String myurl) {
//            mImageView = imageView;
            mUrl = myurl;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            //从网络获取图片
            Bitmap bitmap = getBitmapFromURL(strings[0]);
            if (bitmap != null) {
                //将不在缓存的图片加入缓存
                addBitmapToCache(strings[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
/*            if (mImageView.getTag().equals(mUrl))
                mImageView.setImageBitmap(bitmap);
        }*/
        }
    }

    Bitmap getBitmapFromURL(String urlString) {

        Bitmap bitmap;
        InputStream is = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

