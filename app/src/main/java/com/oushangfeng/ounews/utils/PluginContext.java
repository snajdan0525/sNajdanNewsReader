package com.snajdan.android.demo.dynamic.hostplugin;

import dalvik.system.DexClassLoader;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PluginContext extends ContextWrapper {

	private static final String TAG = "PluginContext";

	private DexClassLoader mClassLoader;
	private Resources mResources;
	private LayoutInflater mInflater;

	PluginContext(Context context, String pluginPath,
			String optimizedDirectory, String libraryPath) {
		super(context.getApplicationContext());

		Resources resc = context.getResources();
		// 隐藏API是这样的
		// AssetManager assets = new AssetManager();
		AssetManager assets=null;
		try{
			assets = AssetManager.class.newInstance();
			assets.getClass().getMethod("addAssetPath", String.class)
					.invoke(assets, pluginPath);
		}
		catch(Exception e){
			
		}
		mClassLoader = new DexClassLoader(pluginPath, optimizedDirectory,
				libraryPath, context.getClassLoader());
		mResources = new Resources(assets, resc.getDisplayMetrics(),
				resc.getConfiguration());
		// 隐藏API是这样的
		// mInflater = PolicyManager.makeNewLayoutInflater(this);
		mInflater = new LayoutInflater(context) {

			@Override
			public LayoutInflater cloneInContext(Context newContext) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View inflate(int resource, ViewGroup root,
					boolean attachToRoot) {
				// final Resources res = getContext().getResources();
				final Resources res = mResources;

				final XmlResourceParser parser = res.getLayout(resource);
				try {
					return inflate(parser, root, attachToRoot);
				} finally {
					parser.close();
				}
			}
		};
	}

	@Override
	public ClassLoader getClassLoader() {
		return mClassLoader;
	}

	@Override
	public AssetManager getAssets() {
		return mResources.getAssets();
	}

	@Override
	public Resources getResources() {
		return mResources;
	}

	@Override
	public Object getSystemService(String name) {
		if (name == Context.LAYOUT_INFLATER_SERVICE)
			return mInflater;
		return super.getSystemService(name);
	}

	private Theme mTheme;

	@Override
	public Resources.Theme getTheme() {
		if (mTheme == null) {
//			int resid = Resources.selectDefaultTheme(0, getBaseContext()
//					.getApplicationInfo().targetSdkVersion);
//			mTheme = mResources.newTheme();
//			mTheme.applyStyle(resid, true);
		}
		return mTheme;
	}

}