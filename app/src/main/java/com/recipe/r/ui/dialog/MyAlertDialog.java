package com.recipe.r.ui.dialog;

import android.content.Context;
import android.net.ParseException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.recipe.r.R;
import com.recipe.r.entity.ExpressDelivery;
import com.recipe.r.ui.adapter.adress.ExpressDeliveryRvAdapter;
import com.recipe.r.utils.OnRecycleItemListener;
import com.recipe.r.wheelview_city.CityWheel;
import com.recipe.r.wheelview_time.JudgeDate;
import com.recipe.r.wheelview_time.WheelMain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 2017
 * 06
 * 2017/6/13
 * wangxiaoer
 * 功能描述：自定义的对话框
 **/
public class MyAlertDialog extends android.app.AlertDialog implements View.OnClickListener {
    private int screenWidth, screenHeight;//屏幕宽高
    private Window normaldialogWindow;//屏幕中央的dialog
    private Window bottomDialogWindow;//屏幕下方的dialog
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout alertdialog_ll;
    private View view;
    private TextView tv_tip, tv_yes, tv_no, content_tv;
    private TextView tv1;
    private EditText editText1, editText2, editText3;
    private View.OnClickListener yes_listener, no_listener, city_yes_listener, time_yes_listener, close_listener, edit_yes_listener, noIv_listener;
    private View.OnClickListener tv1_listener, tv2_listener, tv3_listener, checkListener3, checkListener4;
    private String result;//结果
    private String value1, value2, value3 = "";
    private WheelMain wheelMain;//时间三级联动
    private CityWheel cityWheel;//城市三级联动
    private ImageView prize_iv,prize_yes;//红包图片
    private String selectExpress = "";//选中的快递配送
    public MyAlertDialog(Context context) {
        super(context);
        initView(context);
    }

