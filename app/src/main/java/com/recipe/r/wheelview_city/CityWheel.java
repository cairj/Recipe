package com.recipe.r.wheelview_city;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.View;

import com.recipe.r.R;
import com.recipe.r.bean.City;
import com.recipe.r.bean.District;
import com.recipe.r.bean.Province;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by hht on 2016/9/18.
 */
public class CityWheel implements OnWheelChangedListener {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName = "";
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName = "";
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";
    private Context context;
    private View view;
    private WheelView mViewProvince, mViewCity, mViewDistrict;
    private int CurrentProviceNumber = 0;//初始化省份索引
    private int CurrentCityNumber = 0;//初始化城市索引

    public CityWheel(View view, Context context, String mCurrentProviceName, String mCurrentCityName) {
        this.view = view;
        this.context = context;
        this.mCurrentProviceName = mCurrentProviceName;
        this.mCurrentCityName = mCurrentCityName;
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpViews() {
        mViewProvince = (WheelView) view.findViewById(R.id.province);
        mViewCity = (WheelView) view.findViewById(R.id.city);
        mViewDistrict = (WheelView) view.findViewById(R.id.district);
//        mBtnConfirm = (Button) view.findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
//        // 添加onclick事件
//        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initCity();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
//        //设置当前省的位置
//        if ("".equals(mCurrentProviceName)) {
//            mViewProvince.setCurrentItem(3);
//            mViewCity.setCurrentItem(0);
//            mViewDistrict.setCurrentItem(0);
//        } else {
//            for (int i = 0; i < mProvinceDatas.length; i++) {
//                if (mProvinceDatas[i].equals(mCurrentProviceName)) {
//                    mViewProvince.setCurrentItem(i);
//                }
//            }
//            mViewCity.setCurrentItem(0);
//            mViewDistrict.setCurrentItem(0);
//        }
        if (CurrentProviceNumber != 0) {
            mViewProvince.setCurrentItem(CurrentProviceNumber);
        } else {
            mViewProvince.setCurrentItem(0);
        }
        //更新城市
        updateCities();
        updateAreas("provice");
        //第一次加载默认省份城市后，清零数据
        if (CurrentCityNumber != 0) {
            CurrentCityNumber = 0;
            CurrentProviceNumber = 0;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas("city");
        } else if (wheel == mViewDistrict) {
            int pCurrent = mViewDistrict.getCurrentItem();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[pCurrent];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas(String str) {
        int pCurrent = 0;
        //防止逆向选择数组越界
        if (str.equals("city")) {
            //从城市更新地区
            pCurrent = mViewCity.getCurrentItem();
        } else if (str.equals("provice")) {
            //从省份更新
            if (CurrentCityNumber != 0) {
                pCurrent = mViewCity.getCurrentItem();
            } else {
                pCurrent = 0;
            }
        }
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(context, areas));
        mViewDistrict.setCurrentItem(0);
        int pCurrent2 = mViewDistrict.getCurrentItem();
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[pCurrent2];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(context, cities));
//        if ("".equals(mCurrentCityName)) {
        if (CurrentCityNumber != 0) {
            mViewCity.setCurrentItem(CurrentCityNumber);
        } else {
            mViewCity.setCurrentItem(0);
        }
//        } else {
//            for (int i = 0; i < cities.length; i++) {
//                if (cities[i].equals(mCurrentCityName)) {
//                    mViewCity.setCurrentItem(i);
//                }
//            }
//        }
        updateAreas("provice");//同步更新区的信息
    }

    public String getCurrentProviceName() {
        return mCurrentProviceName;
    }

    public String getCurrentCityName() {
        return mCurrentCityName;
    }

    public String getCurrentDistrictName() {
        return mCurrentDistrictName;
    }

    public String getCurrentZipCode() {
        return mCurrentZipCode;
    }

    /**
     * 解析省市区的XML数据
     */
    private void initCity() {
        List<Province> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                if (TextUtils.isEmpty(mCurrentProviceName)) {
                    mCurrentProviceName = provinceList.get(0).getName();
                }
                for (int i = 0; i < provinceList.size(); i++) {
                    if (provinceList.get(i).getName().equals(mCurrentProviceName)) {
                        CurrentProviceNumber = i;
                    }
                }
                List<City> cityList = provinceList.get(CurrentProviceNumber).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    if (TextUtils.isEmpty(mCurrentCityName)) {
                        mCurrentCityName = cityList.get(0).getName();
                    }
                    for (int j = 0; j < cityList.size(); j++) {
                        if (cityList.get(j).getName().equals(mCurrentCityName)) {
                            CurrentCityNumber = j;
                        }
                    }
                    List<District> districtList = cityList.get(CurrentCityNumber).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //设置省份下的城市Map集合
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<City> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<District> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    District[] distrinctArray = new District[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        District districtModel = new District(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
