package frag.victor.com;import android.content.Context;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import android.widget.Toast;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.List;import java.util.Observable;import java.util.Observer;import adapter.victor.com.GifAdapter;import data.victor.com.GifContentData;import data.victor.com.GifData;import flowfunny.victor.com.R;import mode.victor.com.DataObservable;import util.victor.com.Constant;import util.victor.com.DateUtil;import util.victor.com.HttpRequestHelper;import view.victor.com.CircularProgress;import view.victor.com.XListView;public class GifFrag extends Fragment implements Observer,XListView.IXListViewListener{	private String TAG = "GifFrag";	private TextView mTvName;	private CircularProgress mCpProgress;	private XListView mLvGif;	private GifAdapter mGifAdapter;	private List<GifContentData> gifConnentDatas = new ArrayList<GifContentData>();	private HttpRequestHelper mHttpRequestHelper;	private  int currentPage = 1;//当前页数	private int countPages;//总页数	private Context mContext;	Handler mHandler = new Handler(){		@Override		public void handleMessage(Message msg) {			switch (msg.what){				case Constant.Msg.REQUEST_SUCCESS:					mLvGif.setPullLoadEnable(true);					mCpProgress.setVisibility(View.GONE);					gifConnentDatas.addAll((List<GifContentData>) msg.obj);					mGifAdapter.setGifConnentDatas(gifConnentDatas);					mGifAdapter.notifyDataSetChanged();					mLvGif.setRefreshTime(DateUtil.getTodayTime());					if(currentPage == countPages){						mLvGif.setPullLoadEnable(false);					} else {						mLvGif.setPullLoadEnable(true);					}					break;				case Constant.Msg.REQUEST_SUCCESS_NO_DATA:					mCpProgress.setVisibility(View.GONE);					if (currentPage == 1) {						gifConnentDatas.clear();						mLvGif.setPullLoadEnable(false);						mGifAdapter.notifyDataSetChanged();					}					if (mContext != null) {						Toast.makeText(mContext, "服务器没有数据！", Toast.LENGTH_SHORT).show();					}					break;				case Constant.Msg.REQUEST_FAILED:					mCpProgress.setVisibility(View.GONE);					if (currentPage == 1) {						gifConnentDatas.clear();						mLvGif.setPullLoadEnable(false);						mGifAdapter.notifyDataSetChanged();					}					if (mContext != null) {						Toast.makeText(mContext,"访问服务器失败！",Toast.LENGTH_SHORT).show();					}					break;				case Constant.Msg.PARSING_EXCEPTION:					mCpProgress.setVisibility(View.GONE);					if (currentPage == 1) {						gifConnentDatas.clear();						mLvGif.setPullLoadEnable(false);						mGifAdapter.notifyDataSetChanged();					}					if (mContext != null) {						Toast.makeText(mContext,"数据解析异常！",Toast.LENGTH_SHORT).show();					}					break;				case Constant.Msg.NETWORK_ERROR:					mCpProgress.setVisibility(View.GONE);					if (currentPage == 1) {						gifConnentDatas.clear();						mLvGif.setPullLoadEnable(false);						mGifAdapter.notifyDataSetChanged();					}					if (mContext != null) {						Toast.makeText(mContext,"网络错误，请检查网络是否连接！",Toast.LENGTH_SHORT).show();					}					break;				case Constant.Msg.SOCKET_TIME_OUT:					mCpProgress.setVisibility(View.GONE);					if (currentPage == 1) {						gifConnentDatas.clear();						mLvGif.setPullLoadEnable(false);						mGifAdapter.notifyDataSetChanged();					}					if (mContext != null) {						Toast.makeText(mContext,"访问服务器超时，请重试！",Toast.LENGTH_SHORT).show();					}					break;			}		}	};	@Override	public void onActivityCreated(@Nullable Bundle savedInstanceState) {		super.onActivityCreated(savedInstanceState);		mContext = getContext();	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {		View view = inflater.inflate(R.layout.frag_gif,container, false);		initialize(view);		DataObservable.getInstance().addObserver(this);		return view;	}	private void initialize (View view) {		mCpProgress = (CircularProgress) view.findViewById(R.id.cp_progress);		mLvGif = (XListView) view.findViewById(R.id.lv_gif_frag);		mLvGif.setPullLoadEnable(false);		mLvGif.setXListViewListener(this);		mGifAdapter = new GifAdapter(getContext(),mLvGif);		mGifAdapter.setGifConnentDatas(gifConnentDatas);		mLvGif.setAdapter(mGifAdapter);		mHttpRequestHelper = new HttpRequestHelper(getContext());		requestGifDatas();	}	private void onLoad() {		mLvGif.stopRefresh();		mLvGif.stopLoadMore();		mLvGif.setRefreshTime(DateUtil.getTodayTime());	}	@Override	public void onRefresh() {		gifConnentDatas.clear();		currentPage --;		if (currentPage < 1){			currentPage = 1;		}		requestGifDatas();;		onLoad();	}	@Override	public void onLoadMore() {		gifConnentDatas.clear();		currentPage ++;		requestGifDatas();		onLoad();	}	private void requestGifDatas () {		if (mHttpRequestHelper != null) {			mCpProgress.setVisibility(View.VISIBLE);			SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);			String time = sdf.format(new Date());			String requestUrl = String.format(Constant.BUDEJIE_URL,currentPage,Constant.Action.GIF_ACTION,Constant.APP_ID,Constant.APP_SECRET,time);			mHttpRequestHelper.sendRequestWithParms(Constant.Msg.GIF_REQUEST, requestUrl);		}	}	@Override	public void onDestroy() {		if (mHttpRequestHelper != null) {			mHttpRequestHelper.onDestroy();		}		super.onDestroy();	}	@Override	public void update(Observable observable, Object data) {		Log.e(TAG, "update()......");		if (data instanceof GifData) {			GifData gifData = (GifData) data;			int status = gifData.getStatus();			Log.e(TAG, "update()......status=" + status);			Message msg = new Message();			switch (status) {				case Constant.Msg.REQUEST_SUCCESS:					msg.what = Constant.Msg.REQUEST_SUCCESS;					msg.obj = gifData.getGifConnentDatas();					countPages = gifData.getAllPages();					break;				case Constant.Msg.REQUEST_SUCCESS_NO_DATA:					msg.what = Constant.Msg.REQUEST_SUCCESS_NO_DATA;					break;				case Constant.Msg.REQUEST_FAILED:					msg.what = Constant.Msg.REQUEST_FAILED;					break;				case Constant.Msg.PARSING_EXCEPTION:					msg.what = Constant.Msg.PARSING_EXCEPTION;					break;				case Constant.Msg.NETWORK_ERROR:					msg.what = Constant.Msg.NETWORK_ERROR;					break;				case Constant.Msg.SOCKET_TIME_OUT:					msg.what = Constant.Msg.SOCKET_TIME_OUT;					break;			}			mHandler.sendMessage(msg);		}	}}