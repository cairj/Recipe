package com.recipe.r.ui.adapter.share;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.Comment;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * Created by hj on 2017/8/19.
 */
public class CommentReplyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment.Reply> list;
    private String TAG = "CommentReplyAdapter";
    private String UserName = "";

    public CommentReplyAdapter(Context context, ArrayList<Comment.Reply> replylist,String username) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list.clear();
        this.UserName = "";
        this.list = replylist;
        this.UserName = username;
        this.context = context;
    }


    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Comment.Reply getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        CommentReplyAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_reply,
                    null);
            holder = new CommentReplyAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (CommentReplyAdapter.ViewHolder) view.getTag();
        }
        Comment.Reply reply_list = list.get(position);
        if (TextUtils.isEmpty(reply_list.getUser_name())) {
            holder.nickname_reply_comment.setText("游客");
        } else {
            holder.nickname_reply_comment.setText(reply_list.getUser_name());
        }
        holder.content_reply_comment.setText("回复" + UserName + ":" + reply_list.getContent());
        holder.time_reply_comment.setText(DateUtil.getTimeToString(reply_list.getTime()));
        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_photo, Config.IMAGE_URL + reply_list.getHeadimgurl(), WeakImageViewUtil.getImageView(holder.comment_reply_iv));
        return view;
    }

    private class ViewHolder {
        TextView nickname_reply_comment;
        TextView content_reply_comment;
        TextView time_reply_comment;
        ImageView comment_reply_iv;

        public ViewHolder(View itemView) {
            nickname_reply_comment = (TextView) itemView.findViewById(R.id.nickname_reply_comment);
            content_reply_comment = (TextView) itemView.findViewById(R.id.content_reply_comment);
            time_reply_comment = (TextView) itemView.findViewById(R.id.time_reply_comment);
            comment_reply_iv = (ImageView) itemView.findViewById(R.id.comment_reply_iv);
        }
    }

}