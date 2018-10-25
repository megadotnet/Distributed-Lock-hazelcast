package com.megadotnet.distributedlock.service;

import java.util.List;

import com.megadotnet.distributedlock.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

	public Product insertProduct();
	
	public List<String> listAllProducts();

	long deleteAll();

}
