package com.why.project.vlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.why.project.vlayoutdemo.R;

/**
 * Created by HaiyuKing
 * Used stikcy布局， 可以配置吸顶或者吸底
 */
public class StickyTitleAdapter extends DelegateAdapter.Adapter{

	private Context mContext;
	private LayoutHelper mLayoutHelper;
	private String title;
	private int mCount = 1;

	public StickyTitleAdapter(Context context, LayoutHelper layoutHelper,String title){
		this.mContext = context;
		this.mLayoutHelper = layoutHelper;
		this.title = title;
	}

	@Override
	public LayoutHelper onCreateLayoutHelper() {
		return mLayoutHelper;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new StickyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.vlayout_adapter_item_sticky, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		StickyViewHolder stickyViewHolder = (StickyViewHolder)holder;

		stickyViewHolder.sticky_title.setText(title);
	}

	@Override
	public int getItemCount() {
		return mCount;
	}

	static class StickyViewHolder extends RecyclerView.ViewHolder{

		private TextView sticky_title;

		public StickyViewHolder(View itemView) {
			super(itemView);
			sticky_title = (TextView) itemView.findViewById(R.id.valyout_sticky_title);
		}
	}
}
