package com.tiankonguse.gameplatform;

import java.util.ArrayList;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.tab.BlankGame;
import com.tiankonguse.gameplatform.tab.ClassGame;
import com.tiankonguse.gameplatform.tab.MangerGame;
import com.tiankonguse.gameplatform.tab.RankGame;

@SuppressWarnings("deprecation")
public class MainDownloadUI extends TabActivity {

	private Context context = null;
	private LocalActivityManager manager;
	private TabHost tabHost;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.downloadui);

		context = this;

		manager = new LocalActivityManager(this, true);

		manager.dispatchCreate(savedInstanceState);

		loadTabHost();
		
		loadViewPager();
		
		tabHost.setCurrentTab(MyGameDB.TAB_RANK);

	}

	private void loadTabHost() {
		
		tabHost = getTabHost();
		
		MyAddTab(BlankGame.class, R.string.main_tabs_rank);
		
		MyAddTab(BlankGame.class,R.string.main_tabs_class);
		
		MyAddTab(BlankGame.class, R.string.main_tabs_manger);
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				if(tabId.equals(getString(R.string.main_tabs_rank))){
					viewPager.setCurrentItem(MyGameDB.TAB_RANK);
				}else if(tabId.equals(getString(R.string.main_tabs_class))){
					viewPager.setCurrentItem(MyGameDB.TAB_CLASS);
				}else if(tabId.equals(getString(R.string.main_tabs_manger))){
					viewPager.setCurrentItem(MyGameDB.TAB_MANGER);
				} 
			}
			
		});
		
	}

	private void MyAddTab(Class<?>cls , int tabid){

		TabSpec spec = tabHost
					.newTabSpec(getString(tabid))
					.setIndicator(getString(tabid))
					.setContent(new Intent(context,cls));
		
		tabHost.addTab(spec);	
		
	}	
	
	private void loadViewPager(){
		viewPager = (ViewPager)findViewById(R.id.main_viewpager);	
		
		final ArrayList<View>lists = new ArrayList<View>();
		
		lists.add(MygetView(R.string.main_tabs_rank, RankGame.class));
		lists.add(MygetView(R.string.main_tabs_class, ClassGame.class));
		lists.add(MygetView(R.string.main_tabs_manger, MangerGame.class));
		
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return lists.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public void destroyItem(View container, int position, Object object) {
				viewPager.removeView(lists.get(position));
			}
			

			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(lists.get(position));
				return lists.get(position);
			}

			
			@Override
			public void finishUpdate(View container) {

			}

			
			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {

			}

			
			@Override
			public Parcelable saveState() {
				return null;
			}

			
			@Override
			public void startUpdate(View container) {

			}

			
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrollStateChanged(int arg0) {
				
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			public void onPageSelected(int arg0) {
				tabHost.setCurrentTab(arg0);
				MangerGame.updateMyList();
			}
			
			
		});
		
	}	
	
	private Intent  MygetIntent(Class<?> cls){
		return new Intent(context,cls);
	}
	
	
	private View MygetView(int id,Class<?>cls){
		return manager.startActivity(getString(id), MygetIntent(cls)).getDecorView();
	}
	
}
