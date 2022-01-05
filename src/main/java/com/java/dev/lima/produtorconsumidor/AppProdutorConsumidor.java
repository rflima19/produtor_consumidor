package com.java.dev.lima.produtorconsumidor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppProdutorConsumidor {

	public static void main(String[] args) {
		BufferList bufferList = new BufferList(10);
		// ExecutorService executor = Executors.newFixedThreadPool(20);
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			executor.execute(new Consumidor(bufferList));
		}
		for (int i = 0; i < 10; i++) {
			executor.execute(new Produtor(bufferList));
		}
		executor.shutdown();
	}
}
