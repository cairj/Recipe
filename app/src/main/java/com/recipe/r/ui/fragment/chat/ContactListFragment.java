///**
// * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.recipe.r.ui.fragment.chat;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.ui.EaseContactListFragment;
//import com.hyphenate.util.EMLog;
//import com.hyphenate.util.NetUtils;
//import com.recipe.r.R;
//import com.recipe.r.base.Constant;
//import com.recipe.r.ui.activity.chat.BookActivity;
//import com.recipe.r.ui.activity.chat.ChatActivity;
//import com.recipe.r.utils.AppManager;
//import com.recipe.r.utils.Logger;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Hashtable;
//import java.util.Map;
//
///**
// * 联系人列表 通讯录
// */
//public class ContactListFragment extends EaseContactListFragment {
//
//	private static final String TAG = ContactListFragment.class.getSimpleName();
//	private ContactSyncListener contactSyncListener;
//	private BlackListSyncListener blackListSyncListener;
//	private ContactInfoSyncListener contactInfoSyncListener;
//	private View loadingView;
//
//
//	@Override
//	protected void initView() {
//		super.initView();
//		// add loading view
//		loadingView = LayoutInflater.from(getActivity()).inflate(
//				R.layout.em_layout_loading_data, null);
//		contentContainer.addView(loadingView);
//		registerForContextMenu(listView);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void refresh() {
//		Map<String, EaseUser> m = PawnHelper.getInstance().getContactList();
//		if (m instanceof Hashtable<?, ?>) {
//			m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m)
//					.clone();
//		}
//		setContactsMap(m);
//		super.refresh();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void setUpView() {
//		// 设置联系人数据
//		Map<String, EaseUser> m = PawnHelper.getInstance().getContactList();
//		if (m instanceof Hashtable<?, ?>) {
//			m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m)
//					.clone();
//		}
//		setContactsMap(m);
//		super.setUpView();
//		// 设置联系人点击事件
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				String username = ((EaseUser) listView
//						.getItemAtPosition(position)).getUsername();
//				Intent intent = new Intent(getActivity(), ChatActivity.class);
//				// 聊天界面，传值uid
//				intent.putExtra(Constant.EXTRA_USER_ID, username);
//				intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat); //单聊模式
//				startActivity(intent);
//			}
//		});
//
//		titleBar.setLeftImageResource(R.mipmap.reset_back);
//		titleBar.setLeftLayoutClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				AppManager.getInstance().killActivity(BookActivity.class);
//			}
//		});
//		contactSyncListener = new ContactSyncListener();
//		PawnHelper.getInstance().addSyncContactListener(contactSyncListener);
//		blackListSyncListener = new BlackListSyncListener();
//		PawnHelper.getInstance()
//				.addSyncBlackListListener(blackListSyncListener);
//		contactInfoSyncListener = new ContactInfoSyncListener();
//		PawnHelper.getInstance().getUserProfileManager()
//				.addSyncContactInfoListener(contactInfoSyncListener);
//
//		if (PawnHelper.getInstance().isContactsSyncedWithServer()) {
//			loadingView.setVisibility(View.GONE);
//		} else if (PawnHelper.getInstance().isSyncingContactsWithServer()) {
//			loadingView.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if (contactSyncListener != null) {
//			PawnHelper.getInstance().removeSyncContactListener(
//					contactSyncListener);
//			contactSyncListener = null;
//		}
//
//		if (blackListSyncListener != null) {
//			PawnHelper.getInstance().removeSyncBlackListListener(
//					blackListSyncListener);
//		}
//
//		if (contactInfoSyncListener != null) {
//			PawnHelper.getInstance().getUserProfileManager()
//					.removeSyncContactInfoListener(contactInfoSyncListener);
//		}
//	}
//
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		toBeProcessUser = (EaseUser) listView
//				.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
//		toBeProcessUsername = toBeProcessUser.getUsername();
//		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list,
//				menu);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.delete_contact) {
//			try {
//				Logger.e("联系人","删除好友");
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return true;
//		} else if (item.getItemId() == R.id.add_to_blacklist) {
//			moveToBlacklist(toBeProcessUsername);
//			return true;
//		}
//		return super.onContextItemSelected(item);
//	}
//
//
//
//	class ContactSyncListener implements DataSyncListener {
//		@Override
//		public void onSyncComplete(final boolean success) {
//			EMLog.d(TAG, "on contact list sync success:" + success);
//			getActivity().runOnUiThread(new Runnable() {
//				public void run() {
//					getActivity().runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							if (success) {
//								loadingView.setVisibility(View.GONE);
//								refresh();
//							} else {
//								String s1 = getResources().getString(
//										R.string.get_failed_please_check);
//								Toast.makeText(getActivity(), s1, 1).show();
//								loadingView.setVisibility(View.GONE);
//							}
//						}
//
//					});
//				}
//			});
//		}
//	}
//
//	class BlackListSyncListener implements DataSyncListener {
//
//		@Override
//		public void onSyncComplete(boolean success) {
//			getActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					refresh();
//				}
//			});
//		}
//
//	};
//
//	class ContactInfoSyncListener implements DataSyncListener {
//
//		@Override
//		public void onSyncComplete(final boolean success) {
//			EMLog.d(TAG, "on contactinfo list sync success:" + success);
//			getActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					loadingView.setVisibility(View.GONE);
//					if (success) {
//						refresh();
//					}
//				}
//			});
//		}
//
//	}
//
//
//
//}
