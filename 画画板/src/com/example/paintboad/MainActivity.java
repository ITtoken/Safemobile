package com.example.paintboad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageView iv;
	private CheckBox ck;
	private Paint paint;
	private Bitmap bitmap;
	private Canvas canvas;
	private Bitmap bm;
	private String fileName=null;
	private EditText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		bitmap = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());
		canvas = new Canvas(bitmap);
		paint = new Paint();
		//作画
		canvas.drawBitmap(bitmap, new Matrix(), paint);
		
		
		
		iv = (ImageView) findViewById(R.id.iv);
		ck = (CheckBox) findViewById(R.id.ck);
		
		iv.setOnTouchListener(new OnTouchListener() {
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					//获取滑动时的坐标
					int x = (int) event.getX();
					int y = (int) event.getY();
					
					if(ck.isChecked()){
						canvas.drawPoint(x, y, paint);
					}else{
						canvas.drawLine(startX, startY, x, y , paint);
						startX=x;
						startY=y;
					}
					iv.setImageBitmap(bitmap);
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("点击离开");
					break;

				}
				return true;
			}
		});
	}
	
	public void click(View v){
		if(ck.isChecked()){
			ck.setText("点线");
		}else{
			ck.setText("实线");
		}
	}
	
	public void save(View v){
		text = new EditText(this);
		new AlertDialog.Builder(this)
		.setTitle("保存文件")
		.setMessage("请输入文件名")
		.setView(text)
		.setPositiveButton("确定", new MyListener())
		.show();

	}
	public void red(View v){
		paint.setColor(Color.RED);
	}
	public void brush(View v){
		paint.setStrokeWidth(5);
	}
	
	class MyListener implements OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			fileName=text.getText().toString().trim();

			File file = null;
			if(!TextUtils.isEmpty(fileName))
			{
				file = new File("sdcard/"+fileName+".png");
			}else{
				file = new File("sdcard/default.png");
			}
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, stream);
				
				//发送SD卡就绪广播，让SD卡便利媒体文件，并在MediaStore数据库中建立索引，这样系统的多媒体应用就可以找到该文件
				//并将其加载到列表中
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
				intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
				sendBroadcast(intent);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Toast.makeText(MainActivity.this, "保存成功", 0).show();
			
		}
		
	}
	
}
