package com.recipe.r.ui.adapter.share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Comment;
import com.recipe.r.ui.activity.share.CommentsCollegeActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.PermissionUtils;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 2017
 * 07
 * 2017/7/12
 * wangxiaoer
 * 功能描述：
 **/
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mLayoutInflater;
    private CommentsCollegeActivity context;
    // 接收数据集
    private ArrayList<Comment.Item> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private JSONObject data = null;
    private MyOkHttp mMyOkhttp;
    PopupWindow pop;
    private String NEWID;
    private Item1ViewHolder item1ViewHolder;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public CommentAdapter(CommentsCollegeActivity context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        initPopupWindow();
    }

    public void updatelist(ArrayList<Comment.Item> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<Comment.Item> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    public void setContent(JSONObject data_json, String newid) {
        this.data = null;
        this.data = data_json;
        this.NEWID = newid;
        notifyDataSetChanged();
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数
        return position == 0 ? ITEM_TYPE.ITEM1.ordinal() : ITEM_TYPE.ITEM2.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            return new CommentAdapter.Item1ViewHolder(mLayoutInflater.inflate(R.layout.item_comment_head, parent, false));
        } else {
            return new CommentAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_comment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Item1ViewHolder) {
            item1ViewHolder = ((Item1ViewHolder) holder);
            try {
                if (data != null) {

                    if (!TextUtils.isEmpty(data.getString("title"))) {
                        ((Item1ViewHolder) holder).title_comments.setText(data.getString("title"));
                    } else {
                        ((Item1ViewHolder) holder).title_comments.setText("");
                    }
                    if (data.getString("image") != null) {
                        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, Config.IMAGE_URL + data.getString("headimgurl"), WeakImageViewUtil.getImageView(((CommentAdapter.Item1ViewHolder) holder).avater_comments));
                    }
                    if (data.getString("digg_num") != null) {
                        ((CommentAdapter.Item1ViewHolder) holder).zan_share.setText(data.getString("digg_num"));
                    }
                    if (data.getString("source") != null) {
                        ((CommentAdapter.Item1ViewHolder) holder).nickname_comment.setText(data.getString("source"));
                    }
                    if (data.getString("summary") != null) {
                        ((CommentAdapter.Item1ViewHolder) holder).content_comments.setText(data.getString("summary"));
                    }
                    if (data.getString("time") != null) {
                        ((CommentAdapter.Item1ViewHolder) holder).time_comment.setText(DateUtil.getDistanceTime(Long.parseLong(data.getString("time")) * 1000, System.currentTimeMillis()));
                    }
                    if (data.getString("view_num") != null) {
                        ((CommentAdapter.Item1ViewHolder) holder).comments_share.setText(data.getString("view_num"));
                    }
                    ShowImageUtils.showImageView(context, R.mipmap.default_head, Config.IMAGE_URL + data.getString("image"), WeakImageViewUtil.getImageView(((CommentAdapter.Item1ViewHolder) holder).comments_iv1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((CommentAdapter.Item1ViewHolder) holder).ll_comments_share.setOnClickListener(this);
            ((CommentAdapter.Item1ViewHolder) holder).ll_zan_share.setOnClickListener(this);
            ((CommentAdapter.Item1ViewHolder) holder).conveniently_share.setOnClickListener(this);
        } else if (holder instanceof Item2ViewHolder) {
            final Comment.Item exchangeItem = mDatas.get(position - 1);
            holder.itemView.setTag(mDatas.get(position - 1));
            if (TextUtils.isEmpty(exchangeItem.getUser_name())) {
                ((CommentAdapter.Item2ViewHolder) holder).nickname_comment.setText("游客");
            } else {
                ((CommentAdapter.Item2ViewHolder) holder).nickname_comment.setText(exchangeItem.getUser_name());
            }
            ((CommentAdapter.Item2ViewHolder) holder).content_comment.setText(exchangeItem.getContent());
            ((CommentAdapter.Item2ViewHolder) holder).time_comment.setText(DateUtil.getTimeToString(Long.parseLong(exchangeItem.getTime())));
            ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_photo, Config.IMAGE_URL + exchangeItem.getHeadimgurl(), WeakImageViewUtil.getImageView(((CommentAdapter.Item2ViewHolder) holder).comment_iv));
            if (exchangeItem.getReply() != null) {
                if (exchangeItem.getReply().size() > 0) {
                    ArrayList<Comment.Reply> reply_list = exchangeItem.getReply();
                    ((CommentAdapter.Item2ViewHolder) holder).comment_reply.setAdapter(new CommentReplyAdapter(context, reply_list, exchangeItem.getUser_name()));
                    //TODO listView 显示不全
                    setListViewHeightBasedOnChildren(((CommentAdapter.Item2ViewHolder) holder).comment_reply);
                }
            } else {
                ((CommentAdapter.Item2ViewHolder) holder).comment_reply.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(view, (Comment.Item) view.getTag());
                    }

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 1 : mDatas.size() + 1;
    }

    //item1 的ViewHolder
    public static class Item1ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_comments;
        private ImageView avater_comments;
        private TextView nickname_comment;
        private TextView time_comment;
        private TextView content_comments;
        private TextView zan_share;
        private TextView comments_share;
        private ArrayList<ImageView> comments_iv;
        private ImageView comments_iv1;
        private ImageView comments_iv2;
        private ImageView comments_iv3;
        private LinearLayout ll_comments_share;
        private LinearLayout ll_zan_share;
        private ImageView dianzan;
        private ImageView conveniently_share;


        public Item1ViewHolder(View itemView) {
            super(itemView);
            title_comments = (TextView) itemView.findViewById(R.id.title_comments);
            avater_comments = (ImageView) itemView.findViewById(R.id.avater_comments);
            nickname_comment = (TextView) itemView.findViewById(R.id.nickname_comment);
            time_comment = (TextView) itemView.findViewById(R.id.time_comment);
            content_comments = (TextView) itemView.findViewById(R.id.content_comments);
            zan_share = (TextView) itemView.findViewById(R.id.zan_share);
            comments_share = (TextView) itemView.findViewById(R.id.comments_share);
            conveniently_share = (ImageView) itemView.findViewById(R.id.conveniently_share);
            dianzan = (ImageView) itemView.findViewById(R.id.dianzan);
            if (comments_iv == null) {
                comments_iv = new ArrayList<>();
            }
            ll_comments_share = (LinearLayout) itemView.findViewById(R.id.ll_comments_share);
            ll_zan_share = (LinearLayout) itemView.findViewById(R.id.ll_zan_share);
            comments_iv1 = (ImageView) itemView.findViewById(R.id.comments_iv1);
            comments_iv2 = (ImageView) itemView.findViewById(R.id.comments_iv2);
            comments_iv3 = (ImageView) itemView.findViewById(R.id.comments_iv3);
            comments_iv.add(comments_iv1);
            comments_iv.add(comments_iv2);
            comments_iv.add(comments_iv3);

        }
    }

    //item2 的ViewHolder
    public class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname_comment;
        TextView content_comment;
        TextView time_comment;
        ImageView comment_iv;
        private ListView comment_reply;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            nickname_comment = (TextView) itemView.findViewById(R.id.nickname_comment);
            content_comment = (TextView) itemView.findViewById(R.id.content_comment);
            time_comment = (TextView) itemView.findViewById(R.id.time_comment);
            comment_iv = (ImageView) itemView.findViewById(R.id.comment_iv);
            comment_reply = (ListView) itemView.findViewById(R.id.comment_reply);

        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Comment.Item data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comments_share:
                context.isAddReply = false;
                pop.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_zan_share:
                parizeInfo(NEWID);
                break;

            case R.id.conveniently_share:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    if (PermissionUtils.isCameraPermission(context, 0x007))
                        context.showPhotoDialog();
                }
                break;
        }
    }

    /**
     * 资讯点赞
     */
    private void parizeInfo(String news_id) {
        MyOkHttp mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        String url = Config.URL + Config.COMMON_DIGG;
        Map<String, String> params = new HashMap<>();
        params.put("item_id", news_id);
        params.put("type", Config.DIGG_SHARE);
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {
                                //刷新
                                if (item1ViewHolder != null) {
                                    item1ViewHolder.dianzan.setBackgroundResource(R.mipmap.like);
                                }
                                getCommentsDetails(1);
                            }else {
                                ToastUtil.show(context,info,500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 获取详情
     */
    private void getCommentsDetails(final int type) {
        String url = Config.URL + Config.GETINFORMATIONDETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("news_id", NEWID);
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        int status = 0;
                        try {
                            status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                if (item1ViewHolder != null) {
                                    item1ViewHolder.zan_share.setText("赞" + data.getString("digg_num"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /**
     * 自定义的评论弹窗 包含@评论人和发布评论 监听Edittext的输入状态
     */
    private void initPopupWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_comments, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        TextView cancle_pop = (TextView) view.findViewById(R.id.cancle_pop);
        final EditText et_pop = (EditText) view.findViewById(R.id.et_pop);
        RelativeLayout pop_head = (RelativeLayout) view
                .findViewById(R.id.pop_head);
        final TextView published = (TextView) view.findViewById(R.id.published);
        pop.setFocusable(true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        cancle_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pop.setText("");
                pop.dismiss();
                et_pop.clearFocus();
            }
        });
        pop_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                et_pop.setText("");
                pop.dismiss();
                et_pop.clearFocus();
            }
        });
        et_pop.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_pop.getText().length() == 0) {
                    published.setTextColor(context.getResources().getColor(
                            R.color.gray_home));
                    published.setFocusable(false);
                } else {
                    published.setTextColor(context.getResources().getColor(
                            R.color.black_gray));
                    published.setFocusable(true);
                    published.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            if (!chkEditText(et_pop)) {
                                Toast.makeText(context, "未输入内容",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String str = stringFilter(et_pop.getText().toString());
                            if (!(et_pop.getText().toString())
                                    .equals(str)) {
                                Toast.makeText(context, "内容携带特殊字符",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            setPublished(str);
                            et_pop.setText("");// 清空
                            pop.dismiss();
                            et_pop.clearFocus();
                        }
//                            }).show();
//                        }
                    });
                }
            }
        });
    }

    /**
     * 发布评论
     */
    private void setPublished(final String content) {
        String url = Config.URL + Config.ADDCOMMENT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("news_id", NEWID);
        params.put("content", content);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            ToastUtil.show(context, info, 500);
                            if (status == 1) {
                                //刷新
                                context.swipeToLoadLayout.setRefreshing(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 检测EditText不能为空;
     *
     * @param ets
     * @return true：表示EditTexts都不为空； false:表示有EditTexts为空；
     */
    public boolean chkEditText(EditText... ets) {
        for (EditText et : ets) {
            String str = et.getText().toString();
            if (TextUtils.isEmpty(str)) {
                // et.setError(et.getHint() + "不能为空");
                return false;
            }
        }
        return true;
    }

    /**
     * scrollview嵌套listview显示不全解决
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}