    public MyAlertDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    public MyAlertDialog oneYes(String title, String content, String yes) {
        view = inflater.inflate(R.layout.one_yes_alertdialog, null);
        tv_tip = (TextView) view.findViewById(R.id.alertdialog_tip);
        tv_tip.setText(title);
        tv1 = (TextView) view.findViewById(R.id.alertdialog_tv1);
        tv1.setText(content);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_yes);
        tv_yes.setText(yes);
        tv_yes.setOnClickListener(this);
        super.show();
        setDialogSizeAndXY(1, 1, screenWidth - 150, 0);
        return this;
    }

    //退换货成功
    public MyAlertDialog cancelOrderSuccess(String content) {
        view = inflater.inflate(R.layout.cancel_order_success_alertdialog, null);
        tv1 = (TextView) view.findViewById(R.id.content_tv);
        tv1.setText(content);
        ImageView alertdialog_noIv = (ImageView) view.findViewById(R.id.alertdialog_noIv);
        alertdialog_noIv.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setOnClickListener(this);
        super.show();
        setDialogSizeAndXY(1, 1, screenWidth - 150, 0);
        return this;
    }

    //是否退换货
    public MyAlertDialog cancelOrder(String content) {
        view = inflater.inflate(R.layout.cancel_order_alertdialog, null);
        tv1 = (TextView) view.findViewById(R.id.content_tv);
        tv1.setText(content);
        ImageView alertdialog_noIv = (ImageView) view.findViewById(R.id.alertdialog_noIv);
        alertdialog_noIv.setOnClickListener(this);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_yes);
        tv_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setOnClickListener(this);
        super.show();
        setDialogSizeAndXY(1, 1, screenWidth - 150, 0);
        return this;
    }


    public MyAlertDialog yesAndNo(String title, String content, String yes, String no) {
        view = inflater.inflate(R.layout.yes_and_no_alertdialog, null);
        tv_tip = (TextView) view.findViewById(R.id.alertdialog_tip);
        tv_tip.setText(title);
        tv1 = (TextView) view.findViewById(R.id.alertdialog_tv1);
        tv1.setText(content);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_yes);
        tv_yes.setText(yes);
        tv_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setText(no);
        tv_no.setOnClickListener(this);
        super.show();
        setDialogSizeAndXY(1, 1, screenWidth - 150, 0);
        return this;
    }


    //一个edittext
    public MyAlertDialog oneEdit(String title, String text, String yes, String no, int length) {
        view = inflater.inflate(R.layout.one_edit_alertdialog, null);
        alertdialog_ll = (LinearLayout) view.findViewById(R.id.alertdialog_ll);
        editText1 = (EditText) view.findViewById(R.id.alertdialog_edit1);
        editText1.setText(text.trim());
        editText1.setSelection(text.trim().length());
        editText1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});//限制长度
        tv_tip = (TextView) view.findViewById(R.id.alertdialog_tip);
        tv_tip.setText(title);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_edit_yes);
        tv_yes.setText(yes);
        tv_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setText(no);
        tv_no.setOnClickListener(this);
        super.show();
        setDialogSizeAndXY(1, 1, screenWidth - 150, 0);
        return this;
    }


    //时间三级联动
    public MyAlertDialog threeTimepicker(String title, String yes, String no, String time) {
        view = inflater.inflate(R.layout.three_timepicker_alertdialog, null);
        tv_tip = (TextView) view.findViewById(R.id.alertdialog_tip);
        tv_tip.setText(title);
        LinearLayout threePicker = (LinearLayout) view.findViewById(R.id.alertdialog_threePicker);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_time_yes);
        tv_yes.setText(yes);
        tv_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setText(no);
        tv_no.setOnClickListener(this);
        wheelMain = new WheelMain(threePicker);
        wheelMain.screenheight = (int) (screenHeight * 0.8);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        super.show();
        return this;
    }

    //省市区三级联动
    public MyAlertDialog cityPicker(String tip, String yes, String no, String defaultProvince, String defaultCity) {
        view = inflater.inflate(R.layout.city_picker_alertdialog, null);
        tv_tip = (TextView) view.findViewById(R.id.alertdialog_tip);
        tv_tip.setText(tip);
        tv_yes = (TextView) view.findViewById(R.id.alertdialog_city_yes);
        tv_yes.setText(yes);
        tv_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setText(no);
        tv_no.setOnClickListener(this);
        LinearLayout threePicker = (LinearLayout) view.findViewById(R.id.alertdialog_cityPicker);
        cityWheel = new CityWheel(threePicker, context, defaultProvince, defaultCity);
        super.show();
        return this;
    }

    //奖品抽奖弹窗
    public MyAlertDialog prizeGoods() {
        view = inflater.inflate(R.layout.dialog_prizegoods, null);
        prize_yes = (ImageView) view.findViewById(R.id.alertdialog_yes);
        prize_yes.setOnClickListener(this);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setOnClickListener(this);
        prize_iv = (ImageView) view.findViewById(R.id.home_xrzx_tc);
        super.show();
        return this;
    }
    //快递配送
    public MyAlertDialog expressDelivery(String expressMethod) {
        view = inflater.inflate(R.layout.express_delivery_alertdialog, null);
        tv_no = (TextView) view.findViewById(R.id.alertdialog_no);
        tv_no.setOnClickListener(this);
        tv_yes = (TextView) view.findViewById(R.id.express_delivery_yesTv);
        tv_yes.setOnClickListener(this);
        selectExpress = expressMethod;//默认是选中第一个
        final List<ExpressDelivery> expressDeliveryList = new ArrayList<>();
        ExpressDelivery expressDelivery = null;
        expressDelivery = new ExpressDelivery();
        expressDelivery.setMethod(context.getResources().getString(R.string.unlimited_delivery_time));
        expressDeliveryList.add(expressDelivery);
        expressDelivery = new ExpressDelivery();
        expressDelivery.setMethod(context.getResources().getString(R.string.weekday_delivery));
        expressDeliveryList.add(expressDelivery);
        expressDelivery = new ExpressDelivery();
        expressDelivery.setMethod(context.getResources().getString(R.string.holiday_delivery));
        expressDeliveryList.add(expressDelivery);
        for (int i = 0; i < expressDeliveryList.size(); i++) {
            if (expressMethod.equals(expressDeliveryList.get(i).getMethod())) {
                expressDeliveryList.get(i).setClick(true);
            }
        }
        RecyclerView express_delivery_Rv = (RecyclerView) view.findViewById(R.id.express_delivery_Rv);
        express_delivery_Rv.setLayoutManager(new LinearLayoutManager(context));
        final ExpressDeliveryRvAdapter expressDeliveryRvAdapter = new ExpressDeliveryRvAdapter(context, expressDeliveryList);
        express_delivery_Rv.setAdapter(expressDeliveryRvAdapter);
        express_delivery_Rv.addOnItemTouchListener(new OnRecycleItemListener(express_delivery_Rv) {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                expressDeliveryList.get(position).setClick(!expressDeliveryList.get(position).isClick());
                for (int i = 0; i < expressDeliveryList.size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == position) {
                        continue;
                    }
                    expressDeliveryList.get(i).setClick(false);
                }
                if (expressDeliveryList.get(position).isClick()) {
                    selectExpress = expressDeliveryList.get(position).getMethod();
                } else {
                    selectExpress = "";
                }
                expressDeliveryRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder vh, int position) {

            }
        });
        super.show();
        setBottomDialogSizeAndXY(0, 0, screenWidth, 0);
        return this;
    }
    /**
     * 设置红包图片
     */
    public void setPrizeBackground(int Rid) {
        prize_iv.setBackgroundResource(Rid);
    }

    /**
     * 设置红包图片
     */
    public void setPrizeBtn(int Rid) {
        prize_yes.setBackgroundResource(Rid);
    }

    /**
     * 标题内容
     *
     * @param content
     */
    public MyAlertDialog setTitleTip(String content) {

        if (content != null) {
            tv_tip.setText(content);
        }
        return this;
    }

    /**
     * 提示框内容
     *
     * @param content
     */
    public MyAlertDialog setMsg(String content) {

        if (content != null) {
            content_tv.setText(content);
        }
        return this;
    }


    //显示对话框
    public void show() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//保证edittext能弹出软键盘
        setContentView(view);
    }

    //tv1按钮事件
    public MyAlertDialog tv1(View.OnClickListener tv1_listener) {
        this.tv1_listener = tv1_listener;
        return this;
    }

    //tv2按钮事件
    public MyAlertDialog tv2(View.OnClickListener tv2_listener) {
        this.tv2_listener = tv2_listener;
        return this;
    }

    //tv3按钮事件
    public MyAlertDialog tv3(View.OnClickListener tv3_listener) {
        this.tv3_listener = tv3_listener;
        return this;
    }

    //确定按钮事件
    public MyAlertDialog yes(View.OnClickListener onClickListener) {
        this.yes_listener = onClickListener;
        return this;
    }

    //关闭按钮事件
    public MyAlertDialog Close(View.OnClickListener onClickListener) {
        this.close_listener = onClickListener;
        return this;
    }

    public MyAlertDialog check1(View.OnClickListener checkListener3) {
        this.checkListener3 = checkListener3;
        return this;
    }

    public MyAlertDialog check2(View.OnClickListener checkListener4) {
        this.checkListener4 = checkListener4;
        return this;
    }

    //带edittext点击的确定点击事件
    public MyAlertDialog edit_yes(View.OnClickListener edit_yes_listener) {
        this.edit_yes_listener = edit_yes_listener;
        return this;
    }

    //城市确定按钮事件
    public MyAlertDialog city_yes(View.OnClickListener city_yes_ClickListener) {
        this.city_yes_listener = city_yes_ClickListener;
        return this;
    }

    //时间确定按钮事件
    public MyAlertDialog time_yes(View.OnClickListener time_yes_listener) {
        this.time_yes_listener = time_yes_listener;
        return this;
    }

    //取消按钮事件
    public MyAlertDialog no(View.OnClickListener onClickListener) {
        this.no_listener = onClickListener;
        return this;
    }

    //取消按钮事件
    public MyAlertDialog noIv(View.OnClickListener onClickListener) {
        this.noIv_listener = onClickListener;
        return this;
    }

    //    public MyAlertDialog selectYes(View.OnClickListener select_dialog_sureBt_Listener){
