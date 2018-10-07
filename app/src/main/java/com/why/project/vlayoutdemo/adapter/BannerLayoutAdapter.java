package com.why.project.vlayoutdemo.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.why.project.vlayoutdemo.R;
import com.why.project.vlayoutdemo.banner.BannerImageLoader;
import com.why.project.vlayoutdemo.bean.ItemBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaiyuKing
 * Used 通栏布局——轮播图
 */
public class BannerLayoutAdapter extends DelegateAdapter.Adapter{

	private Context mContext;
	private LayoutHelper mLayoutHelper;
	private List<ItemBean> mItemBeanList;
	private int mCount = 1;

	public BannerLayoutAdapter(Context context, LayoutHelper layoutHelper, List<ItemBean> datas){
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
		return new BannerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.vlayout_adapter_item_banner, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		BannerViewHolder bannerViewHolder = (BannerViewHolder)holder;
		bindBanner(bannerViewHolder);
	}

	/**
	 * 轮播图样式
	 */
	private void bindBanner(final BannerViewHolder holder) {
		//轮播图的常规设置
		holder.mBannerView.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器局右显示
		//====加载Banner数据====
		holder.mBannerView.setImageLoader(new BannerImageLoader());//设置图片加载器
		//设置只显示指示器
		holder.mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

		setBannerData(holder);//设置轮播图的数据
	}

	/**设置轮播图的数据*/
	private void setBannerData(final BannerViewHolder holder){

		List<String> images = new ArrayList<String>();
		final List<String> titles = new ArrayList<String>();
		for(ItemBean itemBean : mItemBeanList){
			images.add(itemBean.getImageUrl());
			titles.add(itemBean.getTitle());
		}
		holder.mBannerView.setImages(images);
		holder.mBannerView.setBannerTitles(titles);
		//banner设置方法全部调用完毕时最后调用
		holder.mBannerView.start();

		//设置viewpager的滑动监听
		holder.mBannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				holder.mBannerTitle.setText(titles.get(position));
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		//设置点击事件，下标是从0开始
		holder.mBannerView.setOnBannerListener(new OnBannerListener() {
			@Override
			public void OnBannerClick(int position) {
				if(bannerCallback != null){
					bannerCallback.clickBanner(mItemBeanList.get(position));
				}
			}
		});

		//====设置标题文本为默认第一条数据的标题====
		holder.mBannerTitle.setText(titles.get(0));
	}

	@Override
	public int getItemCount() {
		return mCount;
	}

	static class BannerViewHolder extends RecyclerView.ViewHolder{

		private Banner mBannerView;
		private TextView mBannerTitle;

		public BannerViewHolder(View itemView) {
			super(itemView);
			mBannerView = (Banner) itemView.findViewById(R.id.home_banner);
			mBannerTitle = (TextView) itemView.findViewById(R.id.tv_banner_title);
		}
	}

	/**轮播图点击回调*/
	public interface BannerCallback{
		void clickBanner(ItemBean itemBean);
	}

	private BannerCallback bannerCallback;

	public void setBannerCallback(BannerCallback bannerCallback){
		this.bannerCallback = bannerCallback;
	}
}
