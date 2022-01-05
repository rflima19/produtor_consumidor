package com.java.dev.lima.produtorconsumidor;

import java.util.Random;

public class Consumidor implements Runnable {

	private BufferList buffer;

	public Consumidor(BufferList buffer) {
		super();
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		Random r = new Random();
		while (true) {
			Integer i = this.buffer.consumir();
			//this.buffer.imprimir();
			try {
				Thread.sleep(r.nextInt(10) * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
