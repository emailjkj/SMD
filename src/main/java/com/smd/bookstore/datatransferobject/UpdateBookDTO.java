package com.smd.bookstore.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smd.bookstore.domainvalue.BookType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateBookDTO {

	private String name;

	private String description;

	private String author;

	private BookType bookType;

	private Double price;

	private UpdateBookDTO(String name, String description, String author, BookType bookType, Double price) {
		super();
		this.name = name;
		this.description = description;
		this.author = author;
		this.bookType = bookType;
		this.price = price;
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

	public static BookDTOBuilder newBuilder() {
		return new BookDTOBuilder();
	}

	public static class BookDTOBuilder {

		private String name;
		private String description;
		private String author;
		private BookType bookType;
		private Double price;

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

		public UpdateBookDTO createBookDTO() {
			return new UpdateBookDTO(name, description, author, bookType, price);
		}

	}

}
