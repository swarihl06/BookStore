package com.bkpirates.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;

import com.bkpirates.bookstore.R;
import com.bkpirates.entity.BookEntity;
import com.bkpirates.entity.DistributeBookEntity;
import com.bkpirates.webservice.DataLoader;
import com.bkpirates.webservice.DataLoaderListener;
import com.bkpirates.webservice.NetWorkAdmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class InsertNewBooksFragment extends FragmentActivity implements OnClickListener {

	public static final String UPLOAD_URL = "http://thachpn.name.vn/books/add_book.php";
	public static final String UPLOAD_KEY = "image";
	public static final String TAG = "MY MESSAGE";

	private int PICK_IMAGE_REQUEST = 1;
	EditText editName, editQuantity, editAuthor, editPushlier, editGenre, editContent, editPrice, editPriceAdd;

	ImageView image;
	Button btnChoose, btnUpload;
	BookEntity book = new BookEntity();

	private CheckBox checkPushlier;
	private CheckBox checkGenre;
	private Bitmap bitmap;

	int check = 0;

	private Uri filePath;
	private NetWorkAdmin netWorkAdmin = new NetWorkAdmin();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_insert_new_book);
		setWidgets();

		btnChoose.setOnClickListener(this);
		btnUpload.setOnClickListener(this);

	}

	private void setWidgets() {
		editName = (EditText) findViewById(R.id.editName);
		editAuthor = (EditText) findViewById(R.id.editAuthor);
		editContent = (EditText) findViewById(R.id.editContent);
		editPushlier = (EditText) findViewById(R.id.editPushlier);
		editQuantity = (EditText) findViewById(R.id.editQuantity);
		editPrice = (EditText) findViewById(R.id.editPrice);
		editGenre = (EditText) findViewById(R.id.editGenre);
		editPriceAdd = (EditText) findViewById(R.id.editPriceAdd);

		checkGenre = (CheckBox) findViewById(R.id.checkNewGenre);
		checkPushlier = (CheckBox) findViewById(R.id.checkNewPushlier);

		btnChoose = (Button) findViewById(R.id.btnChoose);
		btnUpload = (Button) findViewById(R.id.btnUpload);

		image = (ImageView) findViewById(R.id.imageView);
	}

	@Override
	public void onClick(View v) {
		if (v == btnChoose) {
			showFileChooser();
		}
		if (v == btnUpload) {
			book.setAuthor(editAuthor.getText() + "");
			book.setContent(editContent.getText() + "");
			book.setName(editName.getText() + "");
			book.setPrice(Integer.parseInt(editPrice.getText() + ""));
			book.setPulisher(editPushlier.getText() + "");
			book.setGenre(editGenre.getText() + "");
//			if(!checkPushlier.isChecked())
//				book.setPulisher(editPushlier.getText() + "");
//			else netWorkAdmin.setNewPushlier(editPushlier.getText() + "");
//			if(!checkGenre.isChecked())
//				book.setGenre(editGenre.getText() + "");
//			else netWorkAdmin.setNewGenre(editGenre.getText() + "");
			book.setPrice_add(Integer.parseInt(editPriceAdd.getText() + ""));
			book.setQuantity(Integer.parseInt(editQuantity.getText() + ""));
			netWorkAdmin.setBookEntity(book);
			uploadImage();
		}
	}

	private boolean checkInteger(int a, int b, int c) {
		if (a <= 0 || b <= 0 || c <= 0) {
			Toast.makeText(InsertNewBooksFragment.this, "Please check quantity or price_add or price",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void showFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	}

	private void uploadImage() {
		String uploadImage = getStringImage(bitmap);
		netWorkAdmin.setEncodedImage(uploadImage);
		Log.d(TAG + " Son ", netWorkAdmin.getEncodedImage());
		GetUserBooksAsyncTask order = (GetUserBooksAsyncTask) new GetUserBooksAsyncTask().execute(UPLOAD_URL);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

			filePath = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				image.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	private class GetUserBooksAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pb;

		@Override
		protected void onPreExecute() {
			pb = new ProgressDialog(InsertNewBooksFragment.this);
			pb.setMessage("Uploading...");
			pb.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String s) {
			if (pb != null) {
				pb.dismiss();
			}
			if (s != null) {
				check = netWorkAdmin.check(s);
				Log.d(TAG, check + "");
				if (check == 1) {

					Toast.makeText(InsertNewBooksFragment.this, " Upload success", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(InsertNewBooksFragment.this, " Upload fail", Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(s);
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			HttpResponse response;
			Log.d(TAG, "ssssssssssssssssssssssssssssssssss");
			try {
				Log.d(TAG, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
				response = netWorkAdmin.makeRequestUpload(url);
			} catch (IOException e) {
				return null;
			}
			Log.d(TAG, "?????????????");
			if (response != null) {

				String content = null;
				try {
					Log.d(TAG, "ddmmm");
					content = netWorkAdmin.processHTTPResponce(response);
					Log.d(TAG + TAG, "ddmmm");
					return content;
				} catch (IOException e) {
					return null;
				} catch (ParseException e) {
					return null;
				}
			}
			else {
				Log.d(TAG, "deo hieu sao lai nhu the");
			}
			return null;

		}

	}

}
