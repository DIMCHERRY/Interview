package com.example.codercoral.interview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonActivity extends Activity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imglist);
        mListView = findViewById(R.id.lv_image);
        String mURL = "http://www.imooc.com/api/teacher?type=4&num=30";
        new NewsAsyncTask().execute(mURL);
    }

    /**
     * 将ｊｓｏｎ数据转化为ｎｅｗｓｂｅａｎ对象
     * @param url
     * @return
     */
    private List<NewsBean> getJsonData(String url) {
        List<NewsBean> newsBeansList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(readStream(new URL(url).openStream()));
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            NewsBean newsBean;
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.newsIconURL = jsonObject.getString("picSmall");
                newsBean.newsTitle = jsonObject.getString("name");
                newsBean.newsContent = jsonObject.getString("description");
                newsBeansList.add(newsBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsBeansList;
    }

    //读取流
    private String readStream(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result = result + line;
        }
        return result;
    }

    //实现网络的异步访问
    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {


        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            NewsAdapter adapter = new NewsAdapter(JsonActivity.this, newsBeans, mListView);
            mListView.setAdapter(adapter);
        }
    }
}
