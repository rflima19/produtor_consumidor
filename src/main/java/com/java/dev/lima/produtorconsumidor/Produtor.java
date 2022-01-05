package com.java.dev.lima.produtorconsumidor;

import java.util.Random;

public class Produtor implements Runnable {

	private BufferList buffer;

	public Produtor(BufferList buffer) {
		super();
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		Random r = new Random();
		while (true) {
			Integer i = r.nextInt(10);
			this.buffer.produzir(i);
			//this.buffer.imprimir();
			try {
				Thread.sleep(r.nextInt(10) * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
