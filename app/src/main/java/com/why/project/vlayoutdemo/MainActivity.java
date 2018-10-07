package com.why.project.vlayoutdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.why.project.vlayoutdemo.adapter.BannerLayoutAdapter;
import com.why.project.vlayoutdemo.adapter.GridLayoutAdapter;
import com.why.project.vlayoutdemo.adapter.HorizontalListLayoutAdapter;
import com.why.project.vlayoutdemo.adapter.ListLayoutAdapter;
import com.why.project.vlayoutdemo.adapter.StickyTitleAdapter;
import com.why.project.vlayoutdemo.bean.ItemBean;
import com.why.project.vlayoutdemo.bean.ModelBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	private List<ModelBean> mModelBeanList;//列表数据集合

	private RecyclerView mVLayoutRV;

	private VirtualLayoutManager layoutManager;
	private DelegateAdapter delegateAdapter;
	private List<DelegateAdapter.Adapter> adapters;

	private RecyclerView.RecycledViewPool viewPool;//设置复用池的大小
	private int itemType;//一个Adapter对应一个类型，这里通过自增加1实现唯一性

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
	}

	private void initViews() {
		mVLayoutRV = findViewById(R.id.vlayout_rv);
	}

	private void initDatas() {
		//初始化列表数据集合
		mModelBeanList = new ArrayList<ModelBean>();
		getTestDatas();
		Log.e(TAG,"mModelBeanList.size()=" + mModelBeanList.size());


		//初始化LayoutManager
		layoutManager = new VirtualLayoutManager(this);
		layoutManager.setRecycleOffset(300);
		mVLayoutRV.setLayoutManager(layoutManager);

		//设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
		//针对type=0的item设置了复用池的大小，如果你的页面有多种type，需要为每一种类型的分别调整复用池大小参数。
		viewPool = new RecyclerView.RecycledViewPool();
		mVLayoutRV.setRecycledViewPool(viewPool);

		//加载数据，通过创建adapter集合实现布局
		delegateAdapter = new DelegateAdapter(layoutManager, false);//必须使用false，实现每一个分组的类型不同
		mVLayoutRV.setAdapter(delegateAdapter);

		setVLayoutAdapter();
	}

	/**设置适配器*/
	private void setVLayoutAdapter() {

		itemType = 0;//自增加1
		if (adapters != null) {
			adapters.clear();
		} else {
			adapters = new LinkedList<>();
		}

		//根据类型不同，采用不同的adapter并添加到集合中
		for(int i=0; i<mModelBeanList.size(); i++){
			String modelName = mModelBeanList.get(i).getModelName();
			String type = mModelBeanList.get(i).getType();
			ArrayList<ItemBean> itemBeanArrayList = (ArrayList<ItemBean>) mModelBeanList.get(i).getItemDataList();

			boolean showModelName = true;//控制是否显示model，也就是吸顶布局
			if(showModelName){
				//stikcy布局， 可以配置吸顶或者吸底
				//设置各个区域的复用池的大小，因为只有一个元素，所以复用池大小就设置为1
				viewPool.setMaxRecycledViews(itemType++, 1);
				StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
				StickyTitleAdapter stickyTitleAdapter = new StickyTitleAdapter(this,stickyLayoutHelper,modelName);
				adapters.add(stickyTitleAdapter);
			}

			//========用来判断分组，展现不同的样式========
			//每一个item对应一种样式
			switch(type) {
				case "banner":
					//通栏布局——轮播图
					//设置各个区域的复用池的大小，设置子集合的总个数为复用池大小
					viewPool.setMaxRecycledViews(itemType++, itemBeanArrayList.size());
					SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();//通栏布局，只会显示一个组件View
					singleLayoutHelper.setMargin(0, 0, 0, dip2px(this, 10));//设置外边距，实现分割效果
					BannerLayoutAdapter bannerLayoutAdapter = new BannerLayoutAdapter(this, singleLayoutHelper, itemBeanArrayList);
					bannerLayoutAdapter.setBannerCallback(new BannerLayoutAdapter.BannerCallback() {
						@Override
						public void clickBanner(ItemBean itemBean) {
							Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
						}
					});//设置自定义回调，用于点击事件监听
					adapters.add(bannerLayoutAdapter);
					break;
				case "hori":
					//通栏布局——横向列表
					//设置各个区域的复用池的大小，设置子集合的总个数为复用池大小
					viewPool.setMaxRecycledViews(itemType++, itemBeanArrayList.size());
					SingleLayoutHelper horizontalListLayoutHelper = new SingleLayoutHelper();//不使用LinearLayoutHelper
					horizontalListLayoutHelper.setMargin(0,0,0,dip2px(this, 10));//设置外边距，实现分割效果
					HorizontalListLayoutAdapter horizontalListLayoutAdapter = new HorizontalListLayoutAdapter(this,horizontalListLayoutHelper,itemBeanArrayList);
					horizontalListLayoutAdapter.setHorizontalListCallback(new HorizontalListLayoutAdapter.HorizontalListCallback() {
						@Override
						public void clickHorizontalItem(ItemBean itemBean) {
							Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
						}
					});//设置自定义回调，用于点击事件监听
					adapters.add(horizontalListLayoutAdapter);
					break;
				case "grid":
					//九宫格布局
					//设置各个区域的复用池的大小，设置子集合的总个数为复用池大小
					viewPool.setMaxRecycledViews(itemType++, itemBeanArrayList.size());
					GridLayoutHelper gridlayoutHelper = new GridLayoutHelper(2);//Grid布局， 支持横向的colspan
					gridlayoutHelper.setAutoExpand(false);//解决单数的时候，最后一张居中显示的问题
					gridlayoutHelper.setMargin(0, 0, 0, dip2px(this, 10));//设置外边距，实现分割效果
					GridLayoutAdapter gridLayoutAdapter = new GridLayoutAdapter(this, gridlayoutHelper, itemBeanArrayList, itemBeanArrayList.size());
					//设置自定义回调，用于点击事件监听
					gridLayoutAdapter.setGridCallback(new GridLayoutAdapter.GridCallback() {
						@Override
						public void clickGrid(ItemBean itemBean) {
							Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
						}
					});
					adapters.add(gridLayoutAdapter);
					break;
				case "list":
					//列表布局（默认布局）
					//设置各个区域的复用池的大小，设置子集合的总个数为复用池大小
					viewPool.setMaxRecycledViews(itemType++, itemBeanArrayList.size());
					LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();//线性布局
					linearLayoutHelper.setMargin(0, 0, 0, dip2px(this, 10));//设置外边距，实现分割效果
					ListLayoutAdapter listLayoutAdapter = new ListLayoutAdapter(this, linearLayoutHelper, itemBeanArrayList, itemBeanArrayList.size());
					//设置自定义回调，用于点击事件监听
					listLayoutAdapter.setListCallback(new ListLayoutAdapter.ListCallback() {
						@Override
						public void clickList(ItemBean itemBean) {
							Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
						}
					});
					adapters.add(listLayoutAdapter);
					break;
				default:
			}

		}

		delegateAdapter.setAdapters(adapters);
	}

	/**
	 * dp转px
	 * 16dp - 48px
	 * 17dp - 51px*/
	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)((dpValue * scale) + 0.5f);
	}


	/*=======================================获取测试数据==============================================*/
	private void getTestDatas(){
		String listdata = getStringFromAssert(MainActivity.this,"vlayout.txt");
		try {
			JSONObject listObj = new JSONObject(listdata);
			JSONArray listArray = listObj.getJSONArray("data");
			for(int i=0; i< listArray.length(); i++){
				JSONObject itemObj = listArray.getJSONObject(i);

				ModelBean modelBean = new ModelBean();
				modelBean.setModelName(itemObj.getString("modelname"));
				modelBean.setType(itemObj.getString("type"));

				JSONArray childArray = itemObj.getJSONArray("data");
				List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
				for(int j=0; j<childArray.length(); j++){
					JSONObject childObj = childArray.getJSONObject(j);

					ItemBean itemBean = new ItemBean();
					itemBean.setId(childObj.getString("id"));
					itemBean.setImageUrl(childObj.getString("image"));
					itemBean.setOrder(childObj.getString("order"));
					itemBean.setTitle(childObj.getString("title"));
					itemBean.setUrlPath(childObj.getString("url"));

					itemBeanList.add(itemBean);
				}

				modelBean.setItemDataList(itemBeanList);

				mModelBeanList.add(modelBean);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 访问assets目录下的资源文件，获取文件中的字符串
	 * @param assetsFilePath - 文件的相对路径，例如："listitemdata.txt或者"/why/listdata.txt"
	 * @return 内容字符串
	 * */
	public static String getStringFromAssert(Context mContext, String assetsFilePath) {

		String content = ""; // 结果字符串
		try {
			InputStream is = mContext.getResources().getAssets().open(assetsFilePath);// 打开文件
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream(); // 实现了一个输出流
			while ((ch = is.read()) != -1) {
				out.write(ch); // 将指定的字节写入此 byte 数组输出流
			}
			byte[] buff = out.toByteArray();// 以 byte 数组的形式返回此输出流的当前内容
			out.close(); // 关闭流
			is.close(); // 关闭流
			content = new String(buff, "UTF-8"); // 设置字符串编码
		} catch (Exception e) {
			Toast.makeText(mContext, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
					.show();
		}
		return content;
	}

}
