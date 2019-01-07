package com.zt.capacity.jinan_zwt.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.LocationGatherMapActivity;
import com.zt.capacity.jinan_zwt.bean.ConsappVo;

import java.util.List;

/**
 * 位置采集list适配
 * Created by Administrator on 2018-05-08.
 */
public class JNGatherListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ConsappVo> lists;

    public JNGatherListAdapter(Context context, List<ConsappVo> list) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        lists = list;
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
        final ConsappVo consappVo = lists.get(i);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.jn_zwt_item_gather_list_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.gather_item_cosapp_name.setText(consappVo.getProName());//工程名称
        holder.reference_number.setText(consappVo.getLicNumber());//备案编号
        holder.construction_monad.setText(consappVo.getBuildName());//施工单位
        holder.gather_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索
                ActivityJumpUtil.jumpActivityByObject((Activity) mContext, LocationGatherMapActivity.class, consappVo, "consappVo");
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView gather_item_cosapp_name;//工程名称
        private TextView reference_number;//备案编号
        private TextView construction_monad;//建设单位
        private ImageView gather_search_button;//搜索


        public ViewHolder(View view) {
            gather_item_cosapp_name = view.findViewById(R.id.gather_item_cosapp_name);
            reference_number = view.findViewById(R.id.reference_number);
            construction_monad = view.findViewById(R.id.construction_monad);
            gather_search_button = view.findViewById(R.id.gather_search_button);
        }
    }
}
