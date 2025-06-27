package com.megadotnet.distributedlock.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.megadotnet.distributedlock.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.megadotnet.distributedlock.entity.Product;
import com.megadotnet.distributedlock.service.ProductService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repo;

	@Override
	public Product insertProduct() {
		// Get the current maximum sequence number safely
		Optional<Integer> maxSeq = repo.findMaxSeq();
		int nextSeq = maxSeq.orElse(-1) + 1;

		Product p = new Product();
		p.setSeq(nextSeq);
		return repo.save(p);
	}

	@Override
	public List<String> listAllProducts() {
		List<String> l = new ArrayList<>();
		List<Product> ps = repo.findAll();
		ps.forEach((p) -> l.add(p.toString()));
		return l;
	}

	@Override
	public long deleteAll() {
		long c = repo.count();
		repo.deleteAll();
		return c;

	}

}
