package com.recipe.r.ui.adapter.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.ImageBean;
import com.recipe.r.entity.ShareItem;
import com.recipe.r.ui.activity.share.CommentsCollegeActivity;
import com.recipe.r.ui.activity.share.PhotoGalleryActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NoHtml;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017/6/12
 * wangxiaoer
 * 功能描述：分享发布圈适配器
 **/
public class ShareRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private List<ShareItem.Sharedata> mDatas = null;
    private String zan;
    private int number;
    ArrayList<ImageBean> imgs_list;
    private String Banner_Image = "";
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int TYPE = 1;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public ShareRecyclerViewAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (imgs_list == null) {
            imgs_list = new ArrayList<ImageBean>();
        }
    }

    public void updatelist(ArrayList<ShareItem.Sharedata> list) {
        this.mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void append(ArrayList<ShareItem.Sharedata> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    public void setBanner(String imageUrl, int type) {
        this.TYPE = type;
        this.Banner_Image = imageUrl;
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
            return new ShareRecyclerViewAdapter.Item1ViewHolder(mLayoutInflater.inflate(R.layout.item_share_head, parent, false));
        } else {
            return new ShareRecyclerViewAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_share_division, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Item1ViewHolder) {
            ShowImageUtils.showImageView(context, R.mipmap.default_photo, Banner_Image, ((Item1ViewHolder) holder).share_banner_iv);
            if (TYPE == 1) {
                //他人分享
                ((Item1ViewHolder) holder).avater_share_my.setVisibility(View.GONE);
                ((Item1ViewHolder) holder).nickname_share_my.setVisibility(View.GONE);
            } else if (TYPE == 2) {
                ((Item1ViewHolder) holder).avater_share_my.setVisibility(View.VISIBLE);
                ((Item1ViewHolder) holder).nickname_share_my.setVisibility(View.VISIBLE);
            }
            ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_photo, AppSettings.getPrefString(context, ConfigApp.AVATER, ""), ((Item1ViewHolder) holder).avater_share_my);
            if (AppSettings.getPrefString(context, ConfigApp.USERNAME, "") != null) {
                ((Item1ViewHolder) holder).nickname_share_my.setText(AppSettings.getPrefString(context, ConfigApp.USERNAME, ""));
            }
        } else if (holder instanceof Item2ViewHolder) {
            final ShareItem.Sharedata shareItem = mDatas.get(position - 1);
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).itemView.setTag(mDatas.get(position - 1));
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).name_share.setText(shareItem.getUser_name());
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).content_share.setText(NoHtml.toNoHtml(shareItem.getSummary()));
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).time_share.setText("" + DateUtil.getDistanceTime(Long.parseLong(shareItem.getTime()) * 1000, System.currentTimeMillis()));
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).comments_share.setText(shareItem.getView_num());
            ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).zan_share.setText(shareItem.getDigg_num());
            ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_photo, Config.IMAGE_URL + shareItem.getHeadimgurl(), WeakImageViewUtil.getImageView(((ShareRecyclerViewAdapter.Item2ViewHolder) holder).head_share_item));
            //防止重叠
            for (int i = 0; i < 9; i++) {
                ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i).setVisibility(View.GONE);
                ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share1.setVisibility(View.GONE);// 3张图片显示
                ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share2.setVisibility(View.GONE);// 3张图片显示
                ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share3.setVisibility(View.GONE);// 3张图片显示
            }
            //发表的图片
            if (shareItem.getImages() != null) {
                if (shareItem.getImages().size() > 0 && shareItem.getImages().size() <= 3) {
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share1.setVisibility(View.VISIBLE);// 3张图片显示
                    for (int i = 0; i < shareItem.getImages().size(); i++) {
                        ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i).setVisibility(View.VISIBLE);
                        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + shareItem.getImages().get(i), WeakImageViewUtil.getImageView(((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i)));
                    }
                } else if (shareItem.getImages().size() > 3 && shareItem.getImages().size() <= 6) {
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share1.setVisibility(View.VISIBLE);// 3张图片显示
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share2.setVisibility(View.VISIBLE);// 3张图片显示
                    for (int i = 0; i < shareItem.getImages().size(); i++) {
                        ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i).setVisibility(View.VISIBLE);
                        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + shareItem.getImages().get(i), WeakImageViewUtil.getImageView(((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i)));

                    }
                } else if (shareItem.getImages().size() > 6 && shareItem.getImages().size() <= 9) {
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share1.setVisibility(View.VISIBLE);// 3张图片显示
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share2.setVisibility(View.VISIBLE);// 3张图片显示
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share3.setVisibility(View.VISIBLE);// 3张图片显示
                    for (int i = 0; i < shareItem.getImages().size(); i++) {
                        ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i).setVisibility(View.VISIBLE);
                        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + shareItem.getImages().get(i), WeakImageViewUtil.getImageView(((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i)));

                    }
                } else {
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share1.setVisibility(View.GONE);// 3张图片显示
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share2.setVisibility(View.GONE);// 3张图片显示
                    ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).ll_share3.setVisibility(View.GONE);// 3张图片显示
                }
            }
            //判断是否点赞
//        if (shareItem.getType().equals("0")) {
//            zan = "0";
            ((Item2ViewHolder) holder).dianzan.setBackgroundResource(R.mipmap.like);
