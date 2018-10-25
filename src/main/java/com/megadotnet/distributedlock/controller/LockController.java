package com.megadotnet.distributedlock.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.megadotnet.distributedlock.entity.Product;
import com.megadotnet.distributedlock.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;

@RestController
@RequestMapping("/api")
@Slf4j
public class LockController {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Autowired
	private ProductService service;

	AtomicInteger atomicInt = new AtomicInteger(0);

	/**
	 * insert with distributed lock in  Hazelcast
	 * @return
	 */
	@RequestMapping(value = "/insertWithLock")
	public String withLock() {
		return insert(1);
	}

	/**
	 * insert with synchronized
	 * @return
	 */
	@RequestMapping(value = "/insertWithSyncLock")
	public String withSyncLock() {
		return insertSync(1);
	}

	/**
	 *  insert with no lock
	 * @return
	 */
	@RequestMapping(value = "/insertWithoutLock")
	public String withoutLock() {
		return insertWithoutLock();
	}

	@RequestMapping(value = "/lockreset")
	public boolean resetSequence() {
		atomicInt.set(0);
		return true;
	}

	@GetMapping(value = "/getall")
	public List<String> getall() {
		return service.listAllProducts();
	}

	@RequestMapping(value = "/deleteall")
	public long deleteall() {
		return service.deleteAll();
	}

	private String insertWithoutLock() {
		Product p = service.insertProduct();
		if (p == null) {
			return "error";
		}
		return p.getSeq().toString();
	}

	private synchronized String insertSync(int amount)
	{
		Product p=null;
		int val=atomicInt.accumulateAndGet(amount,LockController::sum);
		p=service.insertProduct();

		if (p == null) {
			return "error";
		}
		return p.getSeq().toString();
	}

	private String insert(int amount) {
		Product p = null;
		ILock l = hazelcastInstance.getLock("savelock");
		try {
			if (l.tryLock(10, TimeUnit.SECONDS)) {
				int val = atomicInt.accumulateAndGet(amount, LockController::sum);
				p = service.insertProduct();
			}
			// Thread.sleep(300);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		} finally {
			try {
				l.unlock();
			} catch (Exception e) {
				l.forceUnlock();
			}

		}
		if (p == null) {
			return "error";
		}
		return p.getSeq().toString();
	}

	private static int sum(int x, int y) {
		return x + y;
	}

}
