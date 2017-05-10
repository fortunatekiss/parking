package com.bmob.demo.sms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.demo.sms.bean.User;
import com.parking.R;

/**  
 * ���ֻ���  
 * @class  UserBindPhoneActivity  
 * @author smile   
 * @date   2015-6-5 ����3:08:53  
 *   
 */
public class UserBindPhoneActivity extends BaseActivity {
	
	ImageView iv_left;
	EditText et_number;
	EditText et_input;
	TextView tv_title;
	TextView tv_send;
	TextView tv_bind;
	MyCountTimer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}	
		});
		 tv_bind=(TextView) findViewById(R.id.tv_bind);  
		 tv_bind.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					verifyOrBind();
				}
		    	 	
		    });
		 tv_send=(TextView) findViewById(R.id.tv_send);  
		 tv_send.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					requestSMSCode();
				}
		    	 	
		    });
	    et_input=(EditText) findViewById(R.id.et_input);
		et_input=(EditText) findViewById(R.id.et_input);
		et_number=(EditText) findViewById(R.id.et_number);
		iv_left.setVisibility(View.VISIBLE);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("���ֻ���");
	}
	
	class MyCountTimer extends CountDownTimer{  
		  
        public MyCountTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
		@Override  
        public void onTick(long millisUntilFinished) {  
			tv_send.setText((millisUntilFinished / 1000) +"����ط�");  
        }  
        @Override  
        public void onFinish() {  
        	tv_send.setText("���·�����֤��");  
        }  
    }  
		
	private void requestSMSCode() {
		String number = et_number.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);   
			timer.start();
			BmobSMS.requestSMSCode(number, "�ֻ������½ģ��",new QueryListener<Integer>() {
				@Override
				public void done(Integer arg0, BmobException arg1) {
					// TODO �Զ����ɵķ������
					// TODO �Զ����ɵķ������
					// TODO Auto-generated method stub
					if (arg1 == null) {// ��֤�뷢�ͳɹ�
						toast("��֤�뷢�ͳɹ�");// ���ڲ�ѯ���ζ��ŷ�������
					}else{//�����֤�뷢�ʹ��󣬿�ֹͣ��ʱ
						timer.cancel();
					}
				}
			});
		} else {
			toast("�������ֻ�����");
		}
	}
	private void verifyOrBind(){
		final String phone = et_number.getText().toString();
		String code = et_input.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			showToast("�ֻ����벻��Ϊ��");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			showToast("��֤�벻��Ϊ��");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("������֤������֤��...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9�ṩ��һ��ע����¼��ʽ���ɴ��ֻ��������֤��
		BmobSMS.verifySmsCode(phone, code, new UpdateListener() {		
			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					toast("�ֻ���������֤");
					bindMobilePhone(phone);
				}else{
					toast("��֤ʧ�ܣ�code="+ex.getErrorCode()+"������������"+ex.getLocalizedMessage());
				}
			}
		});
	}
	
	private void bindMobilePhone(String phone){
		//�������ڸ��û����ֻ������ʱ����Ҫ�ύ�����ֶε�ֵ��mobilePhoneNumber��mobilePhoneNumberVerified
		User user =new User();
		user.setMobilePhoneNumber(phone);
		user.setMobilePhoneNumberVerified(true);
		User cur = BmobUser.getCurrentUser(User.class);
		user.update( cur.getObjectId(),new UpdateListener() {
			@Override
			public void done(BmobException arg0) {
				// TODO �Զ����ɵķ������
				toast("�ֻ�����󶨳ɹ�");
				finish();
			}
		});
	}
	
}
