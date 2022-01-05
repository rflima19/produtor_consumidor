package com.java.dev.lima.produtorconsumidor;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppProdutorConsumidor {

	public static void main(String[] args) {
		// BufferList bufferList = new BufferList(10);
		BufferArray<Integer> bufferArray = new BufferArray<>(Integer.class, 10);
		// ExecutorService executor = Executors.newFixedThreadPool(20);
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			// executor.execute(new Consumidor(bufferList));
			executor.execute(new Consumidor2<Integer>(bufferArray));
		}
		for (int i = 0; i < 10; i++) {
			// executor.execute(new Produtor(bufferList));
			executor.execute(new Produtor2<Integer>(bufferArray, () -> {
				return new Random().nextInt(10);
			}));
		}
		executor.shutdown();
	}
}
