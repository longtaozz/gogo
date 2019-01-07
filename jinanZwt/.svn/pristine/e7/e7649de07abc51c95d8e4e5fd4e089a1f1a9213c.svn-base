package com.zt.capacity.jinan_zwt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.Car;

import java.util.List;

/**
 * 指令下发车辆信息listview适配器
 * Created by Administrator on 2018-05-08.
 */
public class JNInstructionCarAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<Car> lists;
    private Integer state;//是否单个车,1是，2不是

    public JNInstructionCarAdapter(Context context, List<Car> list, Integer state) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        lists = list;
        this.state = state;
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
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
        Car car = lists.get(i);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.jn_zwt_item_instruction_list_car, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (state == 1) {
            holder.instructions_list_box.setVisibility(View.INVISIBLE);
        }
        holder.instructions_list_carnumber.setText(car.getNumberPlate());//车牌
        holder.instructions_list_sim.setText(car.getSimNumber());//SIM卡号
        holder.instructions_list_company.setText(car.getEnterpriseAbbreviation());//公司名称
        return convertView;
    }

    class ViewHolder {
        public ImageView instructions_list_box;//复选框
        public TextView instructions_list_carnumber, instructions_list_sim, instructions_list_company;//车牌号、SIM卡号、公司名称

        public ViewHolder(View view) {
            instructions_list_box = view.findViewById(R.id.instructions_list_box);//复选框
            instructions_list_carnumber = view.findViewById(R.id.instructions_list_carnumber);//车牌号
            instructions_list_sim = view.findViewById(R.id.instructions_list_sim);//SIM卡号
            instructions_list_company = view.findViewById(R.id.instructions_list_company);//公司名称
        }
    }
}
