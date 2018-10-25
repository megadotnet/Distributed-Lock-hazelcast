package com.megadotnet.distributedlock.service.impl;

import java.util.ArrayList;
import java.util.List;

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
		if (repo.count() == 0) {
			Product p = new Product();
			p.setSeq(0);
			return repo.save(p);
		}
		int seq = repo.findAll(new Sort(Direction.DESC, "seq")).get(0).getSeq();
		Product p = new Product();
		p.setSeq(seq + 1);
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
