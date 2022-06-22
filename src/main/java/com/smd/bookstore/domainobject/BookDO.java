package com.smd.bookstore.domainobject;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.smd.bookstore.domainvalue.BookType;

@Entity
@Table(name = "book", uniqueConstraints = @UniqueConstraint(name = "uc_isbn", columnNames = { "isbn" }))
public class BookDO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	@NotNull(message = "Book Name can not be null!")
	private String name;

	@Column(name = "description", nullable = false)
	@NotNull(message = "Book Description can not be null!")
	private String description;

	@Column(name = "author", nullable = false)
	@NotNull(message = "Author Name can not be null!")
	private String author;

	@Enumerated(EnumType.STRING)
	@Column(name = "book_type", nullable = false)
	private BookType bookType;

	@Column
	private Double price;

	@Column(name = "isbn", nullable = false)
	@NotNull(message = "ISBN can not be null!")
	private String isbn;

	@Column(nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime dateCreated = ZonedDateTime.now();

	@Column(nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime dateModified = ZonedDateTime.now();

	@Column(nullable = false)
	private Boolean deleted = false;

	@Transient
	private List<String> isbns;

	@Transient
	private String promotion;

	@Transient
	private double checkoutPrice;

	private BookDO() {
	}

	public BookDO(String name, String description, String author, BookType bookType, Double price, String isbn) {
		super();
		this.name = name;
		this.description = description;
		this.author = author;
		this.bookType = bookType;
		this.price = price;
		this.isbn = isbn;
	}

	public BookDO(List<String> isbns, String promotion, double price) {
		super();
		this.isbns = isbns;
		this.promotion = promotion;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BookType getBookType() {
		return bookType;
	}

	public void setBookType(BookType bookType) {
		this.bookType = bookType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public ZonedDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(ZonedDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public ZonedDateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(ZonedDateTime dateModified) {
		this.dateModified = dateModified;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<String> getIsbns() {
		return isbns;
	}

	public String getPromotion() {
		return promotion;
	}

}