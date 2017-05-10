package com.bmob.demo.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

import com.bmob.demo.sms.bean.User;
import com.parking.R;

/**
 * һ����¼
 * 
 * @class LoginPhoneActivity
 * @author smile
 * @date 2015-6-5 ����11:23:44
 * 
 */
public class LoginOneKeyActivity extends BaseActivity {

	MyCountTimer timer;
	ImageView iv_left;
	TextView tv_title;
	EditText et_phone;
	EditText et_code;
	Button btn_send;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_onekey);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_code=(EditText) findViewById(R.id.et_verify_code);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}	
		});
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("�ֻ�����һ����¼");
		btn_login=(Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				oneKeyLogin();
			}
		});
	
		btn_send=(Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				requestSMSCode();
			}
			
		});
	}

	class MyCountTimer extends CountDownTimer{  
		  
        public MyCountTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
		@Override  
        public void onTick(long millisUntilFinished) {  
            btn_send.setText((millisUntilFinished / 1000) +"����ط�");  
        }  
        @Override  
        public void onFinish() {  
        	btn_send.setText("���·�����֤��");  
        }  
    }  
	
	private void requestSMSCode() {
		String number = et_phone.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);   
			timer.start();   
			BmobSMS.requestSMSCode( number,"һ��ע����¼ģ��", new QueryListener<Integer>() {

				public void done(Integer smsId, BmobException ex) {
					// TODO Auto-generated method stub
					if (ex == null) {// ��֤�뷢�ͳɹ�
						toast("��֤�뷢�ͳɹ�");// ���ڲ�ѯ���ζ��ŷ�������
					}else{
						timer.cancel();
					}
				}
			});
		} else {
			toast("�������ֻ�����");
		}
	}

	/**
	 * һ����¼����
	 * 
	 * @method login
	 * @return void
	 * @exception
	 */
	private void oneKeyLogin() {
		final String phone = et_phone.getText().toString();
		final String code = et_code.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			showToast("�ֻ����벻��Ϊ��");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			showToast("��֤�벻��Ϊ��");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginOneKeyActivity.this);
		progress.setMessage("������֤������֤��...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9�ṩ��һ��ע����¼��ʽ���ɴ��ֻ��������֤��
		BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<User>() {
			@Override
			public void done(User user, BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					toast("��¼�ɹ�");
					Intent intent = new Intent(LoginOneKeyActivity.this,LoginMainActivity.class);
					intent.putExtra("from", "loginonekey");
					startActivity(intent);
					finish();
				}else{
					toast("��¼ʧ�ܣ�code="+ex.getErrorCode()+"������������"+ex.getLocalizedMessage());
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
}
