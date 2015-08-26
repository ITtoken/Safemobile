package com.example.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slidemn.R;

public class NetDiskFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View netdiskFrameView = inflater.inflate(R.layout.netdiskframe, null);
		return netdiskFrameView;
	}

}