//        } else {
//            zan = "1";
//            ((Item2ViewHolder) holder).dianzan.setBackgroundResource(R.mipmap.like);
//        }
            //点赞
            ((Item2ViewHolder) holder).dianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parizeInfo(shareItem.getNews_id(), shareItem.getDigg_num(), ((Item2ViewHolder) holder).dianzan, shareItem.getType(), ((Item2ViewHolder) holder).zan_share);
                }
            });
            //评论
            ((Item2ViewHolder) holder).ll_comments_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 評論
                    Intent intent = new Intent(context,
                            CommentsCollegeActivity.class);
                    intent.putExtra("news_id", shareItem.getNews_id());
                    context.startActivity(intent);
                }
            });
            if (shareItem.getImages() != null) {
                if (shareItem.getImages().size() > 0) {
                    imgs_list.clear();
                    for (int i = 0; i < shareItem.getImages().size(); i++) {
                        ImageBean imagesbean = new ImageBean();
                        imagesbean.setImageUrl(shareItem.getImages().get(i));
                        imgs_list.add(imagesbean);
                        final int finalI = i;
                        ((ShareRecyclerViewAdapter.Item2ViewHolder) holder).photos.get(i).setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        SeeImage(imgs_list,finalI);
                                    }
                                });
                    }

                }
            }
            ((Item2ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(view, (ShareItem.Sharedata) view.getTag());
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
        ImageView share_banner_iv;
        ImageView avater_share_my;
        TextView nickname_share_my;

        public Item1ViewHolder(View itemView) {
            super(itemView);
            share_banner_iv = (ImageView) itemView.findViewById(R.id.share_banner_iv);
            avater_share_my = (ImageView) itemView.findViewById(R.id.avater_share_my);
            nickname_share_my = (TextView) itemView.findViewById(R.id.nickname_share_my);
        }
    }

    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        ArrayList<ImageView> photos = new ArrayList<>();
        TextView name_share;
        ImageView head_share_item;
        TextView content_share;
        TextView time_share;
        TextView comments_share;
        LinearLayout ll_comments_share;
        TextView zan_share;
        LinearLayout ll_share1;
        LinearLayout ll_share2;
        LinearLayout ll_share3;
        ImageView image1_share;
        ImageView image2_share;
        ImageView image3_share;
        ImageView image4_share;
        ImageView image5_share;
        ImageView image6_share;
        ImageView image7_share;
        ImageView image8_share;
        ImageView image9_share;
        private ImageView dianzan;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            name_share = (TextView) itemView.findViewById(R.id.name_share);
            ll_comments_share = (LinearLayout) itemView.findViewById(R.id.ll_comments_share);
            comments_share = (TextView) itemView.findViewById(R.id.comments_share);
            head_share_item = (ImageView) itemView.findViewById(R.id.head_share_item);
            content_share = (TextView) itemView.findViewById(R.id.content_share);
            time_share = (TextView) itemView.findViewById(R.id.time_share);
            zan_share = (TextView) itemView.findViewById(R.id.zan_share);
            ll_share1 = (LinearLayout) itemView.findViewById(R.id.ll_share1);
            ll_share2 = (LinearLayout) itemView.findViewById(R.id.ll_share2);
            ll_share3 = (LinearLayout) itemView.findViewById(R.id.ll_share3);
            image1_share = (ImageView) itemView.findViewById(R.id.image1_share);
            image2_share = (ImageView) itemView.findViewById(R.id.image2_share);
            image3_share = (ImageView) itemView.findViewById(R.id.image3_share);
            image4_share = (ImageView) itemView.findViewById(R.id.image4_share);
            image5_share = (ImageView) itemView.findViewById(R.id.image5_share);
            image6_share = (ImageView) itemView.findViewById(R.id.image6_share);
            image7_share = (ImageView) itemView.findViewById(R.id.image7_share);
            image8_share = (ImageView) itemView.findViewById(R.id.image8_share);
            image9_share = (ImageView) itemView.findViewById(R.id.image9_share);
            dianzan = (ImageView) itemView.findViewById(R.id.dianzan);
            photos.add(image1_share);
            photos.add(image2_share);
            photos.add(image3_share);
            photos.add(image4_share);
            photos.add(image5_share);
            photos.add(image6_share);
            photos.add(image7_share);
            photos.add(image8_share);
            photos.add(image9_share);
        }
    }

    /**
     * 资讯点赞
     */
    private void parizeInfo(String news_id, final String dianzannum, final ImageView dianzan, String praised, final TextView zannum) {
//        int praiseNum = Integer.parseInt(dianzannum);
//        // 判断是否被赞
//        if (praised.equals("0")) {
//            // 赞
//            if (zan.equals("1")) {
//                number = praiseNum;
//            } else {
//                number = --praiseNum;
//            }
//        } else {
//            // 没
//            if (zan.equals("1")) {
//                number = ++praiseNum;
//            } else {
//                number = praiseNum;
//            }
//        }
        MyOkHttp mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        String url = Config.URL + Config.COMMON_DIGG;
        Map<String, String> params = new HashMap<>();
        params.put("item_id", news_id);
        params.put("type", Config.DIGG_SHARE);
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
//                                if (zan.equals("1")) {
//                                    dianzan.setBackgroundResource(R.mipmap.selector_zan);
//                                    zannum.setText("赞" + number);
//                                    zan = "0";
//                                } else {
                                dianzan.setBackgroundResource(R.mipmap.like);
                                zannum.setText("" + (Integer.parseInt(dianzannum) + 1));
//                                    zan = "1";
//                                }
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

    // 查看大图
    private void SeeImage(ArrayList<ImageBean> imagesUrl,int position) {
        Intent intent_path = new Intent(context, PhotoGalleryActivity.class);
        intent_path.putExtra("position", position);
        Bundle bundle_path = new Bundle();
        bundle_path.putSerializable("key", imagesUrl);
        intent_path.putExtras(bundle_path);
        context.startActivity(intent_path);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ShareItem.Sharedata data);
    }
}