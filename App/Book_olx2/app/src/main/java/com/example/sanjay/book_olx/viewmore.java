package com.example.sanjay.book_olx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.example.sanjay.book_olx.adapter.CommentExpandAdapter;
import com.example.sanjay.book_olx.bean.CommentBean;
import com.example.sanjay.book_olx.bean.CommentDetailBean;
import com.example.sanjay.book_olx.bean.ReplyDetailBean;
import com.example.sanjay.book_olx.view.CommentExpandableListView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sanjay.book_olx.adapter.CommentExpandAdapter;
import com.example.sanjay.book_olx.bean.CommentDetailBean;
import com.example.sanjay.book_olx.bean.ReplyDetailBean;
import com.example.sanjay.book_olx.view.CommentExpandableListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * by moos on 2018/04/20
 */
public class viewmore extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "viewmore";
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList =new ArrayList<>();;
    ;
    private BottomSheetDialog dialog;
    Timer timer = new Timer();
    private SwipeRefreshLayout swipeRefreshLayout;
    User user;
    JSONArray resaa;
    allad a;
    ProgressDialog p;
    String testJson ="{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"Success\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 1,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";
    String oldjson ;
    private boolean no =false;
    private String bookID="";

    public void getbean() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("bookID", bookID);
        String URL = "https://obscure-shelf-28256-aba2f7fd661a.herokuapp.com/getbean";
        final String mRequestBody = obj.toString();
        System.out.println(bookID);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //System.out.println("gsdfgdsfg");
                    resaa = new JSONArray(response);
                    System.out.println(resaa.toString());
                    if(resaa.toString().length()==2)
                    {
                        no =true;
                    }
                    else
                    {
                        JSONObject jsonobj = resaa.getJSONObject(0);
                        String old =jsonobj.toString();
                        Gson gson = new Gson();
                        commentBean = gson.fromJson(old, CommentBean.class);
                        oldjson = gson.toJson(commentBean);

                    }



                    commentsList = generateTestData();
                    initExpandableListView(commentsList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* if(p.isShowing()){
                    p.dismiss();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              /*  if(p.isShowing()){
                    p.dismiss();
                }*/
                System.out.println("Fuckkk");
                //Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);

    }

    public void postbean(JSONObject obj){
        String URL = "https://obscure-shelf-28256-aba2f7fd661a.herokuapp.com/newbean";
        final String mRequestBody = obj.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*if(p.isShowing()){
                    p.dismiss();
                }*/
                //Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if(p.isShowing()){
                    p.dismiss();
                }*/
                //Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    System.out.println( mRequestBody.getBytes("utf-8"));
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updatebean(JSONObject obj){
        String URL = "https://obscure-shelf-28256-aba2f7fd661a.herokuapp.com/updatebean";
        final String mRequestBody = obj.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                /*if(p.isShowing()){
                    p.dismiss();
                }*/
                //Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if(p.isShowing()){
                    p.dismiss();
                }*/
                //Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    System.out.println( mRequestBody.getBytes("utf-8"));
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPrefManager.getInstance(this).getUser();
        setContentView(R.layout.activity_viewmore);

        initView();


    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("comment section");
        a = (allad) getIntent().getParcelableExtra("obj");

        bookID = a.getName()+a.getBookname();

        try {
            getbean();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        startPollingForNewComments();
        /*try {
            getbean();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }*/

        // Set swipe refresh listener
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getbean();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/


    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(viewmore.this,"comment",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2018/04/20
     * func:生成测试数据
     * @return 评论数据
     */
     List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        if(no){
            commentBean = gson.fromJson(testJson, CommentBean.class);
            List<CommentDetailBean> commentList = commentBean.getData().getList();
            commentList.get(0).setNickName(a.getName());
            commentList.get(0).setContent("Book Name :"+a.getBookname()+"\n"+"Book price :"+a.getPrice()+"\n"+"Author :"+a.getAuthor()+"\n"+"Edition :"+a.getEdition()+"\n"+"Contact info :"+" "+a.getMail()+"\n"+"Personal info :"+a.getName()+" "+a.getDept());
            commentBean.getData().setList(commentList);
        }
        else {
            commentBean = gson.fromJson(oldjson, CommentBean.class);
        }
        commentBean.setbookID(bookID);
         //System.out.println(commentBean);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            timer.cancel();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();

                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();

                    CommentDetailBean detailBean = new CommentDetailBean(user.getName(), commentContent,"");

                    adapter.addTheCommentData(detailBean);

                    commentBean.getData().setList(commentsList);
                    Gson gson = new Gson();

                    String bb = gson.toJson(commentBean);
                    JSONObject json;
                    try {
                        json = new JSONObject(bb);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(json);

                    if (no)
                    {
                        postbean(json);
                    }
                    else
                    {
                        updatebean(json);
                    }
                    Toast.makeText(viewmore.this,"Comment successful",Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(viewmore.this,"Make sure your comment is not empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("reply " + commentsList.get(position).getNickName() + " 's comment:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(user.getName(),replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);

                    commentBean.getData().setList(commentsList);

                    Gson gson = new Gson();

                    String bb = gson.toJson(commentBean);
                    JSONObject json;
                    try {
                        json = new JSONObject(bb);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(json);

                    if (no)
                    {
                        postbean(json);
                    }
                    else
                    {
                        updatebean(json);
                    }

                    Toast.makeText(viewmore.this,"Comment successful",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(viewmore.this,"Make sure your comment is not empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void startPollingForNewComments() {
        // Start a timer to poll for new comments
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {

                    getbean();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Show a toast if new comments are available
                        //Toast.makeText(viewmore.this, "有新评论", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        // Schedule the timer to run every 30 seconds
        timer.schedule(task, 3000, 3000);
    }

}
