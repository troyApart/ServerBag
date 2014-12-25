package com.troyApart.serverbag;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ServerBag_EarningsFragment extends Fragment {
	public static final String ARG_LIST_NUMBER = "list_number";
	private ServerBag_Database_Adapter sbDbAdapter;
	private View rootView;

	private Button buttonSend;
    private EditText textTo;
    private EditText textSubject;
    private EditText textMessage;
	
	public ServerBag_EarningsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		int i = getArguments().getInt(ARG_LIST_NUMBER);
		String title = getResources().getStringArray(R.array.drawer_list)[i];

		sbDbAdapter = new ServerBag_Database_Adapter(getActivity().getBaseContext());
		
		initializeData();
		setListeners();

		getActivity().setTitle(title);
		return rootView;
	}

	public void initializeData() {
		sbDbAdapter.openReadableDatabase();
		Cursor cursor = sbDbAdapter.getAllEmails();
		
	}

	public void setListeners() {
		
	}
}