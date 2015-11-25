package com.example.warmweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.warmweather.R;
import com.example.warmweather.db.WarmWeatherDB;
import com.example.warmweather.model.City;
import com.example.warmweather.model.County;
import com.example.warmweather.model.Province;
import com.example.warmweather.util.MyStringRequest;
import com.example.warmweather.util.Utility;

public class ChooseAreaActivity extends Activity {
	private static final int LEVEL_PROVINCE = 0;

	private static final int LEVEL_CITY = 1;

	private static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;

	private TextView titleText;

	private ListView listView;

	private ArrayAdapter<String> adapter;

	private WarmWeatherDB coolWeatherDB;

	private List<String> dataList = new ArrayList<String>();

	/*
	 * 从数据库中读取的省列表
	 */
	private List<Province> provinceList;

	/*
	 * 市列表
	 */
	private List<City> cityList;

	/*
	 * 县列表
	 */
	private List<County> countyList;

	/*
	 * 选中的省
	 */
	private Province selectedProvince;

	/*
	 * 选中的市
	 */
	private City selectedCity;

	/*
	 * 选中的县
	 */
	private County selectedCounty;

	/*
	 * 当前选中的级别
	 */
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);

		coolWeatherDB = WarmWeatherDB.getInstance(this);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			// 要判断当前的级别是什么来作相应的跳转，因为省市县的信息都是由同一个listview来显示的
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}

		});
		queryProvinces();// 加载省级数据，说白了就是为dataList赋予省级的数据
		
	}

	private void queryProvinces() {

		// 先从本地来加载数据
		provinceList = coolWeatherDB.loadProvince();

		// 首先要判断时加载是否成功，判断的依据就是列表是否为空
		if (provinceList.size() > 0) {
			dataList.clear();// 先清空数据列表中的数据

			// 再将省级数据加载到dataList中
			for (Province p : provinceList) {
				dataList.add(p.getProvinceName());
			}

			// 更新listview的显示
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}

	}

	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());

		if (cityList.size() > 0) {
			dataList.clear();
			for (City c : cityList) {
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}

	}

	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());

		if (countyList.size() > 0) {
			dataList.clear();

			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}

			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/*
	 * 根据传入的代号和类型从服务器上查询省市县的数据
	 */
	private void queryFromServer(String code, final String type) {
		
		
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}

		showProgressDialog();

		RequestQueue mRequest = Volley.newRequestQueue(this);
		MyStringRequest mStringRequest = new MyStringRequest(address,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e("TAG", "response ---------"+ response);
						boolean result = false;

						if (type.equals("province")) {
							Log.e("TAG", "进入到了type.equals(province)");
							result = Utility.handleProvinceResponse(
									coolWeatherDB, response);
						} else if (type.equals("city")) {
							result = Utility.handleCitiesResponse(
									coolWeatherDB, response,
									selectedProvince.getId());
						} else if (type.equals("county")) {
							result = Utility.handleCountiesResponse(
									coolWeatherDB, response,
									selectedCity.getId());
						}
						if (result) {
							
							closeProgressDialog();
							if (type.equals("province")) {
								//再从本地区读取数据
								queryProvinces();
								
							} else if (type.equals("city")) {
								
								queryCities();
								
							} else if (type.equals("county")) {
								
								queryCounties();
								
							}
						}
					}
				}
					, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});

		mRequest.add(mStringRequest);
	}

	protected void closeProgressDialog() {
		if (progressDialog != null){
			progressDialog.dismiss();
		}
	}

	/*
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载中。。。。");
			progressDialog.setCanceledOnTouchOutside(false);
			
		}
		progressDialog.show();
	}
	
	@Override
	public void onBackPressed() {
		
		if (currentLevel == LEVEL_COUNTY){
			queryCities();
		} else if (currentLevel == LEVEL_CITY){
			queryProvinces();
		} else{
			finish();
		}
	}
	
/*	private void query(){
		String url = "http://www.weather.com.cn/data/list3/city.xml";
		
		RequestQueue mqueue = Volley.newRequestQueue(ChooseAreaActivity.this);
		MyStringRequest mrequest = new MyStringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				Log.e("TAG", "TEST +++++"+ arg0);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mqueue.add(mrequest);
	}*/
}
