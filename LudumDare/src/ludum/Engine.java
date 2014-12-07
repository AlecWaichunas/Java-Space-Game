package ludum;

import ludum.screens.MainScreen;

public class Engine implements Runnable{

	private MainScreen ms;
	private boolean isRunning;
	private Thread t;
	
	public Engine(MainScreen ms){
		this.ms = ms;
	}
	
	public void start(){
		t = new Thread(this, "GameThread");
		isRunning = true;
		t.start();
	}
	
	public void stop(){
		if(t != null){
			isRunning = false;
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		long before = System.nanoTime();
		double ns = 1000000000/60D;
		double delta = 0;
		
		int fps = 0, ups = 0;
		long timer = System.currentTimeMillis();
		boolean canRender = false;
		
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - before)/ns;
			before = now;
			while(delta >= 1){
				delta--;
				ups++;
				ms.update();
				canRender = true;
			}
			if(canRender){
				ms.render();
				fps++;
				canRender = false;
			}
			if(System.currentTimeMillis() - timer >= 1000){
				timer = System.currentTimeMillis();
				System.out.println("UPS: " + ups + " | FPS: " + fps);
				fps = 0;
				ups = 0;
			}
		}
	}

}
