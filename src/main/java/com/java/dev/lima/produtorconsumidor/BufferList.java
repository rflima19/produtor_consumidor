package com.java.dev.lima.produtorconsumidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;

public class BufferList {

	private ReentrantLock lock;
	private Condition condition;
	private List<Integer> buffer;
	private int tamanho;
	
	public BufferList(int tamanho) {
		super();
		this.lock = new ReentrantLock();
		this.condition = this.lock.newCondition();
		this.buffer = new ArrayList<>();
		this.tamanho = tamanho;
	}
	
	public Integer consumir() {
		this.lock.lock();
		try {
			while (this.buffer.size() <= 0) {
				try {
					this.condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Integer i = this.buffer.remove(0);
			this.imprimir();
			this.condition.signalAll();
			return i;
		} finally {
			this.lock.unlock();
		}
	}
	
	public void produzir(Integer i) {
		this.lock.lock();
		try {
			while (this.buffer.size() >= this.tamanho) {
				try {
					this.condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.buffer.add(i);
			this.imprimir();
			this.condition.signalAll();
		} finally {
			this.lock.unlock();
		}
	}
	
	public void imprimir() {
		this.lock.lock();
		try {
//			System.out.print("[" + this.buffer.size() + "]: ");
//			for (Integer integer : this.buffer) {
//				System.out.print(integer + " ");
//			}
//			System.out.println("");
			Collector<Integer, StringBuilder, String> myCollector = 
					Collector.<Integer, StringBuilder, String>of(
							() -> new StringBuilder(),
							(strb, i) -> strb.append(i.toString() + " "),
							(strb1, strb2) -> strb1.append(strb2.toString()),
							(strb) -> {
								StringBuilder strbAux = new StringBuilder();
								strbAux.append("[");
								strbAux.append(this.buffer.size());
								strbAux.append("] : ");
								strbAux.append(strb.toString());
								strbAux.append(String.format("%n"));
								return strbAux.toString();
							}
							);
			String result = this.buffer.stream().parallel().collect(myCollector);
			System.out.print(result);
		} finally {
			this.lock.unlock();
		}
	}
}
