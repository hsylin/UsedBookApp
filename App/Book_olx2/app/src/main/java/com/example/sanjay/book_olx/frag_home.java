package com.example.sanjay.book_olx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class frag_home extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    List<allad> adList;
    ListView listView;
    JSONArray res;
    alladadapter adapter;
    SearchView searchView;
    RadioButton radioButtonAuthor;
    RadioButton radioButtonBookName;
    RadioButton radioButtonDepartment;

    User user;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        referenceActivity = getActivity();

        parentHolder = inflater.inflate(R.layout.frag_home, container, false);

        adList = new ArrayList<>();
        listView = (ListView) parentHolder.findViewById(R.id.listview);
        searchView = parentHolder.findViewById(R.id.searchView);
        radioButtonAuthor = parentHolder.findViewById(R.id.radioButtonAuthor);
        radioButtonBookName = parentHolder.findViewById(R.id.radioButtonBookName);
        radioButtonDepartment = parentHolder.findViewById(R.id.radioButtonDepartment);
        res = ((nav_home) getActivity()).getallres();

        try {
            setlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new alladadapter(getContext(), R.layout.allads, adList);
        listView.setAdapter(adapter);

        // 设置SearchView的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 当用户按下输入键时触发
                // 在此处根据用户选择的单选按钮来过滤列表数据
                ArrayList<allad> tempArrayList = new ArrayList<>();
                for (allad item : adList) {
                    String itemValue = "";
                    if (radioButtonAuthor.isChecked()) {
                        itemValue = item.getAuthor().toLowerCase();
                    } else if (radioButtonBookName.isChecked()) {
                        itemValue = item.getBookname().toLowerCase();
                    } else if (radioButtonDepartment.isChecked()) {
                        itemValue = item.getDept().toLowerCase();
                    }
                    if (itemValue.contains(query.toLowerCase())) {
                        tempArrayList.add(item);
                    }
                }
                adapter = new alladadapter(getContext(), R.layout.allads, tempArrayList);
                listView.setAdapter(adapter);
                setsize();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter = new alladadapter(getContext(), R.layout.allads, adList);
                listView.setAdapter(adapter);
                setsize();
                return false;

            }
        });


        //attaching adapter to the listview
        listView.setAdapter(adapter);
        setsize();

        return parentHolder;
    }

    public void setlist() throws JSONException {
        for(int i=0; i<res.length(); i++){
            //JSONObject jsonobj = res.getJSONObject(i);
            adList.add(new allad(res.getJSONObject(i).getString("image"), res.getJSONObject(i).getString("book_name"), res.getJSONObject(i).getString("author"), res.getJSONObject(i).getString("price"), res.getJSONObject(i).getString("edition"), res.getJSONObject(i).getString("roll"), res.getJSONObject(i).getString("mobile"), res.getJSONObject(i).getString("name"), res.getJSONObject(i).getString("department"), res.getJSONObject(i).getString("mail")));

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    public void setsize(){
        ListAdapter listadp = listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

}
