package com.bawei.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.bean.DataBean;
import com.bawei.okhttputils.OkHttp;
import com.bawei.recyclerviewdemo.R;
import com.bawei.view.PullBaseView;
import com.bawei.view.PullRecyclerView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;


public class Myfragment extends Fragment implements PullBaseView.OnHeaderRefreshListener, PullBaseView.OnFooterRefreshListener {

    private Myname adapter;


    private String url;
    private PullRecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment1, null);

        //获得子控件
        mRecyclerView = (PullRecyclerView) view.findViewById(R.id.lv);
        //acti传递的参数
        fragmentbundle();
        //设置布局管理
        //listview展示
       /* LinearLayoutManager mgr = new LinearLayoutManager(getActivity());
        mgr.setOrientation(LinearLayoutManager.VERTICAL);*///方向（纵、横）
       // mRecyclerView.setLayoutManager(mgr);

        //GridView展示
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 3);//
       mRecyclerView.setLayoutManager(mGrid);

        mRecyclerView.setOnHeaderRefreshListener(this);//设置下拉监听
        mRecyclerView.setOnFooterRefreshListener(this);//设置上拉监听

//        //设置动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //okhttp请求网络
        httpjson();
        return view;
    }
    @Override
    public void onFooterRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mDatas.add("TEXT更多");
                adapter.notifyDataSetChanged();
                mRecyclerView.onFooterRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  mDatas.add(0, "TEXT新增");
                adapter.notifyDataSetChanged();
                mRecyclerView.onHeaderRefreshComplete();
            }
        }, 3000);
    }
    private void fragmentbundle() {
        //acti传递的参数
        Bundle bundle = getArguments();
        url = bundle.getString("urls");
    }

    private void httpjson() {
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                System.out.print("数据加载失败。。。。。。。。。。。。。。。。。。。。。。。。。。。");
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                String json = result;
                Gson gson = new Gson();
                DataBean db = gson.fromJson(json, DataBean.class);
                ArrayList<DataBean.Data> mDatas = db.data;
                adapter = new Myname(mDatas, getActivity());
                mRecyclerView.setAdapter(adapter);
            }
        });

    }

    public class Myname extends RecyclerView.Adapter<Myname.MyViewHolder> {
        private ArrayList<DataBean.Data> mDatas;
        private Context context;

        public Myname(ArrayList<DataBean.Data> mDatas, Context context) {
            this.mDatas = mDatas;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
            View v = View.inflate(context, R.layout.recy_item, null);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position).efficacy);
            ImageLoader.getInstance().displayImage(mDatas.get(position).goods_img,
                    holder.iv);
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            ImageView iv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv_recy);
                iv = (ImageView) view.findViewById(R.id.iv_recy);
            }
        }
    }
}
