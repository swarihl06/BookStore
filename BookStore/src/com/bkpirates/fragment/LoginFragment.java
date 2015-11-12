package com.bkpirates.fragment;

import java.io.IOException;
import java.text.ParseException;

import org.apache.http.HttpResponse;

import com.bkpirates.bookstore.R;
import com.bkpirates.entity.AccountEntity;
import com.bkpirates.webservice.NetWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("NewApi")
public class LoginFragment extends Fragment {

	private Button signIn;
	private Button crtAccount;
	private EditText phoneNumber;
	private EditText passWord;
	private String phone;
	private String pass;
	NetWork netWork = new NetWork();
	private int check = 0;
	public static AccountEntity accEntity = new AccountEntity();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, null);
		signIn = (Button) view.findViewById(R.id.btnSignIn);
		crtAccount = (Button) view.findViewById(R.id.btnCreateAccount);
		phoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
		passWord = (EditText) view.findViewById(R.id.passWord);
		// phoneNumber.setHintTextColor(getResources().getColor(R.color.white));
		signIn.setEnabled(false);

		phoneNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (phoneNumber.getText().toString().equals("") == false
						&& passWord.getText().toString().equals("") == false) {
					signIn.setEnabled(true);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		passWord.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (phoneNumber.getText().toString().equals("") == false
						&& passWord.getText().toString().equals("") == false) {
					signIn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				phone = phoneNumber.getText().toString();
				pass = passWord.getText().toString();
				netWork.setPhone(phone);
				netWork.setPass(pass);
				if (netWork.checkInternetConnect(getActivity()))
				{
					NetWorkAsyncTask nw = (NetWorkAsyncTask) new NetWorkAsyncTask().execute("http://thachpn.name.vn/books/check_account.php");
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
					dialog.setTitle(" Error").setCancelable(false)
							.setMessage("Not connected with Internet")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					dialog.create().show();

				}
			}
		});
		crtAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				CrtAccountFragment caf = new CrtAccountFragment();
				ft.replace(R.id.container, caf);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		SharedPreferences pre = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
		phoneNumber.setText(pre.getString("phone", ""));
		passWord.setText(pre.getString("pass", ""));
		super.onResume();
	}

	@Override
	public void onPause() {
		SharedPreferences pre = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString("phone", phoneNumber.getText().toString());
		editor.putString("pass", passWord.getText().toString());
		editor.commit();
		super.onPause();
	}

	public class NetWorkAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pb;

		@Override
		protected void onPreExecute() {
			pb = new ProgressDialog(getActivity());
			pb.setMessage("Login...");
			pb.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String s) {
			if (pb != null) {
				pb.dismiss();
			}
			if (s != null) {
				accEntity = netWork.checkAccountForLogin(s);
				check = Integer.parseInt(accEntity.getPassword());
				if (check == 1) {
					accEntity.setPhone(phone);
					accEntity.setPassword(pass);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					AccountFragment fragment = new AccountFragment();
					ft.replace(R.id.container, fragment);
					// ft.addToBackStack(null);
					ft.commit();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Fail");
					builder.setMessage("Invalid login or password @@");
					builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					AlertDialog dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
				}
			}
			super.onPostExecute(s);
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			HttpResponse response = null;
			try {
				response = netWork.makeRquestLogin(url);
			} catch (IOException e) {
				return null;
			}
			if (response != null) {
				String content = null;
				try {
					content = netWork.processHTTPResponce(response);
					return content;
				} catch (IOException e) {
					return null;
				} catch (ParseException e) {
					return null;
				}
			}
			return null;

		}
	}

}
