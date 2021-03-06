package com.example.warmweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author acer
 *         ΪʲôҪʹ�����ݿ⣺��Ϊ��ŵ�����ͦ��ģ�ʡ�ݵ���Ϣ�����е���Ϣ���ص���Ϣ���㲻����˵��sharedpreferenceһ���������
 */
public class WarmWeatherOpenHelper extends SQLiteOpenHelper {

	/*
	 * Province���Ľ������
	 */
	private static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement," + " province_name text,"
			+ " province_code text)";

	/*
	 * ����City���Ľ������
	 */
	private static final String CREATE_CITY = "create table City(id integer primary key autoincrement,"
			+ "city_name text," + " city_code text," + " province_id integer)";

	/*
	 * ����county���Ľ������
	 */
	private static final String CREATE_COUNTY = "create table County(id integer primary key autoincrement, "
			+ "county_name text, " + "county_code text," + " city_id integer)";

	public WarmWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);// ����ʡ��
		db.execSQL(CREATE_CITY);// �����У���
		db.execSQL(CREATE_COUNTY);// �����ر�
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
