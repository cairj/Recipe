//package com.recipe.r.ui.activity.chat;
//
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.Window;
//
//import com.hyphenate.easeui.ui.EaseChatFragment;
//import com.recipe.r.R;
//
//
///**
// * chat activity，EaseChatFragment was used
// */
//public class ChatActivity extends FragmentActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.em_activity_chat);
//
//        EaseChatFragment easeChatFragment = new EaseChatFragment();  //环信聊天界面
//        easeChatFragment.setArguments(getIntent().getExtras()); //需要的参数
//        getSupportFragmentManager().beginTransaction().add(R.id.layout_chat, easeChatFragment).commit();  //Fragment切换
//
//    }
//}