//package com.recipe.r.ui.activity.chat;
//
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.ui.EaseContactListFragment;
//import com.recipe.r.R;
//import com.recipe.r.ui.activity.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * 客服通讯录
// */
//public class BookActivity extends BaseActivity {
//    private EaseContactListFragment contactListFragment;
//    private BroadcastReceiver broadcastReceiver;
//    private LocalBroadcastManager broadcastManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book);
//        initView();
//    }
//
//    private void initView() {
//        contactListFragment = new EaseContactListFragment();
//        if (getContacts() != null) {
//            contactListFragment.setContactsMap(getContacts());
//        }
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_book, contactListFragment)
//                .show(contactListFragment).commit();
//        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
//
//            @Override
//            public void onListItemClicked(EaseUser user) {
//                startActivity(new Intent(BookActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//            }
//        });
//    }
//
//    /**
//     * 获取服务器数据
//     *
//     * @return
//     */
//    private Map<String, EaseUser> getContacts() {
//        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
//        List<String> usernames = new ArrayList<>();
//        usernames.add("guxinshishang103136");
//        usernames.add("guxinshishang103132");
//        usernames.add("guxinshishang103133");
//        for (int i = 0; i < usernames.size(); i++) {
//            EaseUser user = new EaseUser(usernames.get(i));
//            contacts.put(usernames.get(i), user);
//        }
//        return contacts;
//    }
//
////    EMMessageListener messageListener = new EMMessageListener() {
////
////        @Override
////        public void onMessageReceived(List<EMMessage> list) {
////            // notify new message
////
////            refreshUIWithMessage();
////        }
////
////        @Override
////        public void onCmdMessageReceived(List<EMMessage> list) {
////            // red packet code : 处理红包回执透传消息
////
////            // end of red packet code
////            refreshUIWithMessage();
////        }
////
////        @Override
////        public void onMessageRead(List<EMMessage> list) {
////
////        }
////
////        @Override
////        public void onMessageDelivered(List<EMMessage> list) {
////
////        }
////
////        @Override
////        public void onMessageChanged(EMMessage emMessage, Object o) {
////
////        }
////    };
////
////    private void refreshUIWithMessage() {
////        runOnUiThread(new Runnable() {
////            public void run() {
////                if (contactListFragment != null) {
////                    contactListFragment.refresh();
////                }
////            }
////        });
////    }
////
////
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        unregisterBroadcastReceiver();
////    }
////
////    private void unregisterBroadcastReceiver() {
////        broadcastManager.unregisterReceiver(broadcastReceiver);
////    }
//}