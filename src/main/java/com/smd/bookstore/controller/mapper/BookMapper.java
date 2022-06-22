package com.smd.bookstore.controller.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.smd.bookstore.datatransferobject.BookDTO;
import com.smd.bookstore.datatransferobject.CheckoutDTO;
import com.smd.bookstore.datatransferobject.UpdateBookDTO;
import com.smd.bookstore.domainobject.BookDO;

public class BookMapper {

	private BookMapper() {
	}

	public static BookDO makeBookDO(BookDTO bookDTO) {
		return new BookDO(bookDTO.getName(), bookDTO.getDescription(), bookDTO.getAuthor(), bookDTO.getBookType(),
				bookDTO.getPrice(), bookDTO.getIsbn());

	}

	public static BookDTO makeBookDTO(BookDO bookDO) {
		BookDTO.BookDTOBuilder bookDTOBuilder = BookDTO.newBuilder().setIsbn(bookDO.getIsbn()).setName(bookDO.getName())
				.setDescription(bookDO.getDescription()).setAuthor(bookDO.getAuthor()).setPrice(bookDO.getPrice())
				.setBookType(bookDO.getBookType());
		return bookDTOBuilder.createBookDTO();
	}

	public static List<BookDTO> makeBookDTOList(Collection<BookDO> books) {
		return books.stream().map(BookMapper::makeBookDTO).collect(Collectors.toList());
	}

	public static BookDO updateBookDO(UpdateBookDTO bookDTO) {
		return new BookDO(bookDTO.getName(), bookDTO.getDescription(), bookDTO.getAuthor(), bookDTO.getBookType(),
				bookDTO.getPrice(), null);

	}

	public static UpdateBookDTO updateBookDTO(BookDO bookDO) {
		UpdateBookDTO.BookDTOBuilder bookDTOBuilder = UpdateBookDTO.newBuilder().setName(bookDO.getName())
				.setDescription(bookDO.getDescription()).setAuthor(bookDO.getAuthor()).setPrice(bookDO.getPrice())
				.setBookType(bookDO.getBookType());
		return bookDTOBuilder.createBookDTO();
	}

	public static BookDO checkoutBookDO(CheckoutDTO checkoutDTO) {
		return new BookDO(checkoutDTO.getIsbn(), checkoutDTO.getPromotion(), 0.);
	}

	public static CheckoutDTO checkoutPriceDTO(BookDO bookDO) {
		CheckoutDTO.BookDTOBuilder checkoutDTOBuilder = CheckoutDTO.newBuilder().setPrice(bookDO.getPrice())
				.setIsbn(bookDO.getIsbns());
		return checkoutDTOBuilder.responseCheckoutDTO();
	}

}
