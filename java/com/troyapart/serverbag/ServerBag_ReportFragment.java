package com.troyApart.serverbag;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ServerBag_ReportFragment extends Fragment {
    public static final String ARG_LIST_NUMBER = "list_number";
    private ServerBag_Database_Adapter sbDbAdapter;
    private SalesReport salesReport;
    private View rootView;
    private DatePicker datePicker;
    private String dateFromPicker;
    private Spinner shiftSpinner;
    private ListView reportList;
    private ArrayAdapter<ServerCheck> reportListAdapter;
	private ArrayList<ServerCheck> serverCheckList;
	private ArrayList<ServerCheck> tempList;
    private int shift;
    private boolean isReport;
    private Button todayButton;
    
    public ServerBag_ReportFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        int i = getArguments().getInt(ARG_LIST_NUMBER);
        String title = getResources().getStringArray(R.array.drawer_list)[i];        
        getActivity().setTitle(title);
        sbDbAdapter = new ServerBag_Database_Adapter(getActivity().getBaseContext());
        
        shift = 0;
        todayButton = (Button) rootView.findViewById(R.id.report_todayButton);
		initializeData();
		setListeners();        
        setShiftSpinner();
        
        setHasOptionsMenu(true);
        
        return rootView;
    }

    private View emailView;
    private AlertDialog.Builder emailDialogBuilder;
    private AlertDialog emailDialog;
    private Button buttonSend;
    private EditText textTo;
    private EditText textSubject;
    private EditText textMessage;
//    File outputFile = null;
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	MenuItem item = menu.findItem(R.id.email_report);
		item.setVisible(true);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (!isReport) {
		  			Context context = getActivity().getApplicationContext();
					CharSequence text = "No Report to Email.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(
							context, text, duration);
					toast.show();
				} else {
					emailView = View.inflate(getActivity(), R.layout.fragment_email, null);
					emailDialogBuilder = new AlertDialog.Builder(getActivity());
					
					buttonSend = (Button) emailView.findViewById(R.id.buttonSend);
					textTo = (EditText) emailView.findViewById(R.id.editTextTo);
					textSubject = (EditText) emailView.findViewById(R.id.editTextSubject);
					textMessage = (EditText) emailView.findViewById(R.id.editTextMessage);
					
					textSubject.setText("SalesReport_" + dateFromPicker);
//					sbDbAdapter.openReadableDatabase();
//					if (sbDbAdapter.isVirginRun()) {
//						Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//						  startActivityForResult(intent, PICK_CONTACT);
//					} else {
//						
//					}
//					sbDbAdapter.close();
					String shiftOfReport = "";
//					File outputDir = getActivity().getApplicationContext().getCacheDir(); // context being the Activity pointer
					
//					try {

						if (shift == 0) {
							shiftOfReport = "Full Shift";
						} else if (shift == 1) {
							shiftOfReport = "AM";
						} else {
							shiftOfReport = "PM";
						}
//						outputFile = File.createTempFile("SalesReport_" + dateFromPicker + "_", ".txt", outputDir);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.e("BROKEN", "Could not write file " + e.getMessage());
//					}
					
					ArrayList<String> reportString = new ArrayList<String>();
					reportString.add("Sales Report: " + shiftOfReport + "\n");
					reportString.add("Total Sales: " + ((TextView)rootView.findViewById(R.id.report_totalSales)).getText().toString() + "\n");
					reportString.add("Total Cash Payments: " + ((TextView)rootView.findViewById(R.id.report_checkCash)).getText().toString() + "\n");
					reportString.add("Total Credit Card Payments: " + ((TextView)rootView.findViewById(R.id.report_checkCredit)).getText().toString() + "\n");
					reportString.add("Total Credit Card Tip Adjustment: " + ((TextView)rootView.findViewById(R.id.report_tipCredit)).getText().toString() + "\n");
					reportString.add("Total Cash Turn-In: " + ((TextView)rootView.findViewById(R.id.report_totalTurnIn)).getText().toString());
					
					for (String s : reportString) {
						textMessage.setText(textMessage.getText().toString() + reportString.get(reportString.indexOf(s)));
					}
//					try {
//						FileWriter fileWriter = new FileWriter(outputFile);
//						fileWriter.append(fileString.toString());
//						fileWriter.flush();
//						fileWriter.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					buttonSend.setOnClickListener(new OnClickListener() {
						 
						@Override
						public void onClick(View v) {
			 
						  String to = textTo.getText().toString();
						  String subject = textSubject.getText().toString();
						  String message = textMessage.getText().toString();
			 
						  Intent email = new Intent(Intent.ACTION_SEND);
						  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
						  //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
						  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
						  email.putExtra(Intent.EXTRA_SUBJECT, subject);
						  email.putExtra(Intent.EXTRA_TEXT, message);
						  //email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
			 
						  //need this to prompts email client only
						  email.setType("message/rfc822");
			 
						  startActivity(Intent.createChooser(email, "Choose an Email client :"));
						  
						  emailDialog.dismiss();
						}
					});
					emailDialog = emailDialogBuilder.setTitle("Email")
					.setMessage(" Select from below ")
					.setView(emailView)
					.show();
				}
				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
    }
    
