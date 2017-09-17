//package com.recipe.r.ui.fragment.chat;
//
//import android.content.Intent;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chat.EMConversation.EMConversationType;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.ui.EaseConversationListFragment;
//import com.hyphenate.util.NetUtils;
//import com.recipe.r.R;
//import com.recipe.r.base.Constant;
//import com.recipe.r.ui.activity.chat.BookActivity;
//import com.recipe.r.ui.activity.chat.ChatActivity;
//
//
///**
// * 2017/7/11
// * wangxiaoer
// * 功能描述：聊天界面(会话列表)
// **/
//public class ConversationListFragment extends EaseConversationListFragment {
//    private TextView errorText;
//
//
//    @Override
//    protected void initView() {
//        super.initView();
//        View errorView = (LinearLayout) View.inflate(getActivity(),
//                R.layout.em_chat_neterror_item, null);
//        errorItemContainer.addView(errorView);
//        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
//    }
//
//    @Override
//    protected void setUpView() {
//        super.setUpView();
//        // register context menu
//        registerForContextMenu(conversationListView);
//        conversationListView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                EMConversation conversation = conversationListView
//                        .getItem(position);
//                String username = conversation.conversationId();
//                if (username.equals(EMClient.getInstance().getCurrentUser()))
//                    Toast.makeText(getActivity(),
//                            R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
//                else {
//                    // start chat acitivity
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    if (conversation.isGroup()) {
//                        if (conversation.getType() == EMConversationType.ChatRoom) {
//                            // it's group chat
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE,
//                                    Constant.CHATTYPE_CHATROOM);
//                        } else {
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE,
//                                    Constant.CHATTYPE_GROUP);
//                        }
//
//                    }
//                    // 聊天界面，传值uid
//                    intent.putExtra(Constant.EXTRA_USER_ID, username);
//                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat); //单聊模式
//                    startActivity(intent);
//                }
//            }
//        });
//        titleBar.setLeftLayoutVisibility(View.VISIBLE);
//        titleBar.setLeftImageResource(R.mipmap.reset_back);
//        titleBar.setRightLayoutVisibility(View.VISIBLE);
//        titleBar.setRightImageResource(R.mipmap.address_book);
//        titleBar.setBackgroundColor(getActivity().getResources().getColor(R.color.main_color));
//        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
//        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 通讯录
//                Intent intent_pawncircle = new Intent(getActivity(),
//                        BookActivity.class);
//                startActivity(intent_pawncircle);
//            }
//        });
//        super.setUpView();
//
//    }
//
//    @Override
//    protected void onConnectionDisconnected() {
//        super.onConnectionDisconnected();
//        if (NetUtils.hasNetwork(getActivity())) {
//            errorText.setText(R.string.can_not_connect_chat_server_connection);
//        } else {
//            errorText.setText(R.string.the_current_network);
//        }
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenuInfo menuInfo) {
//        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        EMConversation tobeDeleteCons = conversationListView
//                .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position - 1);
//        if (tobeDeleteCons == null) {
//            return true;
//        }
//        refresh();
//
//        return true;
//    }
//
//}
