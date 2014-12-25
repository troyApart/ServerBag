package com.troyApart.serverbag;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class ServerBag_HomeFragment extends Fragment {
	public static final String ARG_LIST_NUMBER = "list_number";
	private ServerBag_Database_Adapter sbDbAdapter;
	private ListView recentList;
	private ArrayAdapter<ServerCheck> recentListAdapter;
	private ArrayList<ServerCheck> serverCheckList;
	private DatePicker datePicker;
	private EditText checkCash;
	private EditText checkCredit;
	private EditText tipCash;
	private EditText tipCredit;
	private EditText textNote;
	private Button recordCheck, todayButton;
	private View rootView;

	public ServerBag_HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		int i = getArguments().getInt(ARG_LIST_NUMBER);
		String title = getResources().getStringArray(R.array.drawer_list)[i];

		sbDbAdapter = new ServerBag_Database_Adapter(getActivity().getBaseContext());
		recentList = (ListView) rootView.findViewById(R.id.listView0);
		
		
		initializeCalendar();
		initializeData();
		setListeners();
		setEditTextFocusDown();
		getActivity().setTitle(title);
		return rootView;
	}

	private void initializeCalendar() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int monthOfYear = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
		datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					Calendar c = Calendar.getInstance();
					int y = c.get(Calendar.YEAR);
					int m = c.get(Calendar.MONTH);
					int d = c.get(Calendar.DAY_OF_MONTH);
					if (year == y && monthOfYear == m && dayOfMonth == d) {
						todayButton.setEnabled(false);
					} else {
						todayButton.setEnabled(true);
					}
				}
		});
	}
	
	public void initializeData() {
		serverCheckList = new ArrayList<ServerCheck>();
		sbDbAdapter.openReadableDatabase();
		Cursor cursor = sbDbAdapter.getAllChecksCursor();
		if (cursor.moveToLast() != false) {
			int idIndex, id, dateIndex, isPMIndex, cCashIndex, cCreditIndex, tCashIndex, tCreditIndex, noteIndex;
			boolean isPM;
			String date, note;
			Double cCash, cCredit, tCash, tCredit;
			Double totalCash = 0.00;
			Double totalCredit = 0.00;
			Double totalTipCash = 0.00;
			Double totalTipCredit = 0.00;

			do {
				idIndex = cursor.getColumnIndex("ID");
				id = cursor.getInt(idIndex);
				dateIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_DATE);
				date = cursor.getString(dateIndex);
				isPMIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_IS_PM);
				if (cursor.getInt(isPMIndex) == 1) {
					isPM = true;
				} else {
					isPM = false;
				}
				cCashIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_CHECK_CASH);
				cCash = cursor.getDouble(cCashIndex);
				totalCash += cCash;
				cCreditIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_CHECK_CREDIT);
				cCredit = cursor.getDouble(cCreditIndex);
				totalCredit += cCredit;
				tCashIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_TIP_CASH);
				tCash = cursor.getDouble(tCashIndex);
				totalTipCash += tCash;
				tCreditIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_TIP_CREDIT);
				tCredit = cursor.getDouble(tCreditIndex);
				totalTipCredit += tCredit;
				noteIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_NOTE);
				note = cursor.getString(noteIndex);
				serverCheckList.add(new ServerCheck(id, date, cCash, cCredit, tCash, tCredit, isPM, note));
			} while (cursor.moveToPrevious());
			cursor.close();
			sbDbAdapter.close();
			updateListView();
		} else {
		}
	}

	public void updateListView() {
		recentListAdapter = new ArrayAdapter<ServerCheck>(
				rootView.getContext(), android.R.layout.simple_list_item_1,
				serverCheckList);
		recentList.setAdapter(recentListAdapter);
		recentListAdapter.notifyDataSetChanged();
	}

	public void clearServerCheckList() {
		serverCheckList.clear();
		updateListView();
	}

	public void setListeners() {
		checkCash = (EditText) rootView.findViewById(R.id.editCheckAmountCash);
		checkCredit = (EditText) rootView.findViewById(R.id.editCheckAmountCredit);
		tipCash = (EditText) rootView.findViewById(R.id.editTipCash);
		tipCredit = (EditText) rootView.findViewById(R.id.editTipCredit);
		textNote = (EditText) rootView.findViewById(R.id.editTextNote);
		setOnTouchListeners();
		// setNumberToCurrency();
		recentList.setOnItemClickListener(new OnItemClickListener() {
			private View alertDialogView;
			private int checkID, pos;
			private AdapterView<?> itemClick;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				itemClick = parent;
				pos = position;

				AlertDialog.Builder adb = new AlertDialog.Builder(parent.getContext());
				adb.setView(alertDialogView);
				adb.setTitle("Would you like to remove this entry?");
				adb.setIcon(android.R.drawable.ic_dialog_alert);
				adb.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								checkID = ((ServerCheck) itemClick.getItemAtPosition(pos)).getCheckID();
								sbDbAdapter.openReadableDatabase();
								if (sbDbAdapter.deleteCheck(checkID)) {
									Context context = getActivity().getApplicationContext();
									CharSequence text = "Check: " + checkID + ", was removed";
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
									serverCheckList.remove(pos);
									updateListView();
								}
								sbDbAdapter.close();
							}
						});
				adb.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// finish();
							}
						});
				adb.show();
			}

		});
		recentList.setOnItemLongClickListener(new OnItemLongClickListener() {
			private View alertDialogView;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(parent.getContext());
				adb.setView(alertDialogView);
				adb.setTitle("Would you like to remove all entries?");
				adb.setIcon(android.R.drawable.ic_dialog_alert);
				adb.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								sbDbAdapter.openReadableDatabase();
								if (sbDbAdapter.deleteAllEntries()) {
									Context context = getActivity().getApplicationContext();
									CharSequence text = "All entries have been removed.";
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
									serverCheckList.clear();
									updateListView();
								} else {
									Context context = getActivity().getApplicationContext();
									CharSequence text = "There was an error removing records from the database.";
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
								}
								sbDbAdapter.close();
							}
						});
				adb.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// finish();
							}
						});
				adb.show();
				return true;
			}
		});
		setButtonListener();
	}

	public void setOnTouchListeners() {
		checkCash.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText et = (EditText) rootView.findViewById(R.id.editCheckAmountCash);
				et.setText("");
				return false;
			}
		});

		checkCredit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText et = (EditText) rootView.findViewById(R.id.editCheckAmountCredit);
				et.setText("");
				return false;
			}
		});
		tipCash.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText et = (EditText) rootView.findViewById(R.id.editTipCash);
				et.setText("");
				return false;
			}
		});
		tipCredit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText et = (EditText) rootView.findViewById(R.id.editTipCredit);
				et.setText("");
				return false;
			}
		});
		tipCredit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (tipCredit.isFocused()) {
					tipCredit.clearFocus();
					InputMethodManager inputManager = (InputMethodManager)
					rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});
		textNote.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText et = (EditText) rootView.findViewById(R.id.editTextNote);
				et.setText("");
				return false;
			}
		});
		textNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textNote.isFocused()) {
					textNote.clearFocus();
					InputMethodManager inputManager = (InputMethodManager)
					rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});
	}

	public void setNumberToCurrency() {
		checkCash.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals(current) && !s.toString().isEmpty()) {
					EditText et = (EditText) rootView.findViewById(R.id.editCheckAmountCash);
					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);
					String formated = NumberFormat.getCurrencyInstance().format((parsed / 100));

					current = formated;
					et.setText(formated);
					et.setSelection(formated.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
		});
		checkCredit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals(current) && !s.toString().isEmpty()) {
					EditText et = (EditText) rootView.findViewById(R.id.editCheckAmountCredit);
					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);

					String formated = NumberFormat.getCurrencyInstance().format((parsed / 100));

					current = formated;
					et.setText(formated);
					et.setSelection(formated.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
		});
		tipCash.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals(current) && !s.toString().isEmpty()) {
					EditText et = (EditText) rootView.findViewById(R.id.editTipCash);
					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);
					String formated = NumberFormat.getCurrencyInstance().format((parsed / 100));

					current = formated;
					et.setText(formated);
					et.setSelection(formated.length());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
		});
		tipCredit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals(current) && !s.toString().isEmpty()) {
					EditText et = (EditText) rootView.findViewById(R.id.editTipCredit);
					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);
					String formated = NumberFormat.getCurrencyInstance().format((parsed / 100));

					current = formated;
					et.setText(formated);
					et.setSelection(formated.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
		});
		
	}

	public void setButtonListener() {
		recordCheck = (Button) rootView.findViewById(R.id.recordButton);
		todayButton = (Button) rootView.findViewById(R.id.todayButton);
		recordCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String date = "";
				double cCash, cCredit, tCash, tCredit;
				boolean cCashBool = false, cCreditBool = false, tCashBool = false, tCreditBool = false;
				boolean isPM = ((Switch) rootView.findViewById(R.id.isPM)).isChecked();
				
				int day = datePicker.getDayOfMonth();
				int month = datePicker.getMonth() + 1;
				int year = datePicker.getYear();
				date = month + "-" + day + "-" + year;
				
				EditText editCCash = (EditText) rootView.findViewById(R.id.editCheckAmountCash);
				EditText editCCredit = (EditText) rootView.findViewById(R.id.editCheckAmountCredit);
				EditText editTCash = (EditText) rootView.findViewById(R.id.editTipCash);
				EditText editTCredit = (EditText) rootView.findViewById(R.id.editTipCredit);
				EditText editNote = (EditText) rootView.findViewById(R.id.editTextNote);

				String temp = "";
				String temp2 = "";
				String temp3 = "";
				String temp4 = "";
				temp = (editCCash.getText()).toString();
				if (temp.length() == 0) {
					cCash = 0;
					cCashBool = true;
				} else {
					// cCash = Double.parseDouble((temp).substring(1));
					cCash = Double.parseDouble(temp);
					cCashBool = moneyInputCheck(cCash);
					if (!cCashBool) {
						invalidInputToast("Check Total (Cash)");
					}
				}
				temp2 = (editCCredit.getText()).toString();
				if (temp2.length() == 0) {
					cCredit = 0;
					cCreditBool = true;
				} else {
					// cCredit = Double.parseDouble((temp).substring(1));
					cCredit = Double.parseDouble(temp2);
					cCreditBool = moneyInputCheck(cCredit);
					if (!cCreditBool) {
						invalidInputToast("Check Total (Credit Card)");
					}
				}
				temp3 = (editTCash.getText()).toString();
				if (temp3.length() == 0) {
					tCash = 0;
					tCashBool = true;
				} else {
					// tCash = Double.parseDouble((temp).substring(1));
					tCash = Double.parseDouble(temp3);
					tCashBool = moneyInputCheck(tCash);
					if (!tCashBool) {
						invalidInputToast("Tip (Cash)");
					}
				}
				temp4 = (editTCredit.getText()).toString();
				if (temp4.length() == 0) {
					tCredit = 0;
					tCreditBool = true;
				} else {
					// tCredit = Double.parseDouble((temp).substring(1));
					tCredit = Double.parseDouble(temp4);
					tCreditBool = moneyInputCheck(tCredit);
					if (!tCreditBool) {
						invalidInputToast("Tip (Credit Card)");
					}
				}
				if (cCashBool && cCreditBool && tCashBool && tCreditBool) {
					sbDbAdapter.openWriteableDatabase();
					sbDbAdapter.insertChecks(date, isPM, cCash, cCredit, tCash, tCredit, (editNote.getText()).toString());
					sbDbAdapter.close();
					editCCash.setText("");
					editCCash.clearFocus();
					editCCredit.setText("");
					editCCredit.clearFocus();
					editTCash.setText("");
					editTCash.clearFocus();
					editTCredit.setText("");
					editTCredit.clearFocus();
					editNote.setText("");
					editNote.clearFocus();
					initializeData();
				}
				InputMethodManager inputManager = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

			public boolean moneyInputCheck(double input) {
				String[] temp = Double.toString(input).split("\\.");
				if (input < 100000 && input > -100000 && temp[1].length() <= 2) {
					return true;
				} else {
					return false;
				}
			}
			
			public void invalidInputToast(String alert) {
				Context context = getActivity().getApplicationContext();
				CharSequence text = alert + " Input is Incorrect.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
		todayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
		    	int year = c.get(Calendar.YEAR);
		    	int monthOfYear = c.get(Calendar.MONTH);
		    	int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		    	
		    	datePicker.updateDate(year, monthOfYear, dayOfMonth);
			}
		});
	}
	public void setEditTextFocusDown() {
		checkCash.setNextFocusDownId(R.id.editCheckAmountCredit);
		checkCredit.setNextFocusDownId(R.id.editTipCash);
		tipCash.setNextFocusDownId(R.id.editTipCredit);

	}
	
}