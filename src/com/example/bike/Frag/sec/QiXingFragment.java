package com.example.bike.Frag.sec;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.NaviMode;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.bike.R;

import com.example.bike.Adapter.RouteSearchPoiDialog;
import com.example.bike.Adapter.RouteSearchPoiDialog.OnListItemClick;
import com.example.bike.music.MusicModel;
import com.example.bike.music.Saomiao;
import com.example.bike.robot.ChatMessage;
import com.example.bike.util.AMapUtil;
import com.example.bike.util.JsonParser;
import com.example.bike.util.TTSController;
import com.example.bike.util.yudong_data;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.UserWords;

/**
 * 
 * @author Kevin 骑行界面
 * 
 */
public class QiXingFragment extends Fragment implements LocationSource,
		AMapLocationListener, OnMapClickListener, OnMapLoadedListener,
		OnCameraChangeListener, OnMarkerClickListener, AMapNaviListener,
		OnInfoWindowClickListener, InfoWindowAdapter, OnPoiSearchListener,
		OnRouteSearchListener {

	// 语音识别部分
	private RecognizerDialog mRecognizerDialog;
	private SpeechRecognizer mSpeechRecognizer;
	private SpeechSynthesizer mSpeechSynthesizer;
	private Button mStartSpeechButton;
	public SeekBar seekBar;
	public ImageView nextm, lastm, playm, style;
	public TextView mTvCurrentTime;
	public TextView mTvMaxTime;
	Saomiao saomiao;
	private ArrayList<MusicModel> mMusicList;
	private Map<String, Integer> musicNameMap;
	private ArrayList<String> mMusicNameList;
	private ArrayList<String> mBikeWords;
	TextView geci;
	LocalBroadcastManager lbm;
	ControlBroadcastReceiver controlBroadcastReceiver;
	private Boolean isplay = false;
	private int check = 0;
	private Boolean isFirst = true;
	private UserWords myUserWords;
	private String userString;
	Boolean isSpeeching = false;
	Boolean isStop = false;
	private String TAG = "dsdfc";
	private Boolean isRobot = false;
	private Boolean isBegin = false;
	private Boolean isUpload = false;  // 判断是否上传

	private LinearLayout ll_1;

	// 地图部分
	private static QiXingFragment fragment = null;
	public static final int POSITION = 0;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private MapView mapView;
	private AMap aMap;
	private View mapLayout;
	private TextView gpstN;
	private TextView voicedaohan;
	private String avg;
	private UiSettings mUiSettings;
	private Button zuji, start, end;
	private Marker currentMarker;
	private Animation centerMarker;
	private double latitude;// 维度
	private double longitude;// 经度
	private LatLng ll;
	private AMapLocation startAMapLocation;
	private LatLng oldll, qidianll;
	private MarkerOptions markerOptions;
	private View infoWindow;
	private Marker marker;
	private ArrayList<MarkerOptions> markeroptionArrayList;
	private AMapNaviGuide guide;
	// private String speedgengxiao;
	private String type = "one";// 定位类型
	// private float perspeed ;//前一个速度
	private float speed = 0;// 速度
	private float maxspeed;// 最高速度
	private float sumlucheng = 0;// 总里程
	private String kaluli;// 卡路里
	private String shijian;// 事件
	private double haiba;// 海拔
	private String avgspeed;// 平均速度
	private yudong_data data;
	private float distance = 2;
	private String voicetype = "one";
	private RelativeLayout remap, respeed;
	private LinearLayout ll_tree, lldata;
	// 语音导航
	NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
	NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
	List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
	List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
	private String isroute;
	private AMapNavi aMapNavi;
	private RouteOverLay routeOverLay;
	private int routeIndex;
	private int[] routeIds;
	private TTSController ttsManager;
	private boolean chooseRouteSuccess;
	private boolean mapClickStartReady;
	private boolean mapClickEndReady;
	private Marker mStartMarker;
	private Marker mWayMarker;
	private Marker mEndMarker;
	private boolean calculateSuccess;
	private RelativeLayout relayout_roadserch_top;
	private EditText endTextView;
	private String strEnd;
	private String routeMakerType;
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	private PoiSearch.Query endSearchQuery;
	private PoiSearch.Query startSearchQuery;
	public ArrayAdapter<String> aAdapter;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private String cityname;
	private String citycode;
	private String address;
	private ImageView imgV_road_search;
	private Button button_search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 此方法加载布局文件
		/*
		 * View view = View.inflate(getActivity(), R.layout.qixing_pager, null);
		 * mStartSpeechButton = (Button)
		 * view.findViewById(R.id.btn_start_speech); mTvCurrentTime = (TextView)
		 * view .findViewById(R.id.tv_current_music_time); mTvMaxTime =
		 * (TextView) view.findViewById(R.id.tv_max_music_time); nextm =
		 * (ImageView) view.findViewById(R.id.xia); lastm = (ImageView)
		 * view.findViewById(R.id.shang); playm = (ImageView)
		 * view.findViewById(R.id.play); style = (ImageView)
		 * view.findViewById(R.id.style); geci = (TextView)
		 * view.findViewById(R.id.geci); seekBar = (SeekBar)
		 * view.findViewById(R.id.se);
		 * 
		 * return view;
		 */

		super.onSaveInstanceState(savedInstanceState);
		Log.e("zhangsan", "------------------>>" + savedInstanceState);
		if (mapLayout == null) {
			Log.i("sys", "MF onCreateView() null");

			mapLayout = inflater.inflate(R.layout.qixing_pager, null);

			// 语音部分
			mStartSpeechButton = (Button) mapLayout
					.findViewById(R.id.btn_start_speech);
			mTvCurrentTime = (TextView) mapLayout
					.findViewById(R.id.tv_current_music_time);
			mTvMaxTime = (TextView) mapLayout
					.findViewById(R.id.tv_max_music_time);
			nextm = (ImageView) mapLayout.findViewById(R.id.xia);
			lastm = (ImageView) mapLayout.findViewById(R.id.shang);
			playm = (ImageView) mapLayout.findViewById(R.id.play);
			style = (ImageView) mapLayout.findViewById(R.id.style);
			geci = (TextView) mapLayout.findViewById(R.id.geci);
			seekBar = (SeekBar) mapLayout.findViewById(R.id.se);

			// 高德地图部分
			mapView = (MapView) mapLayout.findViewById(R.id.map);
			gpstN = (TextView) mapLayout.findViewById(R.id.gpstN);
			zuji = (Button) mapLayout.findViewById(R.id.zuji);
			button_search = (Button) mapLayout.findViewById(R.id.button_search);
			// start = (Button) mapLayout.findViewById(R.id.start);
			// end = (Button) mapLayout.findViewById(R.id.end);
			remap = (RelativeLayout) mapLayout.findViewById(R.id.remap);
			lldata = (LinearLayout) mapLayout.findViewById(R.id.lldata);
			ll_tree = (LinearLayout) mapLayout.findViewById(R.id.ll_tree);
			respeed = (RelativeLayout) mapLayout.findViewById(R.id.respeed);
			relayout_roadserch_top = (RelativeLayout) mapLayout
					.findViewById(R.id.RelativeLayout_roadsearch_top);
			voicedaohan = (TextView) mapLayout.findViewById(R.id.voice);
			endTextView = (EditText) mapLayout
					.findViewById(R.id.autotextview_roadsearch_goals);
			imgV_road_search = (ImageView) mapLayout
					.findViewById(R.id.imagebtn_roadsearch_search);
			Log.e("zhangsan", "------------------>>" + savedInstanceState);

			/*
			 * 语音导航
			 */

			voicedaohan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (voicetype == "one") {
						Drawable drawable = getResources().getDrawable(
								R.drawable.mic_3);
						voicedaohan.setCompoundDrawablesWithIntrinsicBounds(
								null, drawable, null, null); // 设置TextView的drawabletop
						voicetype = "two";
						ll_tree.setVisibility(View.GONE);
						relayout_roadserch_top.setVisibility(View.VISIBLE);
						button_search.setVisibility(View.VISIBLE);

						routeMakerType = "roadserch_top";
						searchRoute();
						// start.setVisibility(View.VISIBLE);
						// end.setVisibility(View.VISIBLE);
						aMap.clear();
						lldata.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						remap.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, 950));

						// 语音导航
						if (isOPen(getActivity())) {
							qidianll = ll;
						}

						aMapNavi = AMapNavi.getInstance(getActivity());
						aMapNavi.addAMapNaviListener(QiXingFragment.this);
						ttsManager = TTSController.getInstance(getActivity());
						ttsManager.init();
						ttsManager.startSpeaking();
						aMapNavi.addAMapNaviListener(ttsManager);
						mStartMarker = aMap.addMarker(new MarkerOptions()
								.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
										.decodeResource(getResources(),
												R.drawable.icon_update_point)))
								.anchor(0.5f, 0.5f)
								.visible(true)
								.title("地址：" + startAMapLocation.getCity()
										+ "/" + startAMapLocation.getDistrict()
										+ "/" + startAMapLocation.getStreet()
										+ "/" + startAMapLocation.getPoiName())
								.snippet("时间：" + convertToTime())
								.perspective(true).draggable(false));

						// mWayMarker = aMap.addMarker(new MarkerOptions()
						// .icon(BitmapDescriptorFactory
						// .fromBitmap(BitmapFactory
						// .decodeResource(getResources(),
						// R.drawable.way))));
						mEndMarker = aMap.addMarker(new MarkerOptions()
								.icon(BitmapDescriptorFactory
										.fromBitmap(BitmapFactory
												.decodeResource(getResources(),
														R.drawable.end))));
						mlocationClient.startLocation();
						aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
						isroute = "open";
						Log.e("voicedaohan", "----->>" + "daohangkaishi");
						zuji.setVisibility(View.GONE);
						// 语音锁定目的地

					} else {
						Drawable drawable = getResources().getDrawable(
								R.drawable.mic_0);
						voicedaohan.setCompoundDrawablesWithIntrinsicBounds(
								null, drawable, null, null); // 设置TextView的drawabletop
						ll_tree.setVisibility(View.VISIBLE);

						// start.setVisibility(View.GONE);
						// end.setVisibility(View.GONE);
						remap.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, 450));
						lldata.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
						zuji.setVisibility(View.VISIBLE);
						button_search.setVisibility(View.GONE);
						relayout_roadserch_top.setVisibility(View.GONE);
						isroute = "close";
						voicetype = "one";
						routeMakerType = "mapclick";
						Log.e("voicedaohan", "----->>" + "daohangguangbi");
					}

				}
			});

			mapView.onCreate(savedInstanceState);
			if (aMap == null) {
				aMap = mapView.getMap();
				// 足迹
				zuji.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (type == "one") {
							Log.e("zuji", "---->>" + "zujiopen");
							aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
							mlocationClient.startLocation();
							zuji.setText("完成");
							type = "two";
						}

						else {

							aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
							zuji.setText("足迹");
							// marker.setVisible(true);
							Log.e("zuji", "---->>"
									+ "zujiguanbi__dingwei__5000");

							type = "one";
							aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
						}

					}
				});
				setUpMap();
			}
		} else {
			if (mapLayout.getParent() != null) {
				((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
			}
		}
		getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// 跳转到大地图监听事件
		// 开始点
		// start.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// chooseStart();
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
		//
		// }
		// });
		// 结束点
		// end.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// chooseEnd();
		// Log.e("end", "---->>" + "shexiangtou_yingdongdao_15");
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		//
		// }
		// });
		imgV_road_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchRoute();
				if (calculateSuccess) {
					relayout_roadserch_top.setVisibility(View.GONE);
				}
			}
		});
		button_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchRoute();
				if (calculateSuccess) {
					relayout_roadserch_top.setVisibility(View.VISIBLE);
				}
			}
		});
		return mapLayout;

	}

	// 高德地图部分

	// 时间转化为String
	public static String convertToTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		String timestr = formatter.format(curDate);
		return timestr;
	}

	public static Fragment newInstance() {
		if (fragment == null) {
			synchronized (QiXingFragment.class) {
				if (fragment == null) {
					fragment = new QiXingFragment();
				}
			}
		}
		return fragment;
	}

	// 判读Gps信号
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = false;
		gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Log.e("gpsxinghao", "--->>" + gps);
		return gps;
	}

	/**
	 * 查询路径规划终点
	 */
	public void endSearchResult() {
		strEnd = endTextView.getText().toString().trim();
		// if (endPoint != null && strEnd.equals("地图上的终点")) {
		// searchRouteResult(startPoint, endPoint);
		// } else {
		showProgressDialog();
		endSearchQuery = new PoiSearch.Query(address+strEnd, "", citycode); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
		Log.e("endSearchResult", "---------------->>" + citycode);
		endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
		endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

		PoiSearch poiSearch = new PoiSearch(getActivity(), endSearchQuery);
//		SearchBound searchBound = new SearchBound(startPoint, 1000, true);
//		poiSearch.setBound(searchBound);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn(); // 异步poi查询

	}
	
	
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(getActivity());
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索");
		progDialog.show();
	}
	
	public void searchRoute() {
		// strStart = startTextView.getText().toString().trim();
		strEnd = endTextView.getText().toString().trim();
		// if (strStart == null || strStart.length() == 0) {
		// ToastUtil.show(RouteActivity.this, "请选择起点");
		// return;
		// }
		if (strEnd == null || strEnd.length() == 0) {
			Toast.makeText(getActivity(), "你要去哪呀，告诉我呗", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		/*
		 * if (strStart.equals(strEnd)) { ToastUtil.show(RouteActivity.this,
		 * "起点与终点距离很近，您可以步行前往"); return; }
		 */
		Log.e("searchRoute", "---------------->>" + "endsearchResult");
		endSearchResult();// 开始搜终点

	}
	
	
	private void setUpMap() {

		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.moveCamera(CameraUpdateFactory.zoomTo(25));
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		aMap.setOnMapLoadedListener(this);
		aMap.setOnCameraChangeListener(this);
		aMap.setOnMapLoadedListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
		MyLocationStyle locationStyle = new MyLocationStyle();
		Log.e("setupmap", "---->>" + "landianto_icon_update_point");
		locationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.icon_update_point)));
		aMap.setOnMarkerClickListener(this);
		// aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapClickListener(this);
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		startAMapLocation = arg0;

		// Log.e("zhangsan", "zhangsan------------>>" + arg0.getDistrict());
		if (mListener != null && arg0 != null) {
			if (arg0 != null && arg0.getErrorCode() == 0) {
				markerOptions = new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.visible(true)
						.position(ll)
						.title("地址：" + arg0.getCity() + "/" + arg0.getStreet()
								+ "/" + arg0.getPoiName())
						.snippet("时间：" + convertToTime()).perspective(true)
						.draggable(false);
                      
				citycode = arg0.getCityCode();
				haiba = arg0.getAltitude();
				address=arg0.getStreet();
				Log.e("address", "----->>"+address);
				markerOptions.setFlat(true);
				// markeroptionArrayList.add(markerOptions);
				// markeroptionArrayList.size();
				marker = aMap.addMarker(markerOptions
						.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.icon_update_point))));
				mListener.onLocationChanged(arg0);// 显示系统小蓝点

				ll = new LatLng(arg0.getLatitude(), arg0.getLongitude());

				if (oldll != null) {
					distance = AMapUtils.calculateLineDistance(oldll, ll);

				}

				if (ll != oldll && (distance > 1)) {
					Log.e("onLocationChanged",
							"---->>"
									+ "onLocationChanged_dingweichenggong_marker_hongdian");
					speed = arg0.getSpeed();
					sumlucheng = sumlucheng + distance;
					if (maxspeed <= speed) {
						maxspeed = speed;
					}
					aMap.addPolyline((new PolylineOptions()).add(oldll, ll)
							.color(Color.GREEN).width(10));
					mLocationOption.setInterval(Long.valueOf(5));
				} else {
					speed = 0;
			       mLocationOption.setInterval(Long.valueOf(10000));
				}

				// markeroptionArrayList.add(markerOptions);
				distance = 0;
				// } else {
				// marker.remove();
				// mLocationOption.setInterval(Long.valueOf(10000));
				// aMap.addPolyline((new PolylineOptions()).add(oldll, ll)
				// .color(Color.GREEN).width(0));
				//
				// marker = aMap.addMarker(markerOptions
				// .icon(BitmapDescriptorFactory
				// .fromBitmap(BitmapFactory.decodeResource(
				// getResources(),
				// R.drawable.wusetouming))));
				// marker.showInfoWindow();
				// Log.e("onLocationChanged",
				// "---->>"
				// +
				// "onLocationChanged_meiyidongbudingwei_marker_wuxianbaishe");

			}
		} else {
			String errText = "定位失败," + arg0.getErrorCode() + ": "
					+ arg0.getErrorInfo();
			distance = 0;

		}
		/**
		 * 得到运动数据
		 * 
		 */
		// 时间

		// shijian = shijian
		// + Time(arg0.getTime() - startAMapLocation.getTime());

		// data.setShijian(shijian);

		// sum距离

		// sumlucheng=sumlucheng+guide.getLength();

		String strll = ll.toString();

		// String str = time + strll;

	}
	// 激活定位
		@Override
		public void activate(OnLocationChangedListener arg0) {
			mListener = arg0;
			if (mlocationClient == null) {
				Log.e("activate", "---->>" + "dingwei_jihuo_5s");
				mlocationClient = new AMapLocationClient(getActivity());
				mLocationOption = new AMapLocationClientOption();
				// 设置定位监听
				mlocationClient.setLocationListener(this);
				mLocationOption.isGpsFirst();
				// 设置为高精度定位模式
				mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
				mLocationOption.setInterval(Long.valueOf(5000));
				// 设置定位参数
				mLocationOption.setGpsFirst(true);
				mlocationClient.setLocationOption(mLocationOption);

				// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
				// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
				// 在定位结束后，在合适的生命周期调用onDestroy()方法
				// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
				mlocationClient.startLocation();
				if (isOPen(getActivity())) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.ic_gps_four);
					gpstN.setCompoundDrawablesWithIntrinsicBounds(null, null,
							drawable, null); // 设置TextView的drawableleft
					Log.e("gps", "isgps");
				} else {

					Toast.makeText(getActivity(), "0.0..gps无信号", Toast.LENGTH_SHORT)
							.show();
					Drawable drawable = getResources().getDrawable(
							R.drawable.ic_gps_zero);
					gpstN.setCompoundDrawablesWithIntrinsicBounds(null, null,
							drawable, null);
				}
			}
		}

		@Override
		public void deactivate() {
			mListener = null;

			if (mlocationClient != null) {
				mlocationClient.stopLocation();
				mlocationClient.onDestroy();
			}
			mlocationClient = null;
			Log.e("deactivate", "---->>" + "dingwei_guangbi");

		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

		}

		@Override
		public void onResume() {
			Log.i("sys", "mf onResume");
			super.onResume();
			mapView.onResume();

		}

		/**
		 * 方法必须重写 map的生命周期方法
		 */
		@Override
		public void onPause() {
			Log.i("sys", "mf onPause");
			super.onPause();
			mapView.onPause();
			deactivate();

			ttsManager.stopSpeaking();
			Log.e("onPause", "---->>" + "zhang_ting");
		}

		/**
		 * 方法必须重写 map的生命周期方法
		 */
		@Override
		public void onSaveInstanceState(Bundle outState) {
			Log.i("sys", "mf onSaveInstanceState");
			super.onSaveInstanceState(outState);
			mapView.onSaveInstanceState(outState);
		}

		/**
		 * 方法必须重写 map的生命周期方法
		 */
		@Override
		public void onDestroy() {
			Log.i("sys", "mf onDestroy");
			super.onDestroy();
			mapView.onDestroy();
			aMapNavi.stopNavi();
			ttsManager.destroy();
			aMapNavi.destroy();
			lbm.unregisterReceiver(controlBroadcastReceiver);
			Log.e("onDestroy", "---->>" + "xiaohui");
		}

		@Override
		public boolean onMarkerClick(Marker arg0) {
			marker.showInfoWindow();
			if (arg0.isInfoWindowShown()) {
				arg0.hideInfoWindow();
			} else {
				arg0.showInfoWindow();
			}

			return true;
		}

		@Override
		public void onCameraChange(CameraPosition arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCameraChangeFinish(CameraPosition arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMapLoaded() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMapClick(LatLng arg0) {
			Log.e("zhangsan", "--------------------->>" + "onmapclick");
			if (isroute == "open") {
				if (calculateSuccess) {
					Log.e("zhangsan", "--------------------->>" + "remove");
					routeOverLay.removeFromMap();
				}

				if (qidianll != null) {
					Log.e("zhangsan", "--------------------->>" + "ll");
					startLatlng = new NaviLatLng(qidianll.latitude,
							qidianll.longitude);
					startPoint = AMapUtil.convertToLatLonPoint(qidianll);

					mStartMarker.setPosition(qidianll);
					mStartMarker.showInfoWindow();
					startList.clear();
					startList.add(startLatlng);
				}

				if (arg0 != ll) {
					endLatlng = new NaviLatLng(arg0.latitude, arg0.longitude);
					endPoint = AMapUtil.convertToLatLonPoint(arg0);
					mEndMarker.setPosition(arg0);
					mEndMarker.showInfoWindow();
					endList.clear();
					endList.add(endLatlng);
					Log.e("zhangsan", "--------------------->>" + "end");
				}
				// 计算步行路线
				Log.e("zhangsan", "------------------->>" + "13131314");

				calculateSuccess = aMapNavi.calculateWalkRoute(startLatlng,
						endLatlng);

				if (calculateSuccess) {
					relayout_roadserch_top.setVisibility(View.GONE);
					mLocationOption.setInterval(Long.valueOf(2000));
					Log.e("zhangsan", "---------->>" + "mapclick--location");

				}
				Log.e("zhangsan", "------------------->>" + calculateSuccess);
				if (!calculateSuccess) {
					Toast.makeText(getActivity(), "路线计算失败，请再试试", Toast.LENGTH_SHORT)
							.show();
					relayout_roadserch_top.setVisibility(View.VISIBLE);

				}

				mapClickEndReady = false;
				mapClickStartReady = false;
			}

		}

		@Override
		public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void hideCross() {
			// TODO Auto-generated method stub

		}

		@Override
		public void hideLaneInfo() {
			// TODO Auto-generated method stub

		}

		@Override
		public void notifyParallelRoad(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onArriveDestination() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onArrivedWayPoint(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCalculateMultipleRoutesSuccess(int[] arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCalculateRouteFailure(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCalculateRouteSuccess() {

			// 你可以通过对应的路径ID获得一条道路路径AMapNaviPath
			Log.e("zhangsan", "------------------>>" + "route");
			AMapNaviPath naviPath = aMapNavi.getNaviPath();
			Log.e("zhangsan", "------------------>>" + naviPath);
			if (naviPath == null) {
				return;
			}
			// 获取路径规划线路，显示到地图上

			routeOverLay = new RouteOverLay(aMap, naviPath, getActivity());
			routeOverLay.setTransparency(1f);
			Toast.makeText(
					getActivity(),
					"导航距离:" + naviPath.getAllLength() + "m" + "\n" + "导航时间:"
							+ naviPath.getAllTime() + "s", Toast.LENGTH_SHORT)
					.show();
			routeOverLay.addToMap();
			if (calculateSuccess) {
				if (isOPen(getActivity()))
					aMapNavi.startNavi(NaviMode.GPS);
				else {
					aMapNavi.startNavi(NaviMode.EMULATOR);
				}
			}

		}

		@Override
		public void onEndEmulatorNavi() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetNavigationText(int arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGpsOpenStatus(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onInitNaviFailure() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onInitNaviSuccess() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChange(AMapNaviLocation arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNaviInfoUpdate(NaviInfo arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNaviInfoUpdated(AMapNaviInfo arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReCalculateRouteForTrafficJam() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReCalculateRouteForYaw() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartNavi(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTrafficStatusUpdate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void showCross(AMapNaviCross arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPoiItemSearched(PoiItem arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPoiSearched(PoiResult arg0, int arg1) {
			dissmissProgressDialog();
			Log.e("onPoiSearched", "----arg0--------->>" + arg0);
			Log.e("onPoiSearched", "------arg1------->>" + arg1);
			Log.e("onPoiSearched", "-------getQuery------>>" + arg0.getQuery());
			Log.e("onPoiSearched", "--------getPois----->>" + arg0.getPois());
			Log.e("onPoiSearched", "----------size--->>" + arg0.getPois().size());

			if (arg1 == 0) {// 返回成功
				if (arg0 != null && arg0.getQuery() != null
						&& arg0.getPois() != null && arg0.getPois().size() > 0) {// 搜索poi的结果
					if (arg0.getQuery().equals(startSearchQuery)) {
						List<PoiItem> poiItems = arg0.getPois();// 取得poiitem数据
						RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
								getActivity(), poiItems);
						dialog.setTitle("您要找的起点是:");
						dialog.show();
						dialog.setOnListClickListener(new OnListItemClick() {
							@Override
							public void onListItemClick(
									RouteSearchPoiDialog dialog,
									PoiItem startpoiItem) {
								startPoint = startpoiItem.getLatLonPoint();
								// strStart = startpoiItem.getTitle();
								// startTextView.setText(strStart);
								endSearchResult();// 开始搜终点
							}

						});
					} else if (arg0.getQuery().equals(endSearchQuery)) {
						List<PoiItem> poiItems = arg0.getPois();// 取得poiitem数据
						RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
								getActivity(), poiItems);
						dialog.setTitle("您要找的终点是:");
						dialog.show();
						dialog.setOnListClickListener(new OnListItemClick() {
							@Override
							public void onListItemClick(
									RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
								endPoint = endpoiItem.getLatLonPoint();

								endLatlng = new NaviLatLng(endPoint.getLatitude(),
										endPoint.getLongitude());
								strEnd = endpoiItem.getTitle();
								endTextView.setText(strEnd);
								if (qidianll != null) {
									startLatlng = new NaviLatLng(qidianll.latitude,
											qidianll.longitude);
								}

								calculateSuccess = aMapNavi.calculateWalkRoute(
										startLatlng, endLatlng);

								if (calculateSuccess) {
									aMap.clear();
									relayout_roadserch_top.setVisibility(View.GONE);
									mLocationOption.setInterval(Long.valueOf(2000));
									Log.e("zhangsan", "---------->>"
											+ "mapclick--location");

								}
								Log.e("zhangsan", "------------------->>"
										+ calculateSuccess);
								if (!calculateSuccess) {
									Toast.makeText(getActivity(), "路线计算失败，请再试试",
											Toast.LENGTH_SHORT).show();

								}

								mapClickEndReady = false;
								mapClickStartReady = false;

							}

						});
					}
				} else {
					Toast.makeText(getActivity(), "哈哈，附近没有这个地方啊！",
							Toast.LENGTH_SHORT).show();

				}
			} else if (arg1 == 27) {
				Toast.makeText(getActivity(), "error_network", Toast.LENGTH_SHORT)
						.show();

			} else if (arg1 == 32) {
				Toast.makeText(getActivity(), "error_key", Toast.LENGTH_SHORT)
						.show();

			} else {
				Toast.makeText(getActivity(), "error_other" + arg1,
						Toast.LENGTH_SHORT).show();

			}

		}

		@Override
		public View getInfoContents(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onInfoWindowClick(Marker arg0) {
			// TODO Auto-generated method stub

		}

	
		/**
		 * 隐藏进度框
		 */
		private void dissmissProgressDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}
	
	
	
	
	
	
	
	// 语音识别部分

	@Override
	public void onStart() {
		super.onStart();
		initSpeech();
		zhuce();
		getMusicMsg();
		setAllOnclickListener();
	}

	/**
	 * 注册广播接收器
	 */
	public void zhuce() {
		lbm = LocalBroadcastManager.getInstance(getActivity());
		controlBroadcastReceiver = new ControlBroadcastReceiver();
		IntentFilter mFilter = new IntentFilter();

		mFilter.addAction("ACTION_INDEX");
		mFilter.addAction("ACTION_MAXTIME");
		mFilter.addAction("ACTION_NOWTIME");
		mFilter.addAction("ACTION_NOWMUSIC");

		lbm.registerReceiver(controlBroadcastReceiver, mFilter);
	}

	private void setAllOnclickListener() {
		mStartSpeechButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "你点击了开始说话", Toast.LENGTH_SHORT)
						.show();
				startSpeech();
			}
		});

		nextm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextmusic(1);
			}
		});
		lastm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextmusic(0);
			}
		});
		playm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isplay) {
					isplay = false;
					playm.setImageResource(R.drawable.dianji_19);

				} else {
					isplay = true;
					playm.setImageResource(R.drawable.dianji_20);
					if (isFirst) {
						playMusicIndex(0);
					}
				}
				musicPlayState(isplay);
			}
		});

		// 控制播放模式
		style.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				check++;
				if (check > 3)
					check = 1;

				if (check == 1) {
					style.setImageResource(R.drawable.dianji_28);
					stylemusic(1);
				}
				if (check == 2) {
					style.setImageResource(R.drawable.dianji1_28);
					stylemusic(2);
				}
				if (check == 3) {
					style.setImageResource(R.drawable.pic2_28);
					stylemusic(3);
				}
			}
		});
		geci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "你点击了歌词", Toast.LENGTH_SHORT)
						.show();
			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser == true) {
					Intent intent = new Intent();
					intent.setAction("ACTION_SEEKBAR");
					intent.putExtra("seekbar", progress);
					lbm.sendBroadcast(intent);
				}
			}
		});
	}

	public class ControlBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("ACTION_INDEX")) {
				isplay = true;
				isFirst = false;
				playm.setImageResource(R.drawable.dianji_20);

			} else if (intent.getAction().equals("ACTION_MAXTIME")) {
				int maxtime = intent.getIntExtra("maxtime", 0);
				seekBar.setMax(maxtime);
				mTvMaxTime.setText(setTime(maxtime) + "");
			} else if (intent.getAction().equals("ACTION_NOWMUSIC")) {
				String currentMusic = intent.getStringExtra("nowmusic");
				Toast.makeText(getActivity(), "您正在播放" + currentMusic,
						Toast.LENGTH_LONG).show();
			} else if (intent.getAction().equals("ACTION_NOWTIME")) {
				int currentPos = intent.getIntExtra("nowtime", 0);
				seekBar.setProgress(currentPos);
				mTvCurrentTime.setText(setTime(currentPos) + "");
			}
		}
	}

	

	public String setTime(int time) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date = new Date(time);
		return sdf.format(date);
	}

	// 上一曲，下一曲，0上一曲，1下一曲
	public void nextmusic(int num) {

		isplay = true;
		playm.setImageResource(R.drawable.dianji_20);

		Intent intent = new Intent();
		intent.setAction("ACTION_NEXT");
		intent.putExtra("next", num);
		lbm.sendBroadcast(intent);
	}

	// 发送播放/暂停广播
	public void musicPlayState(boolean isplay) {
		Intent intent = new Intent();
		intent.setAction("ACTION_ISPLAY");
		intent.putExtra("isplay", isplay);
		lbm.sendBroadcast(intent);
	}

	public void stylemusic(int num) {
		Intent intent = new Intent();
		intent.setAction("ACTION_STYLE");
		intent.putExtra("check", num);
		lbm.sendBroadcast(intent);
	}

	private void getMusicMsg() {
		saomiao = Saomiao.getInstance();
		mMusicList = saomiao.query(getActivity());
		musicNameMap = saomiao.getMusicNameIndex();
		mMusicNameList = new ArrayList<String>();

		for (Map.Entry<String, Integer> entry : musicNameMap.entrySet()) {
			String musicName = entry.getKey();
			mMusicNameList.add(musicName);
		}
	}

	/**
	 * 根据索引来播放音乐
	 * 
	 * @param index
	 */
	private void playMusicIndex(int index) {
		Intent intent = new Intent();
		intent.setAction("ACTION_INDEX");
		intent.putExtra("index", index);
		lbm.sendBroadcast(intent);

	}

	private void initSpeech() {

		mSpeechRecognizer = SpeechRecognizer.createRecognizer(getActivity(),
				mInitListener);
		mRecognizerDialog = new RecognizerDialog(getActivity(), mInitListener);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity(),
				mInitListener);

		setParam();
		upLoadUserwords();

		mSpeechSynthesizer.startSpeaking("您好！欢迎使用动感单车，正在为您搜索GPS信号   "
				+ "恭喜。搜索完成", mTtsListener);

	}

	private void startSpeech() {
		mRecognizerDialog.setListener(dialogListener);
		mRecognizerDialog.show();

	}

	/**
	 * 语音合成监听器
	 */
	SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakResumed() {
			Log.i(TAG, "继续播放");
		}

		@Override
		public void onSpeakProgress(int percent, int arg1, int arg2) {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakBegin() {
			Log.i(TAG, "开始播放");
			isSpeeching = true;
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
			} else if (error != null) {
				Log.i(TAG, error.getPlainDescription(true));
			}
			isSpeeching = false;
			if (isBegin && !isUpload) {
				new MusicIndexThread(1000).start();
			}

		}

		@Override
		public void onBufferProgress(int percent, int arg1, int arg2,
				String arg3) {
		}
	};

	/**
	 * 初始化监听器
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(getActivity(), "初始化失败,错误码：" + code,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private class MusicIndexThread extends Thread {
		private long sleepTime = 0;

		public MusicIndexThread(long time) {
			sleepTime = time;
		}

		@Override
		public void run() {
			super.run();
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = 1;
			if (!isSpeeching)
				musicIndex_handler.sendMessage(message);
			Log.d(TAG, "469:当前线程MusicIndexThread");
		}
	}

	private class MusicIndexHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.d(TAG, "yes receive msg");
				mRecognizerDialog.setListener(dialogListener);
				mRecognizerDialog.show();
			}
		}
	}

	private Handler musicIndex_handler = new MusicIndexHandler();

	private void speech_musicIndex() {
		Log.d(TAG, "调用 speech_musicIndex()");
		for (Map.Entry<String, Integer> music : musicNameMap.entrySet()) {
			if (music.getKey().equals(userString)) {
				playMusicIndex(musicNameMap.get(userString));
			}
		}
		new MusicIndexThread(5000).start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ChatMessage from = (ChatMessage) msg.obj;

			Log.d(TAG, "服务器回应:" + from.getMsg());
			mSpeechSynthesizer.startSpeaking(from.getMsg(), mTtsListener);
		}
	};

	private void speech_robot() {

		new Thread() {
			public void run() {

				ChatMessage from = null;
				try {
					Log.d(TAG, "客户端说：" + userString);
					from = com.example.bike.robot.HttpUtils.sendMsg(userString);
				} catch (Exception e) {

				}

				Message message = Message.obtain();
				message.obj = from;
				mHandler.sendMessage(message);

			};
		}.start();
	}

	/**
	 * 带对话框的监听器
	 */
	private RecognizerDialogListener dialogListener = new RecognizerDialogListener() {

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			userString = text;
			Log.d(TAG, "识别结果：" + userString);

			if (userString.equals("开启机器人")) {
				isBegin = true;
				mSpeechSynthesizer.startSpeaking("智能机器人已开启", mTtsListener);
				isRobot = true;
			} else if (userString.equals("开始骑行")) {
				isBegin = true;
				playMusicIndex((int) (mMusicList.size() * Math.random()));
				new MusicIndexThread(3000).start();
			} else if (userString.equals("我要换歌")) {
				mSpeechSynthesizer.startSpeaking("请说出歌名", mTtsListener);
				new MusicIndexThread(3000).start();
			} else if (userString.equals("瞬时速度")) {
				mSpeechSynthesizer.startSpeaking("您当前时速  30千米每时，继续保持",
						mTtsListener);
			} else if (userString.equals("消耗")) {
				mSpeechSynthesizer.startSpeaking("您消耗了1000大卡，真不错，加油吧",
						mTtsListener);
			} else if (userString.equals("结束骑行")) {
				isSpeeching = true;
				mSpeechSynthesizer.startSpeaking("骑行已结束，请时候要将当前数据上传云端",
						mTtsListener);
				if (isplay) {
					playm.setImageResource(R.drawable.dianji_19);
				}
				musicPlayState(isplay);
			} else if (userString.equals("上传")) {
				mSpeechSynthesizer.startSpeaking("上传成功，欢迎使用！", mTtsListener);
				mRecognizerDialog.cancel();
				isUpload = true;
			} else if (userString.equals("暂停") || userString.equals("停止")
					|| userString.equals("暂停播放") || userString.equals("停止播放")) {
				if (isplay) {
					isplay = false;
					playm.setImageResource(R.drawable.dianji_19);
				}
				musicPlayState(isplay);
				speech_musicIndex();
			} else if (userString.equals("继续") || userString.equals("继续播放")) {
				if (!isplay) {
					isplay = true;
					playm.setImageResource(R.drawable.dianji_20);
					if (isFirst) {
						playMusicIndex(0);
					}
				}
				musicPlayState(isplay);
			} else if (userString.equals("上一曲") || userString.equals("上一首")) {
				nextmusic(0);
			} else if (userString.equals("下一曲") || userString.equals("下一首")) {
				nextmusic(1);
			} else if (isRobot && !TextUtils.isEmpty(userString)) {
				if (userString.equals("关闭机器人")) {
					mSpeechSynthesizer.startSpeaking("智能机器人已关闭", mTtsListener);
					isRobot = false;
					return;
				} else {
					speech_robot();
				}
			} else if (userString.equals("退出程序")) {
				getActivity().finish();
			} else {
				Log.d(TAG, "执行到这");
				speech_musicIndex();
			}
		}

		@Override
		public void onError(SpeechError error) {
			Log.d(TAG, "recognizerDialog onerror:" + error);
			new MusicIndexThread(4000).start();
		}
	};

	private void setParam() {
		mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
		mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE,
				SpeechConstant.TYPE_CLOUD);

		mSpeechRecognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
		mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "10000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "0");
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.pcm");
	}

	/**
	 * 上传自定义词组
	 */
	private void upLoadUserwords() {
		myUserWords = new UserWords();
		mBikeWords = new ArrayList<String>();

		mBikeWords.add("退出程序");

		mBikeWords.add("开始骑行");
		mBikeWords.add("我要换歌");
		mBikeWords.add("瞬时速度");
		mBikeWords.add("消耗");
		mBikeWords.add("结束骑行");
		mBikeWords.add("瞬时速度");

		mBikeWords.add("暂停");
		mBikeWords.add("停止");
		mBikeWords.add("继续");
		mBikeWords.add("播放");
		mBikeWords.add("上一曲");
		mBikeWords.add("下一曲");
		mBikeWords.add("上一首");
		mBikeWords.add("下一首");

		mBikeWords.add("开启");
		mBikeWords.add("关闭");
		mBikeWords.add("智能");
		mBikeWords.add("机器人");

		myUserWords.putWords("bikeWords", mBikeWords);
		myUserWords.putWords("musics", mMusicNameList);

		int ret = mSpeechRecognizer.updateLexicon("userword",
				myUserWords.toString(), lexiconListener);

		if (ret != ErrorCode.SUCCESS) {
			Log.d(TAG, "上传用户词表失败：" + ret);
		}
	}

	// 上传用户词表监听器。
	private LexiconListener lexiconListener = new LexiconListener() {
		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
				Log.d(TAG, error.toString());
			} else {
				Log.d(TAG, "上传成功！");
			}
		}
	};

}
