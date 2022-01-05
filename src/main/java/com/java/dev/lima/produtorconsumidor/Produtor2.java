package com.java.dev.lima.produtorconsumidor;

import java.util.Random;
import java.util.function.Supplier;

public class Produtor2<T> implements Runnable {

	private BufferArray<T> buffer;
	private Supplier<T> supplier;

	public Produtor2(BufferArray<T> buffer, Supplier<T> supplier) {
		super();
		this.buffer = buffer;
		this.supplier = supplier;
	}
	
	@Override
	public void run() {
		Random r = new Random();
		while (true) {
			T obj = this.supplier.get();
			this.buffer.produzir(obj);
			//this.buffer.imprimir();
			try {
				Thread.sleep(r.nextInt(10) * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
