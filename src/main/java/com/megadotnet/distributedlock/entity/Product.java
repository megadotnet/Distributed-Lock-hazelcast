package com.megadotnet.distributedlock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Product {

	@javax.persistence.Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private Integer seq;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "Product [seq=" + seq + "]";
	}

	
}
