package com.zt.capacity.jinan_zwt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.CarHistoryBean;

import java.util.List;

/**
 * 违章信息适配器
 * Created by Administrator on 2018-05-08.
 */

public class DoubtfulAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<CarHistoryBean> lists;
    public DoubtfulAdapter(Context context, List<CarHistoryBean> list){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        lists = list;

    }
    @Override
    public int getCount() {
        return lists==null ? 0:lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.jn_zwt_item_gridview_doubtful, null);
            holder = new ViewHolder();
            holder.text_doubtful=convertView.findViewById(R.id.text_doubtful);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_doubtful.setText(lists.get(i).getCarNumber());
//        holder.zhi.setText();

        return convertView;
    }

    class ViewHolder {
       public TextView text_doubtful;
    }
}
