package com.smd.bookstore.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.smd.bookstore.dataaccessobject.BookRepository;
import com.smd.bookstore.domainobject.BookDO;
import com.smd.bookstore.exception.ConstraintsViolationException;
import com.smd.bookstore.exception.DuplicateEntityException;
import com.smd.bookstore.exception.EntityNotFoundException;

/**
 * Service to encapsulate the link between DAO and controller and to have
 * business logic for some book specific things.
 * <p/>
 */
@Service("book1")
public class DefaultBookService implements BookService {

	private static final String COULD_NOT_FIND_BOOK_WITH_ISBN = "Could not find book with ISBN: ";
	private static final Logger LOG = LoggerFactory.getLogger(DefaultBookService.class);

	private final BookRepository bookRepository;

	public DefaultBookService(final BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	/**
	 * Creates a book
	 * 
	 * @param bookDO
	 * @return
	 * @throws DuplicateEntityException
	 * @throws ConstraintsViolationException
	 */
	@Override
	public BookDO create(BookDO bookDO) throws DuplicateEntityException, ConstraintsViolationException {
		BookDO book = null;
		try {
			book = bookRepository.save(bookDO);
		} catch (DataIntegrityViolationException exception) {
			LOG.error("ConstraintsViolationException while creating a book: {}", bookDO, exception);
			throw new ConstraintsViolationException("Please use another ISBN and retry.");
		}
		return book;
	}

	/**
	 * Find book by ISBN
	 * 
	 * @param isbn
	 * @return
	 * @throws EntityNotFoundException
	 */
	@Override
	public BookDO find(String isbn) throws EntityNotFoundException {
		return findBookChecked(isbn, false);
	}

	/**
	 * Delete book by it's ISBN
	 * 
	 * @param isbn
	 * @throws EntityNotFoundException
	 */
	@Override
	public void delete(String isbn) throws EntityNotFoundException {
		var bookDO = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_BOOK_WITH_ISBN + isbn));
		bookRepository.delete(bookDO);
	}

	private BookDO findBookChecked(String isbn, boolean isDeleted) throws EntityNotFoundException {
		return bookRepository.findByIsbnAndDeleted(isbn, isDeleted)
				.orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_BOOK_WITH_ISBN + isbn));
	}

	/**
	 * Enable/Disable book
	 * 
	 * @param isbn
	 * @param isDeleted
	 */
	@Override
	@Transactional
	public void toggleStatus(String isbn, boolean status) throws EntityNotFoundException {
		var bookDO = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_BOOK_WITH_ISBN + isbn));
		bookDO.setDeleted(status);
		bookDO.setDateModified(ZonedDateTime.now());
	}

	/**
	 * Update book by ISBN
	 * 
	 * @param isbn
	 * @param bookDO
	 * @throws EntityNotFoundException
	 */
	@Override
	@Transactional
	public BookDO updateBook(String isbn, BookDO updateBookDO) throws EntityNotFoundException {
		var bookDO = findBookChecked(isbn, false);
		bookDO.setAuthor(updateBookDO.getAuthor());
		bookDO.setName(updateBookDO.getName());
		bookDO.setDescription(updateBookDO.getDescription());
		bookDO.setBookType(updateBookDO.getBookType());
		bookDO.setPrice(updateBookDO.getPrice());
		bookDO.setDateModified(ZonedDateTime.now());
		return bookDO;
	}

	/**
	 * Checkout books by ISBNs
	 * 
	 * @param bookDO
	 * @throws EntityNotFoundException
	 * 
	 */
	@Override
	public BookDO checkout(BookDO checkoutBookDO) throws EntityNotFoundException {
		var bookDOs = bookRepository.findByIsbnInAndDeleted(checkoutBookDO.getIsbns(), false);
		if (bookDOs.isEmpty()) {
			throw new EntityNotFoundException(COULD_NOT_FIND_BOOK_WITH_ISBN + checkoutBookDO.getIsbns());
		}
		var prices = new ArrayList<Double>();
		var processedIsbns = new ArrayList<String>();
		var promoCode = null != checkoutBookDO.getPromotion() ? checkoutBookDO.getPromotion() : "";
		bookDOs.forEach(bookDO -> {
			prices.add("dxb-promo".equalsIgnoreCase(promoCode)
					? Double.sum(bookDO.getPrice(), -bookDO.getPrice() * bookDO.getBookType().getDiscount() / 100)
					: bookDO.getPrice());
			processedIsbns.add(bookDO.getIsbn());
		});
		return new BookDO(checkoutBookDO.getIsbns().stream().filter(isbn -> !processedIsbns.contains(isbn))
				.collect(Collectors.toList()), null, prices.stream().reduce(0., Double::sum));
	}

}
