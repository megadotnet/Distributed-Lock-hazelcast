package com.megadotnet.distributedlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megadotnet.distributedlock.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
