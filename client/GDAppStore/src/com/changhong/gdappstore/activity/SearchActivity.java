package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.SearchResultAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.model.SearchAppModel;
import com.changhong.gdappstore.util.L;

/**
 * searchActivity
 * 
 * This activity used to search app by word or number. The search results will
 * be listed by GridView at the right side in this activity, the gridview will
 * be notifyDataSetChanged when you change the editText data.
 * 
 * @author wangxiufeng
 * 
 */
public class SearchActivity extends BaseActivity implements OnClickListener {

	private int[] id_keybords = { R.id.bt_fullkeybord_0, R.id.bt_fullkeybord_1, R.id.bt_fullkeybord_2,
			R.id.bt_fullkeybord_3, R.id.bt_fullkeybord_4, R.id.bt_fullkeybord_5, R.id.bt_fullkeybord_6,
			R.id.bt_fullkeybord_7, R.id.bt_fullkeybord_8, R.id.bt_fullkeybord_9, R.id.bt_fullkeybord_0,
			R.id.bt_fullkeybord_a, R.id.bt_fullkeybord_b, R.id.bt_fullkeybord_c, R.id.bt_fullkeybord_d,
			R.id.bt_fullkeybord_e, R.id.bt_fullkeybord_f, R.id.bt_fullkeybord_g, R.id.bt_fullkeybord_h,
			R.id.bt_fullkeybord_i, R.id.bt_fullkeybord_j, R.id.bt_fullkeybord_k, R.id.bt_fullkeybord_l,
			R.id.bt_fullkeybord_m, R.id.bt_fullkeybord_n, R.id.bt_fullkeybord_o, R.id.bt_fullkeybord_p,
			R.id.bt_fullkeybord_q, R.id.bt_fullkeybord_r, R.id.bt_fullkeybord_s, R.id.bt_fullkeybord_t,
			R.id.bt_fullkeybord_u, R.id.bt_fullkeybord_v, R.id.bt_fullkeybord_w, R.id.bt_fullkeybord_x,
			R.id.bt_fullkeybord_y, R.id.bt_fullkeybord_z };
	/** 输入框 */
	private EditText editText;
	/** 按钮：中文，回退，清楚，换一批 */
	private Button bt_chinese, bt_backone, bt_clear, bt_nextpage;
	/** 查询结果页码提示 */
	private TextView tv_searchresult;
	/** 查询结果 */
	private GridView gv_search;
	/** 搜索结果列表适配器 */
	private SearchResultAdapter adapter;
	/** 搜索结果 */
	private List<SearchAppModel> dataList = new ArrayList<SearchAppModel>();
	/** 上次选中的grideview */
	private RelativeLayout lastContentLayout;
	/** 选中放大动画 */
	private Animation scallAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
		initData();
		updateAppnumTextVisible();
	}

	private void initView() {
		for (int i = 0; i < id_keybords.length; i++) {
			findViewById(id_keybords[i]).setOnClickListener(keyBordOnClickListener);
		}
		editText = findView(R.id.edt_search);
		editText.addTextChangedListener(textWatcher);
		bt_backone = findView(R.id.bt_search_backone);
		bt_chinese = findView(R.id.bt_search_chinese);
		bt_clear = findView(R.id.bt_search_clear);
		bt_nextpage = findView(R.id.bt_nextpage);
		tv_searchresult = findView(R.id.tv_num_searchresult);
		gv_search = findView(R.id.gv_search_result);

		bt_backone.setOnClickListener(this);
		bt_chinese.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
		bt_nextpage.setOnClickListener(this);

		scallAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_big);
		adapter = new SearchResultAdapter(context);
		gv_search.setAdapter(adapter);
		gv_search.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				L.d("search gridview onItemSelected " + position);
				RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_appsearch_content);
				rl_content.setBackgroundResource(R.drawable.focues_post);
				rl_content.startAnimation(scallAnimation);
				if (lastContentLayout != null) {
					lastContentLayout.setBackgroundColor(Color.TRANSPARENT);
					lastContentLayout.clearAnimation();
				}
				lastContentLayout = rl_content;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				L.d("search gridview onNothingSelected");
				if (lastContentLayout != null) {
					lastContentLayout.setBackgroundColor(Color.TRANSPARENT);
					lastContentLayout.clearAnimation();
				}
			}
		});
		if (gv_search != null && gv_search.getChildAt(0) != null) {
			View view = gv_search.getChildAt(0);
			RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_appsearch_content);
			rl_content.setBackgroundColor(Color.TRANSPARENT);
			rl_content.clearAnimation();
			if (lastContentLayout != null) {
				lastContentLayout.setBackgroundColor(Color.TRANSPARENT);
				lastContentLayout.clearAnimation();
			}
			lastContentLayout = rl_content;
		}
		bt_chinese.requestFocus();
	}

	private void initData() {
		for (int i = 0; i < 20; i++) {
			dataList.add(new SearchAppModel("应用名字" + i, R.drawable.img_post1, 4.5f));
		}
		adapter.updateData(dataList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_search_chinese:
			editText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.bt_search_backone:
			String curEdtString = editText.getText().toString().trim();
			editText.setText(TextUtils.isEmpty(curEdtString) ? ""
					: curEdtString.substring(0, curEdtString.length() - 1));
			break;
		case R.id.bt_search_clear:
			editText.setText("");
			break;
		case R.id.bt_nextpage:

			break;

		default:
			break;
		}
	}

	private OnClickListener keyBordOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			editText.append(button.getText());
		}
	};

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			L.d("textWatcher ontextchanged--" + s + " " + start + " " + before + " " + count);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			L.d("textWatcher beforeTextChanged--" + s + " " + start + " " + after + " " + count);

		}

		@Override
		public void afterTextChanged(Editable s) {
			L.d("textWatcher afterTextChanged--" + s.toString());
			updateAppnumTextVisible();
		}
	};
	/**
	 * 跟新搜索列表上面文字和按钮显示
	 * 如果输入框内容为空，显示换一批按钮。如果有信息，显示搜索结果个数。
	 */
	private void updateAppnumTextVisible() {
		if (TextUtils.isEmpty(editText.getText().toString().trim())) {
			// 输入框为空时候，显示推荐。
			tv_searchresult.setVisibility(INVISIBLE);
			bt_nextpage.setVisibility(VISIBLE);
			findViewById(R.id.tv_everybody_search).setVisibility(VISIBLE);
		} else {
			tv_searchresult.setVisibility(VISIBLE);
			bt_nextpage.setVisibility(INVISIBLE);
			findViewById(R.id.tv_everybody_search).setVisibility(INVISIBLE);
			int datasize = dataList.size();
			if (datasize > 0) {
				tv_searchresult.setText("当前搜索结果有：" + dataList.size() + " 个应用。");
			} else {
				tv_searchresult.setText("没有搜索到：“" + editText.getText().toString().trim() + "”相关应用，请重新输入搜索信息！");
			}

		}
	}

}
