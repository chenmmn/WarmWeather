package com.example.warmweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.warmweather.model.City;
import com.example.warmweather.model.County;
import com.example.warmweather.model.Province;

/**
 * @author acer ����������ݿⳣ�õ�һЩ������װ���� ���ǵĳ���ֻ��Ҫ����һ�����ݿ⣬�������ǲ��õ���
 */
public class WarmWeatherDB {

	/*
	 * ���ݿ���
	 */
	public final String DB_NAME = "cool_weather";

	/*
	 * ���ݿ�汾
	 */
	public final int VERSION = 1;

	private static WarmWeatherDB warmWeatherDB;

	private static SQLiteDatabase db;

	/*
	 * �����캯��˽�л�
	 */
	private WarmWeatherDB(Context context) {
		WarmWeatherOpenHelper dbHelper = new WarmWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getReadableDatabase();
	}

	/*
	 * ��ȡCoolWeatherDBʵ��
	 */
	public synchronized static WarmWeatherDB getInstance(Context context) {
		warmWeatherDB = new WarmWeatherDB(context);
		return warmWeatherDB;
	}

	/*
	 * ��Provinceʵ���洢�����ݿ�(һ��ʡ������)
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/*
	 * �����ݿ�������ʡ����Ϣ��ȡ��������List���洢ʡ����Ϣ
	 */
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();

		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Province province = new Province();
			province.setProvinceCode(cursor.getString(cursor
					.getColumnIndex("province_code")));
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(cursor.getString(cursor
					.getColumnIndex("province_name")));
			list.add(province);
		}

		return list;
	}

	/*
	 * ��cityʵ�������ݴ洢�����ݿ���
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/*
	 * �����ݿ��ȡĳʡ�����г�����Ϣ
	 */
	public List<City> loadProvinces(int provinceId) {
		List<City> list = new ArrayList<City>();

		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);

		while (cursor.moveToNext()) {
			City city = new City();
			city.setCityCode(cursor.getString(cursor
					.getColumnIndex("city_code")));
			city.setCityName(cursor.getString(cursor
					.getColumnIndex("city_name")));
			city.setProvinceId(provinceId);
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			list.add(city);
		}

		return list;
	}

	/*
	 * ��countyʵ�������ݴ洢�����ݿ���
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	/*
	 * �����ݿ��ж�ȡĳ�����������ص���Ϣ
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();

		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		while (cursor.moveToNext()) {
			County county = new County();
			county.setCityId(cityId);
			county.setCountyCode(cursor.getString(cursor
					.getColumnIndex("county_name")));
			county.setCountyName(cursor.getString(cursor
					.getColumnIndex("county_name")));
			county.setId(cursor.getInt(cursor.getColumnIndex("id")));

			list.add(county);
		}

		return list;
	}

}
