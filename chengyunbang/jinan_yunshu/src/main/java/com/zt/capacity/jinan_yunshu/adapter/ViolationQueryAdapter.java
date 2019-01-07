package com.zt.capacity.jinan_yunshu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.bean.ViolationsBean;

import java.util.List;

/**
 * 违章信息适配器
 * Created by Administrator on 2018-05-08.
 */

public class ViolationQueryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ViolationsBean> lists;
    public ViolationQueryAdapter(Context context, List<ViolationsBean> list){
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
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.jn_yunshu_item_violation, null);
            holder.date = convertView.findViewById(R.id.date);
            holder.area = convertView.findViewById(R.id.area);
            holder.act = convertView.findViewById(R.id.act);
            holder.fen =convertView.findViewById(R.id.fen);
            holder.money = convertView.findViewById(R.id.money);
            holder.zhi = convertView.findViewById(R.id.zhi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.date.setText(lists.get(i).getDate());
        holder.area.setText(lists.get(i).getArea());
        holder.act.setText(lists.get(i).getAct());
        holder.fen.setText(lists.get(i).getFen()+"分");
        holder.money.setText(lists.get(i).getMoney()+"元");
//        holder.zhi.setText();

        return convertView;
    }

    class ViewHolder {
       private TextView date;
       private TextView area;////违章地点
        private TextView act;//违章行为
        private TextView fen;//扣分
        private TextView money;//罚款
        private TextView zhi;

    }
}
