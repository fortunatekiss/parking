package com.bmob.demo.sms;

import com.parking.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * ��������
 * @class ResetPasswordActivity
 * @author smile
 * @date 2015-6-5 ����11:23:44
 * 
 */
public class ResetPasswordActivity extends BaseActivity {

	MyCountTimer timer;
	ImageView iv_left;
	TextView tv_title;
	EditText et_phone;
	EditText et_code;
	Button btn_send;
	EditText et_pwd;
	Button btn_reset;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}	
		});
		btn_send=(Button)findViewById(R.id.btn_send);
	    btn_send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				requestSMSCode();
			}
	    	 	
	    });
		btn_reset=(Button)findViewById(R.id.btn_reset);
	    btn_reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				resetPwd();
			}
	    	 	
	    });
		et_code=(EditText) findViewById(R.id.et_verify_code);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		iv_left.setVisibility(View.VISIBLE);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("��������");
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
			BmobSMS.requestSMSCode(number, "��������ģ��",new QueryListener<Integer>() {
				@Override
				public void done(Integer arg0, BmobException arg1) {
					// TODO �Զ����ɵķ������
					if (arg1== null) {// ��֤�뷢�ͳɹ�
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
	/**
	 */
	private void resetPwd() {
		final String code = et_code.getText().toString();
		final String pwd = et_pwd.getText().toString();
		if (TextUtils.isEmpty(code)) {
			showToast("��֤�벻��Ϊ��");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			showToast("���벻��Ϊ��");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(ResetPasswordActivity.this);
		progress.setMessage("������������...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9�ṩ���������빦�ܣ�ֻ��Ҫ������֤��������뼴��
		BmobUser.resetPasswordBySMSCode(code, pwd, new UpdateListener() {
			
			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					toast("�������óɹ�");
					finish();
				}else{
					toast("��������ʧ�ܣ�code="+ex.getErrorCode()+"������������"+ex.getLocalizedMessage());
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
