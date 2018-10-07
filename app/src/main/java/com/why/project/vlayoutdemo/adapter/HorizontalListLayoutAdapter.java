package com.why.project.vlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.why.project.vlayoutdemo.R;
import com.why.project.vlayoutdemo.bean.ItemBean;

import java.util.List;

/**
 * Created by HaiyuKing
 * Used 通栏布局——横向列表
 */
public class HorizontalListLayoutAdapter extends DelegateAdapter.Adapter{

	private Context mContext;
	private LayoutHelper mLayoutHelper;
	private List<ItemBean> mItemBeanList;
	private int mCount = 1;

	public HorizontalListLayoutAdapter(Context context, LayoutHelper layoutHelper,List<ItemBean> datas){
		this.mContext = context;
		this.mLayoutHelper = layoutHelper;
		this.mItemBeanList = datas;
	}

	@Override
	public LayoutHelper onCreateLayoutHelper() {
		return mLayoutHelper;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new HorizontalListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.vlayout_adapter_item_horizontal, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		HorizontalListViewHolder horizontalListViewHolder = (HorizontalListViewHolder)holder;
		bindHorizontalList(horizontalListViewHolder);
	}

	/**
	 * 横向列表样式
	 */
	private void bindHorizontalList(HorizontalListViewHolder holder) {
		//设置布局管理器(现行管理器ListView样式，支持横向、纵向)
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向
		holder.recyclerView.setLayoutManager(linearLayoutManager);

		//设置适配器
		HorizontalListViewAdapter horizontalListViewAdapter = new HorizontalListViewAdapter(mContext, mItemBeanList);
		holder.recyclerView.setAdapter(horizontalListViewAdapter);
		horizontalListViewAdapter.setOnHorizontalListItemClickLitener(new HorizontalListViewAdapter.OnHorizontalListItemClickLitener() {
			@Override
			public void onItemClick(ItemBean itemBean) {
				if(horizontalListCallback != null){
					horizontalListCallback.clickHorizontalItem(itemBean);
				}
			}
		});

	}

	@Override
	public int getItemCount() {
		return mCount;
	}

	static class HorizontalListViewHolder extends RecyclerView.ViewHolder{

		private RecyclerView recyclerView;

		public HorizontalListViewHolder(View itemView) {
			super(itemView);
			recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_horizontal);
		}
	}

	/**系列片点击回调*/
	public interface HorizontalListCallback{
		void clickHorizontalItem(ItemBean itemBean);
	}

	/**系列片点击回调*/
	private HorizontalListCallback horizontalListCallback;

	public void setHorizontalListCallback(HorizontalListCallback horizontalListCallback){
		this.horizontalListCallback = horizontalListCallback;
	}

}
