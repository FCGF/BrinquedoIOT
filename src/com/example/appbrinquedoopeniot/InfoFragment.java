package com.example.appbrinquedoopeniot;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_info_fragment, container,
				false);
		getDialog().setTitle("DialogFragment Tutorial");		
		// Do something else
		return rootView;
	}
}
