package com.yvision.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yvision.R;
import com.yvision.common.ImageLoadingConfig;
import com.yvision.model.OldEmployeeModel;
import com.yvision.widget.CircleImageView;


/**
 * 老员工姓名列表适配
 *
 * @author
 */

public class RegisterListAdapter extends BaseListAdapter {
    // 图片缓存
    private Context context;
    private DisplayImageOptions imgOption;
    private ImageLoader imgLoader;

    public class WidgetHolder {
        public TextView tv_name;
        public TextView tv_workid;
        public CircleImageView imageView;
    }

    public RegisterListAdapter(Context context) {
        super(context);
        this.context = context;
        // 图片缓存实例化
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOption = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.default_photo);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_registermain_list, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        holder.tv_workid = (TextView) view.findViewById(R.id.tv_workid);
        holder.imageView = (CircleImageView) view.findViewById(R.id.imageView);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例

        OldEmployeeModel model = (OldEmployeeModel) entityList.get(position);
        //获取一条信息
        holder.tv_name.setText(model.getEmployeeName());
        holder.tv_workid.setText(model.getWrokId());

        imgLoader.init(ImageLoaderConfiguration.createDefault(context));//异常提示没注册
        imgLoader.displayImage(model.getPic(), holder.imageView, imgOption);
    }

    public void destroy() {
        if (imgLoader!=null) {
            imgLoader.destroy();
        }
    }
}
