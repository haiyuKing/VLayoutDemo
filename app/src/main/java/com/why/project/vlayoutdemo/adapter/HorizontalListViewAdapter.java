package com.why.project.vlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapp.GlideApp;
import com.why.project.vlayoutdemo.R;
import com.why.project.vlayoutdemo.bean.ItemBean;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by HaiyuKing
 * Used 横向列表的适配器
 */
public class HorizontalListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
	/**上下文*/
	private Context myContext;
	private List<ItemBean> mItemBeanList;

	/**
	 * 构造函数
	 */
	public HorizontalListViewAdapter(Context context, List<ItemBean> datas) {
		myContext = context;
		this.mItemBeanList = datas;
	}

	/**
	 * 获取总的条目数
	 */
	@Override
	public int getItemCount() {
		return mItemBeanList.size();
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(myContext).inflate(R.layout.vlayout_adapter_item_horizontal_item, parent, false);
		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
		return itemViewHolder;
	}

	/**
	 * 声明列表项ViewHolder*/
	static class ItemViewHolder extends RecyclerView.ViewHolder
	{
		public ItemViewHolder(View view)
		{
			super(view);
			mHoriLayout = (LinearLayout) view.findViewById(R.id.layout_hori);
			mThumbImg = (ImageView) view.findViewById(R.id.img_thumb);
			mTitleTv = (TextView) view.findViewById(R.id.tv_title);
		}
		LinearLayout mHoriLayout;
		ImageView mThumbImg;
		TextView mTitleTv;
	}

	/**
	 * 将数据绑定至ViewHolder
	 */
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {
		final ItemBean itemBean = mItemBeanList.get(index);
		final ItemViewHolder itemViewHold = ((ItemViewHolder)viewHolder);

		//缩略图
		GlideApp.with(myContext)
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
				.into(itemViewHold.mThumbImg);
		//标题
		itemViewHold.mTitleTv.setText(itemBean.getTitle());

		itemViewHold.mHoriLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = itemViewHold.getLayoutPosition();//在增加数据或者减少数据时候，position和index就不一样了
				if(mOnItemClickLitener != null){
					mOnItemClickLitener.onItemClick(itemBean);
				}
			}
		});

	}

	/**
	 * 添加Item--用于动画的展现*/
	public void addItem(int position,ItemBean itemModel) {
		mItemBeanList.add(position,itemModel);
		notifyItemInserted(position);
	}
	/**
	 * 删除Item--用于动画的展现*/
	public void removeItem(int position) {
		mItemBeanList.remove(position);
		notifyItemRemoved(position);
	}

	/*=====================添加OnItemClickListener回调================================*/
	public interface OnHorizontalListItemClickLitener
	{
		/**列表项的点击事件*/
		void onItemClick(ItemBean itemBean);
	}

	private OnHorizontalListItemClickLitener mOnItemClickLitener;

	public void setOnHorizontalListItemClickLitener(OnHorizontalListItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

}
