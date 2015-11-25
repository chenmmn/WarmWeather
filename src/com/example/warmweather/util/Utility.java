package com.example.warmweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.warmweather.db.WarmWeatherDB;
import com.example.warmweather.model.City;
import com.example.warmweather.model.County;
import com.example.warmweather.model.Province;

/**
 * @author acer �Դӷ��������ص��ַ�������������
 */
public class Utility {

	/*
	 * �����ʹ�����������ص�ʡ������
	 */
	public  static boolean handleProvinceResponse(
			WarmWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);

					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * �Է��������ص��м����ݽ��д���
	 */

	public static boolean handleCitiesResponse(WarmWeatherDB coolWeatherDB, String response, int provinceId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");

			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}

		
		return false;
		
	}
	
	/*
	 * �����ʹ�����������ص��ؼ�����
	 */
	public static boolean handleCountiesResponse(WarmWeatherDB coolWeatherDB, String response, int cityId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");

			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		
		return false;
		
	}
}
