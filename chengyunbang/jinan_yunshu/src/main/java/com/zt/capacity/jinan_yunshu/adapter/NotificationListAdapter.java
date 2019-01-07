package com.zt.capacity.jinan_yunshu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zt.capacity.common.util.DateUtil;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.bean.PushRecord;

import java.util.List;

/**
 * Created by Administrator on 2018-05-08.
 */

public class NotificationListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    private List<PushRecord> pushRecords;

    public NotificationListAdapter(Context context, List<PushRecord> pushRecords) {
        mInflater = LayoutInflater.from(context);
        this.pushRecords = pushRecords;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pushRecords == null ? 0 : pushRecords.size();
    }

    @Override
    public Object getItem(int i) {
        return pushRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.jn_yunshu_item_notification_list, null);
            holder.fz(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PushRecord pushRecord = pushRecords.get(i);
        holder.title.setText(pushRecord.getPushRecordTitle() != null ? pushRecord.getPushRecordTitle() : "");
        holder.content.setText(pushRecord.getPushRecordContent() != null ? pushRecord.getPushRecordContent() : "");
        holder.time.setText(pushRecord.getPushRecordTime() != null ? DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM,pushRecord.getPushRecordTime())+"" : "");
        if (pushRecord.getPushState() != null && pushRecord.getPushState() == 1) {
            holder.state.setText("已读");
            holder.state.setTextColor(context.getResources().getColor(R.color.color_9b9b9b));
        } else {
            holder.state.setText("未读");
            holder.state.setTextColor(context.getResources().getColor(R.color.red));
        }
        return convertView;
    }

    class ViewHolder {
        //标题，内容，时间，状态
        TextView title, content, time, state;

        public void fz(View view) {
            title = view.findViewById(R.id.notification_item_title);
            content = view.findViewById(R.id.notification_item_content);
            time = view.findViewById(R.id.notification_item_time);
            state = view.findViewById(R.id.notification_item_state);
        }


    }
}
