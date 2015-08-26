package com.example.musicplayer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

public class MusicServer extends Service {

	private MediaPlayer mp;
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return new MidPlayer();
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		mp = new MediaPlayer();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
		if(mp!=null){
			mp.release();
			mp=null;
		}
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}

	class MidPlayer extends Binder implements Musicinterface {

		@Override
		public void start() {
			MusicServer.this.start();
		}

		@Override
		public void pause() {
			MusicServer.this.pause();
			
		}

		@Override
		public void stop() {
			MusicServer.this.stop();
		}

		@Override
		public void continued() {
			MusicServer.this.continued();
			
		}

		@Override
		public void seekTo(int progress) {
			MusicServer.this.seekTo(progress);
		}


	}

	/**
	 * 开始播放
	 * 
	 * @param v
	 */
	public void start() {
		try {
			mp.reset();//MediaPlayer对象创建后reset是必须的一步（每次都要执行）
			mp.setDataSource("sdcard/E-Type - Ding Ding Song.mp3");//设置文件的路径
//			mp.prepare();//准备（必须执行）
//			mp.start();//开始播放
			mp.prepareAsync();//异步准备
			mp.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();//开始播放
					addTimer();
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

	/**
	 * 暂停播放
	 * 
	 * @param v
	 */
	public void pause() {
			mp.pause();
	}

	/**
	 * 停止播放
	 * 
	 * @param v
	 */
	public void stop() {
		mp.stop();//停止播放
	}

	public void continued() {
		mp.start();
	}
	
	public void seekTo(int progress){
		mp.seekTo(progress);
	}
	
	public void addTimer(){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				int duration = mp.getDuration();//获取播放的音乐的总时长
				int currentPosition = mp.getCurrentPosition();//获取当前播放位置
				
				Message msg = MainActivity.handler.obtainMessage();
				Bundle data = new Bundle();
				data.putInt("duration", duration);
				data.putInt("currentPosition", currentPosition);
				msg.setData(data);
				MainActivity.handler.sendMessage(msg);
			}
		}, 5, 500);
	}
}
