package com.bkpirates.fragment;

import java.io.IOException;
import java.text.ParseException;

import org.apache.http.HttpResponse;

import com.bkpirates.bookstore.R;
import com.bkpirates.entity.BookEntity;
import com.bkpirates.webservice.GetBookData;
import com.bkpirates.webservice.GetBookData.GetBookDataListener;
import com.bkpirates.webservice.NetWork;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class BookFragment extends Fragment implements GetBookDataListener {

	int check = 0;
//	private Context context;
	private BookEntity book;
	private int numberBookToBuy = 1;
	private LinearLayout btnBuy;
	private ImageView btnLike;
	TextView number;
	TextView content, status, author, pulisher;
	TextView numberFavorite;
	private final String TAG = "BookFragment";
	
	private int tempLike;

	NetWork netWork = new NetWork();
	private final String ADD_CART = "http://thachpn.name.vn/books/add_cart.php";
	private final String ADD_FAVORITE = "http://thachpn.name.vn/books/add_favorite_book.php";
	private final String DELETE_FAVORITE = "http://thachpn.name.vn/books/delete_favorite_book.php";

	public BookFragment(Context context, BookEntity book) {
//		this.content = content;
		this.book = new BookEntity();
		this.book.setBid(book.getBid());
		this.book.setName(book.getName());
		this.book.setAuthor(book.getAuthor());
		this.book.setPrice(book.getPrice());
		this.book.setLinkImage(book.getLinkImage());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_book, container, false);

		TextView increase_button = (TextView) view.findViewById(R.id.increase_button);
		number = (TextView) view.findViewById(R.id.number);
		TextView decrease_button = (TextView) view.findViewById(R.id.decrease_button);
		btnBuy = (LinearLayout) view.findViewById(R.id.buy_button);
		btnLike = (ImageView) view.findViewById(R.id.like_button);
		numberFavorite = (TextView) view.findViewById(R.id.number_favorite);
		number.setText(Integer.toString(numberBookToBuy));

		increase_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (book.getQuantity() <= numberBookToBuy) {

					AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					dialog.setTitle("Lỗi");
					dialog.setMessage("Số sách hiện có không đủ!");
					dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});

					dialog.setCancelable(false);
					dialog.create();
					dialog.show();

				} else {
					number.setText(Integer.toString(++numberBookToBuy));
				}
			}
		});
		decrease_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (numberBookToBuy > 1)
					number.setText(Integer.toString(--numberBookToBuy));
			}
		});

		ImageView image = (ImageView) view.findViewById(R.id.image);
		ImageLoader.getInstance().displayImage(book.getLinkImage(), image);

		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(book.getName());

		TextView price = (TextView) view.findViewById(R.id.price);
		price.setText(Integer.toString(book.getPrice()) + " Đ");

		GetBookData getBook = new GetBookData();
		getBook.listener = this;
		getBook.execute(book);

		status = (TextView) view.findViewById(R.id.status);
		status.setText("Status: ");

		content = (TextView) view.findViewById(R.id.content);
		author = (TextView) view.findViewById(R.id.author);
		pulisher = (TextView) view.findViewById(R.id.pulisher);
		// TextView numberFavorite = (TextView)
		// view.findViewById(R.id.number_favorite);
		// numberFavorite.setText("("+book.get);

	
		btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				if (LoginFragment.checkLogin == 1) {

					netWork.setBookEntity(book);
					netWork.setPhone(LoginFragment.accEntity.getPhone());
					netWork.setNumberBookToBuy(numberBookToBuy);
					if (book.getQuantity() > 0) {

						AddToCartAndFavoriteListAsyncTask add = (AddToCartAndFavoriteListAsyncTask) new AddToCartAndFavoriteListAsyncTask()
								.execute(ADD_CART);
					} else {
						Log.d(TAG, TAG);
						AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
						dialog.setTitle("Lỗi");
						dialog.setMessage("Số sách hiện có không đủ!");
						dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

							}
						});

						dialog.setCancelable(false);
						dialog.create();
						dialog.show();

					}
				} else {
					Toast.makeText(getActivity(), "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (LoginFragment.checkLogin == 1) {
					Log.d(TAG, book.getLike() + "");
					if (tempLike == 0) {

						btnLike.setImageResource(R.drawable.like);
						int temp = book.getLikedPersonNumber();
						book.setLikedPersonNumber(++temp);
						numberFavorite.setText(temp + "");
						netWork.setBookEntity(book);
						netWork.setPhone(LoginFragment.accEntity.getPhone());
						AddToCartAndFavoriteListAsyncTask add = (AddToCartAndFavoriteListAsyncTask) new AddToCartAndFavoriteListAsyncTask()
								.execute(ADD_FAVORITE);
						tempLike = 1;
					} else {
						btnLike.setImageResource(R.drawable.unlike);
						int temp = book.getLikedPersonNumber();
						book.setLikedPersonNumber(--temp);
						numberFavorite.setText(temp + "");
						netWork.setBookEntity(book);
						netWork.setPhone(LoginFragment.accEntity.getPhone());
						AddToCartAndFavoriteListAsyncTask add = (AddToCartAndFavoriteListAsyncTask) new AddToCartAndFavoriteListAsyncTask()
								.execute(DELETE_FAVORITE);
						tempLike = 0;
					}

				} else {
					Toast.makeText(getActivity(), "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_LONG)
					.show();
				}
			}
		});
		return view;
	}

	@Override
	public void onDownloadSuccess() {
		// TODO Auto-generated method stub

		pulisher.setText(book.getPulisher());
		author.setText(book.getAuthor());
		if (book.getQuantity() > 0) {
			status.setText("Tình trạng: Còn hàng.");
		} else {
			status.setText("Tình trạng: Hết hàng.");
		}
		numberFavorite.setText(Integer.toString(book.getLikedPersonNumber()));
		if (book.getLike() == 1) {
			btnLike.setImageResource(R.drawable.like);
		}
		tempLike = book.getLike();

		content.setText(book.getContent());
		makeTextViewResizable(content, 3, "Xem thêm", true);
	}

	public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText,
			final boolean viewMore) {

		if (tv.getTag() == null) {
			tv.setTag(tv.getText());
		}
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {

				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (maxLine == 0) {
					int lineEndIndex = tv.getLayout().getLineEnd(0);
					String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " "
							+ expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine,
							expandText, viewMore), BufferType.SPANNABLE);
				} else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
					int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
					String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " "
							+ expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine,
							expandText, viewMore), BufferType.SPANNABLE);
				} else {
					int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
					String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv,
							lineEndIndex, expandText, viewMore), BufferType.SPANNABLE);
				}
			}
		});

	}

	private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
			final int maxLine, final String spanableText, final boolean viewMore) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(spanableText)) {
			ssb.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {

					if (viewMore) {
						tv.setLayoutParams(tv.getLayoutParams());
						tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
						tv.invalidate();
						makeTextViewResizable(tv, -1, "Thu gọn", false);
					} else {
						tv.setLayoutParams(tv.getLayoutParams());
						tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
						tv.invalidate();
						makeTextViewResizable(tv, 3, "Xem thêm", true);
					}

				}
			}, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

		}
		return ssb;
	}
	public class AddToCartAndFavoriteListAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pb;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String s) {
			if (s != null) {
				check = netWork.checkForAddCartAndFavoriteList(s);
				if (check == 1)
					Toast.makeText(getActivity(), "Thành công!", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity(), "Lỗi khi thêm sách!" , Toast.LENGTH_LONG).show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Lỗi");
				builder.setMessage("Vui lòng kết nối mạng!");
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

			super.onPostExecute(s);
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			HttpResponse response = null;
			if (url.equals(ADD_CART)) {
				try {
					response = netWork.makeRquestAddToCart(url);
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
			} else if (url.equals(ADD_FAVORITE) || url.equals(DELETE_FAVORITE)) {
				try {
					response = netWork.makeRquestAddFavoriteList(url);
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

			}
			

			return null;
		}
	}
}