package com.zt.capacity.jinan_yunshu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zt.capacity.common.base.ImageBigActivity;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.bean.DataActionBean;

import java.util.List;

/**
 * 指令下发记录信息listview适配器
 *
 * @author lt
 * @time 2018/12/19 13:02
 **/
public class InstructionRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataActionBean> lists;

    public InstructionRecordAdapter(Context context, List<DataActionBean> list) {
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
        final DataActionBean dataAction = lists.get(i);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.jn_yunshu_item_instruction_list_record, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.record_list_carNumber.setText(dataAction.getCarPass()== null ? "" : dataAction.getCarPass());//车牌
        holder.record_list_time.setText(dataAction.getDealTime() == null ? "" : dataAction.getDealTime());

        //下发类型判断
        Integer type = dataAction.getActionType();//类型
        Integer data = dataAction.getActionValue();//值
        String result = "";
        if (type == 1) {
            if (data == 0) {
                result = "解除锁车";
            } else if (data == 1) {
                result = "锁车";
            }
        } else if (type == 2) {
            if (data == 0) {
                result = "解除限速";
            } else {
                result = "限速&nbsp;" + data + "km/h";
            }
        } else if (type == 3) {
            if (data == 0) {
                result = "解除限举";
            } else if (data == 1) {
                result = "限举";
            }
        } else if (type == 4) {
            if (data == 0) {
                result = "抓拍";
            }
        } else if (type == 5) {
            if (data == 0) {
                result = "下发密码";
            }
        } else if (type == 6) {
            if (data == 1) {
                result = "指纹解锁";
            } else if (data == 2) {
                result = "解除管控";
            } else if (data == 3) {
                result = "指纹验证";
            } else if (data == 4) {
                result = "开启管控";
            }
        }
        holder.record_list_control.setText(result);//下发类型
        //判断处理结果
        String dealResults = "";
        if (dataAction.getDealResult() == 0) {
            dealResults = "未处理";
            holder.record_list_result.setTextColor(mContext.getResources().getColor(R.color.color_4a4a4a));
        }
        if (dataAction.getDealResult() == 1) {
            dealResults = "处理成功";
            if (type == 4) {
                if (data == 0) {
                    dealResults = "查看";
                    holder.record_list_result.setTextColor(mContext.getResources().getColor(R.color.entirety_theme));
                    holder.record_list_result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //查看抓拍图片
                            ActivityJumpUtil.jumpActivityByString((Activity) mContext, ImageBigActivity.class, dataAction.getImgPath(), "imgUrl");
                        }
                    });
                }
            } else {
                holder.record_list_result.setTextColor(mContext.getResources().getColor(R.color.color_4a4a4a));
            }
        }
        if (dataAction.getDealResult() == -1) {
            dealResults = "处理失败";
            holder.record_list_result.setTextColor(mContext.getResources().getColor(R.color.color_4a4a4a));
            holder.record_list_result.setClickable(false);
        }
        holder.record_list_result.setText(dealResults);//处理结果

        return convertView;
    }

    class ViewHolder {
        public TextView record_list_carNumber, record_list_control, record_list_time, record_list_result;//车牌号、操作类型、处理结果

        public ViewHolder(View view) {
            record_list_carNumber = view.findViewById(R.id.record_list_carNumber);//车牌号
            record_list_control = view.findViewById(R.id.record_list_control);//操作类型
            record_list_time = view.findViewById(R.id.record_list_time);//时间
            record_list_result = view.findViewById(R.id.record_list_result);//处理结果
        }
    }
}
