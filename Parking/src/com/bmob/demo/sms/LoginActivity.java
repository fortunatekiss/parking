package com.bmob.demo.sms;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.bmob.demo.sms.bean.User;
import com.parking.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


/**  
 * ��¼ 
 * @class  LoginActivity  
 * @author smile   
 * @date   2015-6-5 ����11:16:04  
 *   
 */
public class LoginActivity extends BaseActivity{

	String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	Tencent mTencent;
	IUiListener tencentLoginListener;
	SsoHandler mSsoHandler;
	//private static final String BMOB_APP_KEY="19872817e4d9a4070addbbb47c9564f5";
	EditText et_account;
	EditText et_password;
	Button btn_login;
	ImageView iv_left;
	ImageView qqView;
	//Button btn_onekey;
	Button btn_register;
    ImageView weiboView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	    et_account=(EditText) findViewById(R.id.et_account);
	    et_password=(EditText) findViewById(R.id.et_password);
		btn_login=(Button) findViewById(R.id.btn_login);
 	 //   btn_onekey=(Button)findViewById(R.id.btn_onekey);
	    btn_register=(Button)findViewById(R.id.btn_register);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setVisibility(View.VISIBLE);
		weiboView=(ImageView) findViewById(R.id.weiboView);
		qqView=(ImageView) findViewById(R.id.qqView);
		qqView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				 qqAuthorize();
			}
			
		});
	
		weiboView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				 weibo();
			}
			
		});
		
		iv_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}	
		});
	 
	    btn_register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
	    	 	
	    });
	  /*  btn_onekey.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(LoginActivity.this,LoginOneKeyActivity.class);
				startActivity(intent);
			}
		
	    });*/
		btn_login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				login();
			}
			
		});
	}
	
	 private void weibo() {
			// TODO �Զ����ɵķ������
	    	AuthInfo mAuthInfo=new AuthInfo(LoginActivity.this,"3380356994" ,"https://api.weibo.com/oauth2/default.html",SCOPE);
	    	mSsoHandler=new SsoHandler(LoginActivity.this,mAuthInfo);
	    	mSsoHandler.authorize(new WeiboAuthListener(){

				@Override
				public void onCancel() {
					// TODO �Զ����ɵķ������
					  Toast.makeText(LoginActivity.this, "ʧ��", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onComplete(Bundle arg0) {
					// TODO �Զ����ɵķ������
					Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(arg0);
		            if (accessToken != null && accessToken.isSessionValid()) {
		            	//����Bmob�ṩ����Ȩ��¼��������΢����½����¼�ɹ�����Ϳ����ں�̨��������User���п���΢����½����û���
		            	String token = accessToken.getToken();
			            String expires = String.valueOf(accessToken.getExpiresTime());
			            String uid = accessToken.getUid();
						BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_WEIBO,token, expires,uid);
						Toast.makeText(LoginActivity.this, "�ɹ�", Toast.LENGTH_LONG).show();
						loginWithAuth(authInfo);
		            }
				}

				@Override
				public void onWeiboException(WeiboException arg0) {
					// TODO �Զ����ɵķ������
					  Toast.makeText(LoginActivity.this, "ʧ��", Toast.LENGTH_LONG).show();
				}
	    		
	    	});
			
		}
		public void loginWithAuth(final BmobThirdUserAuth authInfo){
	    	 BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
				@Override
				public void done(JSONObject arg0, BmobException arg1) {
					// TODO �Զ����ɵķ������
				
				}
	    	 });
		}
		
	    private void qqAuthorize(){
	    	if(mTencent==null){
				mTencent = Tencent.createInstance("1105994375", getApplicationContext());
				
			}
			mTencent.logout(this);
		
			tencentLoginListener= new IUiListener() {
				
				@Override
				public void onComplete(Object arg0) {
					// TODO Auto-generated method stub
				
					if(arg0!=null){
						JSONObject jsonObject = (JSONObject) arg0;
						try {
							String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
							String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
							String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
							BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
							loginWithAuth(authInfo);
						} catch (JSONException e) {
						}
					}			 
				  Toast.makeText(LoginActivity.this, "�ɹ�", Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onCancel() {
				  
				}

				@Override
				public void onError(UiError arg0) {
					// TODO �Զ����ɵķ������
					
				}
			
			};
			 mTencent.login(this, "all",  tencentLoginListener);
		}
		
	/** ��½���� 
	 * @method login    
	 * @return void  
	 * @exception   
	 */
	private void login(){
		String account = et_account.getText().toString();
		String password = et_password.getText().toString();
		if (TextUtils.isEmpty(account)) {
			showToast("�˺Ų���Ϊ��");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			showToast("���벻��Ϊ��");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("���ڵ�¼��...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		//V3.3.9�ṩ���µĵ�¼��ʽ���ɴ��û���/����/�ֻ�����
		BmobUser.loginByAccount(account, password, new LogInListener<User>() {
			@Override
			public void done(User user, BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					toast("��¼�ɹ�---�û�����"+user.getUsername());
					Intent intent = new Intent(LoginActivity.this,LoginMainActivity.class);
					intent.putExtra("from", "login");
					startActivity(intent);
					finish();
				}else{
					toast("��¼ʧ�ܣ�code="+ex.getErrorCode()+"������������"+ex.getLocalizedMessage());
				}
			}
		});
	}
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data,tencentLoginListener);
    	super.onActivityResult(requestCode, resultCode, data);
         if(mSsoHandler!=null){
        	 mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
         }
    }
}
