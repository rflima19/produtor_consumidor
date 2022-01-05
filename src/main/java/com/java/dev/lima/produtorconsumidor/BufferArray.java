package com.java.dev.lima.produtorconsumidor;

import java.lang.reflect.Array;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class BufferArray<T> {

	private ReentrantLock lock;
	private Condition condition;
	private T[] buffer;
	private int tamanhoMax;
	private int tamanhoAtual;
	
	public BufferArray(Class<T> clazz, int tamanho) {
		super();
		this.lock = new ReentrantLock();
		this.condition = this.lock.newCondition();
		this.buffer =  (T[]) Array.newInstance(clazz, tamanho);
		this.tamanhoMax = tamanho;
		this.tamanhoAtual = 0;
	}
	
	private T remover() {
		T elemento = this.buffer[0];
		this.buffer[0] = null;
		int j = 0;
		T aux = null;
		for (int i = 0; i < (this.buffer.length - 1); i++) {
			j = i + 1;
			aux = this.buffer[i];
			this.buffer[i] = this.buffer[j];
			this.buffer[j] = aux;
		}
		return elemento;
	}
	
	private void adicionar(T elemento) {
		for (int i = 0; i < this.buffer.length; i++) {
			if (this.buffer[i] == null) {
				this.buffer[i] = elemento;
				break;
			}
		}
	}
	
	public T consumir() {
		this.lock.lock();
		try {
			while (this.tamanhoAtual <= 0) {
				try {
					this.condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			T elemento = this.remover();
			this.tamanhoAtual--;
			this.imprimir();
			this.condition.signalAll();
			return elemento;
		} finally {
			this.lock.unlock();
		}
	}
	
	public void produzir(T elemento) {
		this.lock.lock();
		try {
			while (this.tamanhoAtual >= this.tamanhoMax) {
				try {
					this.condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.adicionar(elemento);
			this.tamanhoAtual++;
			this.imprimir();
			this.condition.signalAll();
		} finally {
			this.lock.unlock();
		}
	}
	
	public void imprimir() {
		this.lock.lock();
		try {
			Collector<T, StringBuilder, String> myCollector = 
					Collector.<T, StringBuilder, String>of(
							() -> new StringBuilder(),
							(strb, e) -> {
								if (e == null) {
									return;
								}
								strb.append(e.toString() + " ");
								},
							(strb1, strb2) -> strb1.append(strb2.toString()),
							(strb) -> {
								StringBuilder strbAux = new StringBuilder();
								strbAux.append("[");
								strbAux.append(this.tamanhoAtual);
								strbAux.append("] : ");
								strbAux.append(strb.toString());
								strbAux.append(String.format("%n"));
								return strbAux.toString();
							}
							);
			String result = Stream.of(this.buffer).parallel().collect(myCollector);
			System.out.print(result);
		} finally {
			this.lock.unlock();
		}
	}
}
