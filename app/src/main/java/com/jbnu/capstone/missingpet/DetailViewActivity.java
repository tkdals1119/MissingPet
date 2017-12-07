//마크표시를 눌렀을시 뜨는 디테일 뷰
package com.jbnu.capstone.missingpet;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class DetailViewActivity extends AppCompatActivity {
    EditText et; //댓글입력받는 창
    TextView tv; //임시로 댓글 보여주는 창(TEST 창 )
    Button btn; //입력된 댓글 전송 버튼
    StringBuffer sb = new StringBuffer(); //입력된 댓글을 저장해서 만들어진 데이터 덩어리
    StringBuffer JsonComment = new StringBuffer(); //작성된 댓글을 json server에 전송하기 위해 json 형식으로 만든다
    StringBuffer comments = new StringBuffer();
    Calendar calendar = Calendar.getInstance();//현재 시각

    Toolbar toolbar; //툴바 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);//액션바와 같게 만들어줌
        // ↓툴바에 홈버튼을 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ↓툴바의 홈버튼의 이미지를 변경(기본 이미지는 뒤로가기 화살표)//
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        et = (EditText)findViewById(R.id.et); //댓글 입력창
        tv = (TextView)findViewById(R.id.tv); //입력된 댓글 보여주는 창 (temp)
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(myClickListener); //댓글 전송 버튼
        ArrayList<String> nameList = new ArrayList<>();//

        // 댓글이 들어있는 json server의 URL 설정.
        String url = "https://gilmeow.herokuapp.com/posts/4";

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            tv.setText(s);
        }
    }



    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    View.OnClickListener myClickListener = new View.OnClickListener() //댓글 전송 버튼 눌렀을시 이벤트
    {
        @Override //json 값으로 전송 시키기위해
        public void onClick(View v)
        {
            JsonComment =null;
            StringBuffer JsonComment = new StringBuffer(); //위에서 선언했지만 null했으므로 재생성 해야한다.
            String startJson = "[";
            String endJson ="]";

            if(!sb.toString().equals(""))
            {
                sb.append(",");
            }
            String temp = "{\"comment\""+":"+"\""+et.getText().toString()+"\"" + "}";
            sb.append(temp);
            JsonComment.append(startJson+sb+endJson);
//            tv.setText(JsonComment);


            try
            {
                String str = calendar.getTime().toString().substring(4,16); //현재 시간
                comments = null;
                StringBuffer comments = new StringBuffer();//위에서 선언해 줬지만 초기화 시켜서 새로운 댓글을 입력시켜줌


                JSONArray jsonArray = new JSONArray(JsonComment.toString());
                System.out.println("GOOD");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    comments.append(jsonObject.getString("comment"));
                    comments.append(str);
                    comments.append("\n");
                }
                tv.setText(comments+"\n");



            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }



        }

    };


//    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app settings UI...
//                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
//                return super.onOptionsItemSelected(item);
//        }
//    }


}