//        this.select_dialog_sureBt_Listener = select_dialog_sureBt_Listener;
//        return this;
//    }
    //设置dialog的显示位置和大小
    private void setDialogSizeAndXY(int x, int y, int width, int height) {
        normaldialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = normaldialogWindow.getAttributes();
        if (x == 0) {
            lp.x = x;
        }
        if (y == 0) {
            lp.y = y;
        }
        if (width != 0) {
            lp.width = width;
        }
        if (height != 0) {
            lp.height = height;
        }
        normaldialogWindow.setAttributes(lp);
    }

    //设置dialog的显示位置和大小
    private void setBottomDialogSizeAndXY(int x, int y, int width, int height) {
        bottomDialogWindow = this.getWindow();
        bottomDialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = bottomDialogWindow.getAttributes();
        if (x == 0) {
            lp.x = x;
        }
        if (y == 0) {
            lp.y = y;
        }
        if (width != 0) {
            lp.width = width;
        }
        if (height != 0) {
            lp.height = height;
        }
        bottomDialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alertdialog_edit_yes:
                if (editText1 != null) {
                    value1 = editText1.getText().toString();
                    if (value1.length() <= 0) {
                        Toast.makeText(context, context.getResources().getString(R.string.input_is_empty), Toast.LENGTH_SHORT).show();
                        return;
//                        value1="null";
                    }
                }
                if (editText2 != null) {
                    value2 = editText2.getText().toString().trim();
                    if (value2.length() <= 0) {
                        Toast.makeText(context, context.getResources().getString(R.string.input_is_empty), Toast.LENGTH_SHORT).show();
                        return;
//                        value2 = "null";
                    }
                }
                if (editText3 != null) {
                    value3 = editText3.getText().toString().trim();
                    if (value3.length() <= 0) {
                        Toast.makeText(context, context.getResources().getString(R.string.input_is_empty), Toast.LENGTH_SHORT).show();
                        return;
//                        value3 = "null";
                    }
                }
                if (!TextUtils.isEmpty(value3)) {
                    result = value1 + "," + value2 + "," + value3;
                } else if (value3 == null && !TextUtils.isEmpty(value2)) {
                    result = value1 + "," + value2;
                } else {
                    result = value1;
                }
                view.setTag(result);
                this.edit_yes_listener.onClick(view);
                break;
            case R.id.alertdialog_city_yes:
                result = cityWheel.getCurrentProviceName() + " " + cityWheel.getCurrentCityName() + " " + cityWheel.getCurrentDistrictName();
                view.setTag(result);
                this.city_yes_listener.onClick(view);
                break;
            case R.id.alertdialog_time_yes:
                result = wheelMain.getTime();
                view.setTag(result);
                this.time_yes_listener.onClick(view);
                break;
            case R.id.alertdialog_yes:
                this.yes_listener.onClick(view);
                break;
            case R.id.alertdialog_no:
                this.no_listener.onClick(view);
                break;
            case R.id.alertdialog_noIv:
                this.noIv_listener.onClick(view);
                break;
            case R.id.alertdialog_tv1:
                this.tv1_listener.onClick(view);
                break;
        }
    }

}