//    private static final int PICK_CONTACT=1;
//    @Override
//    public void onActivityResult(int reqCode, int resultCode, Intent data) {
//    super.onActivityResult(reqCode, resultCode, data);
//    	sbDbAdapter.openWriteableDatabase();
//	    switch (reqCode) {
//		    case (PICK_CONTACT) :
//		      if (resultCode == Activity.RESULT_OK) {
//		    	  sbDbAdapter.devirginize(true);
//		      } else {
//		    	  sbDbAdapter.devirginize(false);
//		      }
//	    }
//	    sbDbAdapter.close();
//    }
    
    private void initializeData() {
    	serverCheckList = new ArrayList<ServerCheck>();
    	String reportDate = "";
    	DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.report_datePicker);
    	reportDate = (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + "-" + datePicker.getYear();
    	dateFromPicker = reportDate;
    	Double dayCashTotal = 0.00, nightCashTotal = 0.00, dayCreditTotal = 0.00, nightCreditTotal = 0.00, dayCashTipTotal = 0.00, nightCashTipTotal = 0.00, dayCreditTipTotal = 0.00, nightCreditTipTotal = 0.00;
    	Double cCash, cCredit, tCash, tCredit;
    	boolean isData = false;
    	sbDbAdapter.openReadableDatabase();
    	Cursor cursor = sbDbAdapter.getAllChecksByDateCursor(reportDate);
    	if (cursor.moveToLast() != false) {
    		isData = true;
    		do {
    			int idIndex, id, isPMIndex, cCashIndex, cCreditIndex, tCashIndex, tCreditIndex, noteIndex;
    			String note;
    			boolean isPM;
    			idIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_ID);
    			isPMIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_IS_PM);
				cCashIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_CHECK_CASH);
				cCreditIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_CHECK_CREDIT);
				tCashIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_TIP_CASH);
				tCreditIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_TIP_CREDIT);
				noteIndex = cursor.getColumnIndex(ServerBag_Database_Adapter.CHECK_NOTE);
				id = cursor.getInt(idIndex);
				if (cursor.getInt(isPMIndex) == 1) {
					isPM = true;
				} else {
					isPM = false;
				}
				note = cursor.getString(noteIndex);
    			if (isPM) {
    				cCash = cursor.getDouble(cCashIndex);
    				nightCashTotal += cCash;
    				cCredit = cursor.getDouble(cCreditIndex);
    				nightCreditTotal += cCredit;
    				tCash = cursor.getDouble(tCashIndex);
    				nightCashTipTotal += tCash;
    				tCredit = cursor.getDouble(tCreditIndex);
    				nightCreditTipTotal += tCredit;
    			} else {
    				cCash = cursor.getDouble(cCashIndex);
    				dayCashTotal += cCash;
    				cCredit = cursor.getDouble(cCreditIndex);
    				dayCreditTotal += cCredit;
    				tCash = cursor.getDouble(tCashIndex);
    				dayCashTipTotal += tCash;
    				tCredit = cursor.getDouble(tCreditIndex);
    				dayCreditTipTotal += tCredit;
    			}
    			serverCheckList.add(new ServerCheck(id, reportDate, cCash, cCredit, tCash, tCredit, isPM, note));
    		} while (cursor.moveToPrevious());
    	}
    	cursor.close();
    	sbDbAdapter.close();
 		salesReport = new SalesReport(reportDate, dayCashTotal, nightCashTotal, dayCreditTotal, nightCreditTotal, dayCashTipTotal, nightCashTipTotal, dayCreditTipTotal, nightCreditTipTotal);
  		reportList = (ListView) rootView.findViewById(R.id.report_listView);
 		checkShift();
  		if (!isData) {
  			Context context = getActivity().getApplicationContext();
			CharSequence text = "There are no Records for this Date.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(
					context, text, duration);
			toast.show();
			isReport = false;
  		} else {
  			isReport = true;
  		}
    }
    
    private void checkShift() {
    	TextView totalSales = (TextView) rootView.findViewById(R.id.report_totalSales);
    	TextView totalTurnIn = (TextView) rootView.findViewById(R.id.report_totalTurnIn);
    	TextView checkCash = (TextView) rootView.findViewById(R.id.report_checkCash);
    	TextView checkCredit = (TextView) rootView.findViewById(R.id.report_checkCredit);
    	TextView totalTips = (TextView) rootView.findViewById(R.id.report_totalTips);
    	TextView tipCash = (TextView) rootView.findViewById(R.id.report_tipCash);
    	TextView tipCredit = (TextView) rootView.findViewById(R.id.report_tipCredit);
    	DecimalFormat df = new DecimalFormat("0.00");
    	switch (shift) {
	    	case 0:
	        	totalSales.setText("$" + df.format(salesReport.getTotalSales()));
	        	if (salesReport.getTotalTurnIn() < 0) {
	        		totalTurnIn.setText("-$" + df.format(Math.abs(salesReport.getTotalTurnIn())));
	        	} else {
	        		totalTurnIn.setText("$" + df.format(salesReport.getTotalTurnIn()));
	        	}
	        	checkCash.setText("$" + df.format(salesReport.getDayCashTotal() + salesReport.getNightCashTotal()));
	        	checkCredit.setText("$" + df.format(salesReport.getDayCreditTotal() + salesReport.getNightCreditTotal()));
	        	totalTips.setText("$" + df.format(salesReport.getTotalTips()));
	        	tipCash.setText("$" + df.format(salesReport.getDayCashTipTotal() + salesReport.getNightCashTipTotal()));
	        	tipCredit.setText("$" + df.format(salesReport.getDayCreditTipTotal() + salesReport.getNightCreditTipTotal()));
	    		break;
	    	case 1:
	        	totalSales.setText("$" + df.format(salesReport.getDayTotalSales()));
	        	if (salesReport.getDayTotalTurnIn() < 0) {
	        		totalTurnIn.setText("-$" + df.format(Math.abs(salesReport.getDayTotalTurnIn())));
	        	} else {
	        		totalTurnIn.setText("$" + df.format(salesReport.getDayTotalTurnIn()));
	        	}
	        	checkCash.setText("$" + df.format(salesReport.getDayCashTotal()));
	        	checkCredit.setText("$" + df.format(salesReport.getDayCreditTotal()));
	        	totalTips.setText("$" + df.format(salesReport.getDayTotalTips()));
	        	tipCash.setText("$" + df.format(salesReport.getDayCashTipTotal()));
	        	tipCredit.setText("$" + df.format(salesReport.getDayCreditTipTotal()));
	    		break;
	    	case 2:
	        	totalSales.setText("$" + df.format(salesReport.getNightTotalSales()));
	        	if (salesReport.getNightTotalTurnIn() < 0) {
	        		totalTurnIn.setText("-$" + df.format(Math.abs(salesReport.getNightTotalTurnIn())));
	        	} else {
	        		totalTurnIn.setText("$" + df.format(salesReport.getNightTotalTurnIn()));
	        	}
	        	checkCash.setText("$" + df.format(salesReport.getNightCashTotal()));
	        	checkCredit.setText("$" + df.format(salesReport.getNightCreditTotal()));
	        	totalTips.setText("$" + df.format(salesReport.getNightTotalTips()));
	        	tipCash.setText("$" + df.format(salesReport.getNightCashTipTotal()));
	        	tipCredit.setText("$" + df.format(salesReport.getNightCreditTipTotal()));    		
	    		break;
	    	default:
	    		Log.d("ShiftError", "Error: int shift = " + shift);
	    		break;
    	}
    	updateListView();
    }
    
    private void updateListView() {
    	tempList = new ArrayList<ServerCheck>();
    	switch (shift) {
	    	case 0:
	    		tempList = serverCheckList;
				break;
	    	case 1:
	    		tempList.clear();
	    		for (ServerCheck s : serverCheckList) {
	    			if (!s.getIsPM()) {
	    				tempList.add(s);
	    			}
	    		}
	    		break;
	    	case 2:
	    		tempList.clear();
	    		for (ServerCheck s : serverCheckList) {
	    			if (s.getIsPM()) {
	    				tempList.add(s);
	    			}
	    		}
	    		break;
	    	default:
	    		Log.d("ListError", "Error: int shift = " + shift);
	    		break;
    	}
    	reportListAdapter = new ArrayAdapter<ServerCheck>(
				rootView.getContext(), android.R.layout.simple_list_item_1,
				tempList);
		reportList.setAdapter(reportListAdapter);
		reportListAdapter.notifyDataSetChanged();
	}
    
    
    
    private void setListeners() {
    	Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
    	int monthOfYear = c.get(Calendar.MONTH);
    	int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    	
    	datePicker = (DatePicker) rootView.findViewById(R.id.report_datePicker);
        datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
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
				initializeData();
			}
		});
        reportList.setOnItemClickListener(new OnItemClickListener() {
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
									//TODO find id in serverCheckList and remove, then initalizeData again
//									int removedId = ((ServerCheck)tempList.get(pos)).getCheckID();
//									for (ServerCheck s : serverCheckList) {
//										if (s.getCheckID() == removedId) {
//											serverCheckList.remove();
//										}
//									}
									initializeData();
//									updateListView();
								} else {
									Context context = getActivity().getApplicationContext();
									CharSequence text = "There was an error removing the record from the database.";
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
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
		reportList.setOnItemLongClickListener(new OnItemLongClickListener() {
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
								if (shift == 0) {
									if (sbDbAdapter.deleteAllEntries()) {
										Context context = getActivity().getApplicationContext();
										CharSequence text = "All entries have been removed.";
										int duration = Toast.LENGTH_SHORT;
										Toast toast = Toast.makeText(context, text, duration);
										toast.show();
										//TODO find each id in serverCheckList and remove, then initalizeData again
										initializeData();
									} else {
										Context context = getActivity().getApplicationContext();
										CharSequence text = "There was an error removing records from the database.";
										int duration = Toast.LENGTH_SHORT;
										Toast toast = Toast.makeText(context, text, duration);
										toast.show();
									}
								} else {
									for (ServerCheck s : tempList) {
										sbDbAdapter.deleteCheck(s.getCheckID());
									}
									Context context = getActivity().getApplicationContext();
									CharSequence text = "All entries have been removed.";
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
									initializeData();
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
    
    private void setShiftSpinner() {
    	shiftSpinner = (Spinner) rootView.findViewById(R.id.report_shiftSpinner);
    	ArrayList<String> sArray = new ArrayList<String>();
    	sArray.add("SHIFT");
    	sArray.add("AM");
    	sArray.add("PM");
    	ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, sArray);
    	shiftSpinner.setAdapter(sAdapter);
    	
    	shiftSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				shift = position;
				checkShift();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}