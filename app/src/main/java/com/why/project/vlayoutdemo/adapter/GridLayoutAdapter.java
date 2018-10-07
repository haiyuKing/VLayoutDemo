package com.why.project.vlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapp.GlideApp;
import com.why.project.vlayoutdemo.R;
import com.why.project.vlayoutdemo.bean.ItemBean;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by HaiyuKing
 * Used 九宫格布局
 */
public class GridLayoutAdapter extends DelegateAdapter.Adapter{
	private Context mContext;
	private LayoutHelper mLayoutHelper;
	private List<ItemBean> mItemBeanList;
	private int mCount = 1;

	public GridLayoutAdapter(Context context, LayoutHelper layoutHelper,List<ItemBean> datas,int mCount){
		this.mContext = context;
		this.mLayoutHelper = layoutHelper;
		this.mItemBeanList = datas;
		this.mCount = mCount;
	}

	@Override
	public LayoutHelper onCreateLayoutHelper() {
		return mLayoutHelper;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new GridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.vlayout_adapter_item_grid, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		ItemBean itemBean = mItemBeanList.get(position);
		GridViewHolder gridViewHolder = (GridViewHolder)holder;
		bindGrid(gridViewHolder,itemBean);
	}

	private void bindGrid(GridViewHolder holder, final ItemBean itemBean){
		//缩略图
		GlideApp.with(mContext)
				.load(itemBean.getImageUrl())
				//设置等待时的图片
				.placeholder(R.drawable.img_loading)
				//设置加载失败后的图片显示
				.error(R.drawable.img_error)
				.fitCenter()
				//默认淡入淡出动画
				.transition(withCrossFade())
				//缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
				.skipMemoryCache(false)
				//缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				//设置图片加载的优先级
				.priority(Priority.HIGH)
				.into(holder.mGridImg);
		//标题
		holder.mGridTitle.setText(itemBean.getTitle());
		//点击事件监听
		holder.mGridLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(gridCallback != null){
					gridCallback.clickGrid(itemBean);
				}
			}
		});

	}

	@Override
	public int getItemCount() {
		return mCount;
	}

	static class GridViewHolder extends RecyclerView.ViewHolder{

		private LinearLayout mGridLayout;
		private ImageView mGridImg;
		private TextView mGridTitle;

		public GridViewHolder(View itemView) {
			super(itemView);

			mGridLayout = (LinearLayout) itemView.findViewById(R.id.layout_grid);
			mGridImg = (ImageView) itemView.findViewById(R.id.img_grid);
			mGridTitle = (TextView) itemView.findViewById(R.id.tv_title_grid);
		}
	}

	/**九宫格点击回调*/
	public interface GridCallback{
		void clickGrid(ItemBean itemBean);
	}

	private GridCallback gridCallback;

	public void setGridCallback(GridCallback gridCallback){
		this.gridCallback = gridCallback;
	}
}
