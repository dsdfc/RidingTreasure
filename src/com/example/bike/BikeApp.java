package com.example.bike;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

public class BikeApp extends Application{
	@Override
	public void onCreate() {
		// 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		StringBuffer param = new StringBuffer();
		param.append("appid=56ebe1be");
		param.append(",");
		param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
//		 param.append(",");
//		 param.append(SpeechConstant.FORCE_LOGIN + "=true");
		SpeechUtility.createUtility(BikeApp.this, param.toString());

		super.onCreate();
	
	}

}
