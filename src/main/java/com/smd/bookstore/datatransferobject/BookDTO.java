package com.smd.bookstore.datatransferobject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smd.bookstore.domainvalue.BookType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {

	@NotBlank(message = "Book Name can not be null!")
	private String name;

	@NotBlank(message = "Book Description can not be null!")
	private String description;

	@NotBlank(message = "Author Name can not be null!")
	private String author;

	@NotNull
	private BookType bookType;

	@Min(value = 10, message = "Price can not be lower than $10")
	private Double price;

	@NotBlank(message = "ISBN can not be null!")
	@Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", message = "Invalid ISBN")
	private String isbn;

	private BookDTO(String name, String description, String author, BookType bookType, Double price, String isbn) {
		super();
		this.name = name;
		this.description = description;
		this.author = author;
		this.bookType = bookType;
		this.price = price;
		this.isbn = isbn;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public BookType getBookType() {
		return bookType;
	}

	public Double getPrice() {
		return price;
	}

	public String getIsbn() {
		return isbn;
	}

	public static BookDTOBuilder newBuilder() {
		return new BookDTOBuilder();
	}

	public static class BookDTOBuilder {

		private String name;
		private String description;
		private String author;
		private BookType bookType;
		private Double price;
		private String isbn;

		public BookDTOBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public BookDTOBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public BookDTOBuilder setAuthor(String author) {
			this.author = author;
			return this;
		}

		public BookDTOBuilder setBookType(BookType bookType) {
			this.bookType = bookType;
			return this;
		}

		public BookDTOBuilder setPrice(Double price) {
			this.price = price;
			return this;
		}

		public BookDTOBuilder setIsbn(String isbn) {
			this.isbn = isbn;
			return this;
		}

		public BookDTO createBookDTO() {
			return new BookDTO(name, description, author, bookType, price, isbn);
		}

	}

}
