package com.megadotnet.distributedlock.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import com.megadotnet.distributedlock.entity.Product;
import com.megadotnet.distributedlock.service.ProductService;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		// 在Hazelcast 5.5中，CP锁应该从CP子系统获取
		Lock lock = hazelcastInstance.getCPSubsystem().getLock("savelock");
		try {
			// 尝试获取锁，设置超时时间
			if (lock.tryLock(10, TimeUnit.SECONDS)) {
				try {
					// 在获取锁后执行业务逻辑
					int val = atomicInt.accumulateAndGet(amount, LockController::sum);
					p = service.insertProduct();
				} finally {
					// 确保在业务逻辑执行后释放锁
					lock.unlock();
				}
			} else {
				log.warn("Failed to acquire lock within timeout period");
				return "lock acquisition timeout";
			}
		} catch (InterruptedException e) {
			log.error("Lock interrupted: {}", e.getMessage(), e);
			Thread.currentThread().interrupt(); // 重新设置中断标志
		} catch (Exception e) {
			log.error("Error during lock operation: {}", e.getMessage(), e);
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
