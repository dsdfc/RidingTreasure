package com.example.bike.Frag.sec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bike.R;
import com.example.bike.Fragment.BaseFragment;
import com.example.bike.robot.ChatMessage;

/**
 * 政务
 * 
 * @author Kevin
 * 
 */
public class SheZhiFragment extends BaseFragment {
	
	EditText et_text;
	Button btn_send;
	TextView tv_result;
	

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_test_robot, null);
		et_text = (EditText) view.findViewById(R.id.et_text);
		btn_send = (Button) view.findViewById(R.id.btn_send);
		tv_result = (TextView) view.findViewById(R.id.tv_result);
		
		return view;
	}
	
	@Override
	public void initData() {
		super.initData();
	}

}
