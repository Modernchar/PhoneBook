package cn.codbking.widget.listener;

import cn.codbking.widget.bean.DateBean;
import cn.codbking.widget.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
