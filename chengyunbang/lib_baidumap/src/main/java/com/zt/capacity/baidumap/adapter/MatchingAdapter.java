package com.zt.capacity.baidumap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zt.capacity.baidumap.R;
import com.zt.capacity.common.bean.Car;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义提示列表样式
 * Created by Administrator on 2018/5/3.
 */

public class MatchingAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Car> filterCars;
    private List<Car> cars;
    private MatchingFilter matchingFilter;

    public MatchingAdapter(Context context, List<Car> cars) {
        this.context = context;
        this.cars = cars;
    }

    @Override
    public int getCount() {
        return filterCars == null? 0 :filterCars.size();
    }

    @Override
    public Object getItem(int i) {
        return filterCars.get(i).getNumberPlate();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AtchingItme atchingItme;
        if (view == null) {
            atchingItme = new AtchingItme();
            view = View.inflate(context, R.layout.jn_zwt_itme_matching, null);
            atchingItme.atchingCarNumber = view.findViewById(R.id.atchingCarNumber);
            view.setTag(atchingItme);

        } else {
            atchingItme = (AtchingItme) view.getTag();
        }
        Car car = filterCars.get(i);
        atchingItme.atchingCarNumber.setText(car.getNumberPlate());
        return view;
    }

    class AtchingItme {
        TextView atchingCarNumber;

    }

    @Override
    public Filter getFilter() {
        if (matchingFilter == null) {
            matchingFilter = new MatchingFilter();
        }
        return matchingFilter;
    }

    private class MatchingFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            //如果没有过滤条件则不过滤
            Log.d("adpterCCCCCC", (constraint == null)+"++"+(constraint.length() == 0));
            if (filterCars == null) {
                filterCars = new ArrayList<Car>(cars);
            }

            if (constraint == null || constraint.length() == 0) {
                results.values = filterCars;
                results.count = filterCars.size();
            } else {
                List<Car> retList = new ArrayList<Car>();
                //过滤条件
                String str = constraint.toString().toLowerCase().toUpperCase();
                //循环变量数据源，如果有属性满足过滤条件，则添加到result中

                for (Car car : cars) {
                    if (car.getNumberPlate().contains(str)) {
                        retList.add(car);
                    }
                }
                results.values = retList;
                results.count = retList.size();
            }
            return results;
        }

        //在这里返回过滤结果
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
//          notifyDataSetInvalidated()，会重绘控件（还原到初始状态）
//          notifyDataSetChanged()，重绘当前可见区域
            filterCars = (List<Car>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    }
}