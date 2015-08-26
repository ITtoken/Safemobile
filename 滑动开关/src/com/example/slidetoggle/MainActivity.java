package com.example.slidetoggle;

import com.example.togglebutton.ToggleButton;
import com.example.togglebutton.ToggleButton.ToggleState;
import com.example.togglebutton.ToggleButton.onToggleStateChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ToggleButton tb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tb = (ToggleButton) findViewById(R.id.tb);
		tb.setBacgrounfSource(R.drawable.slidebg);
		tb.setSwitckSource(R.drawable.slidebtn);
		tb.setDefaltStat(ToggleState.OPEN);

		tb.setToggleStateChangeListener(new onToggleStateChangeListener() {

			@Override
			public void ToggleState(ToggleState toggleState) {
				Toast.makeText(MainActivity.this,
						toggleState == ToggleState.OPEN ? "¿ªÆô" : "¹Ø±Õ", 0)
						.show();

			}
		});

	}

}
