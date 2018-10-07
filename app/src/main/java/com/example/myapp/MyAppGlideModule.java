package com.example.myapp;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by HaiyuKing
 * Used 自定义GlideModule子类，设置内存缓存、Bitmap 池、磁盘缓存、、默认请求选项、解码格式等等
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html#avoid-appglidemodule-in-libraries
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

	int diskCacheSize = 1024 * 1024 * 200; // 200mb
	int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存


	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		//设置解码格式
        /*在 Glide v3， 默认的 DecodeFormat 是 DecodeFormat.PREFER_RGB_565，它将使用 [Bitmap.Config.RGB_565]，除非图片包含或可能包含透明像素。对于给定的图片尺寸，RGB_565 只使用 [Bitmap.Config.ARGB_8888] 一半的内存，但对于特定的图片有明显的画质问题，包括条纹(banding)和着色(tinting)。
        为了避免RGB_565的画质问题，Glide 现在默认使用 ARGB_8888。结果是，图片质量变高了，但内存使用也增加了。*/
		builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));

		//默认请求选项【不太习惯，还是每个请求重复使用吧】
        /*builder.setDefaultRequestOptions(
                new RequestOptions()
                        //设置等待时的图片
                        .placeholder(R.drawable.img_loading)
                        //设置加载失败后的图片显示
                        .error(R.drawable.img_error)
                        .centerCrop()
                        //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                        .skipMemoryCache(false)
                        //缓存策略,貌似只有这一个设置
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //设置图片加载的优先级
                        .priority(Priority.HIGH));*/

		// 设置内存缓存
		MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
				.setMemoryCacheScreens(2)
				.build();
		builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
		//builder.setMemoryCache(new LruResourceCache(memorySize));//自定义大小

		//设置Bitmap 池
		MemorySizeCalculator calculator2 = new MemorySizeCalculator.Builder(context)
				.setBitmapPoolScreens(3)
				.build();
		builder.setBitmapPool(new LruBitmapPool(calculator2.getBitmapPoolSize()));
		//builder.setBitmapPool(new LruBitmapPool(memorySize));//自定义大小

		//设置磁盘缓存【暂时不做处理】
		//lide 使用 DiskLruCacheWrapper 作为默认的 磁盘缓存 。 DiskLruCacheWrapper 是一个使用 LRU 算法的固定大小的磁盘缓存。默认磁盘大小为 250 MB ，位置是在应用的 缓存文件夹 中的一个 特定目录 。
        /*builder.setDiskCache(new ExternalDiskCacheFactory(context));
        builder.setDiskCache(new InternalDiskCacheFactory(context, diskCacheSize));*/
	}
}