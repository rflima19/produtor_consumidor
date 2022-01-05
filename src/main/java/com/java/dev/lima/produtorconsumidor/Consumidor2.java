package com.java.dev.lima.produtorconsumidor;

import java.util.Random;

public class Consumidor2<T> implements Runnable {

	private BufferArray<T> buffer;

	public Consumidor2(BufferArray<T> buffer) {
		super();
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		Random r = new Random();
		while (true) {
			T obj = this.buffer.consumir();
			//this.buffer.imprimir();
			try {
				Thread.sleep(r.nextInt(10) * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
