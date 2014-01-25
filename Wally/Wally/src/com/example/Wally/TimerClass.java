package com.example.Wally;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimerClass extends Activity {
	public static String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		Button set = (Button) findViewById(R.id.button1);
		final EditText interval = (EditText) findViewById(R.id.editText2);
		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Intent i = new Intent(getApplicationContext(),
							Myservice.class);

					MainActivity.getInstance().startService(i);
					Intent intent = new Intent(MainActivity.getInstance(),
							MainActivity.class);

					startActivity(intent);
				} finally {
				}
			}
		};
		set.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				time = interval.getText().toString();
				if (Myservice.al.size() == 0) {
					Toast.makeText(MainActivity.getInstance(),
							"No Image selected", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(),
							Myservice.class);
					MainActivity.getInstance().stopService(i);

				} else {

					thread.start();
				}
			}
		});
	}

}
