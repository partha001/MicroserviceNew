package com.partha.adminApplication.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="author")
	private String author;
	
	@Column(name="category")
	private String category;
	
	@Column(name="availableUnits")
	private Integer availableUnits;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="enabled")
	private Boolean enabled;
	
//	@Column(name="insertDate")
//	private Date insertDate;
//	
//	@Column(name="updateDate")
//	private Date updateDate;
	
}