package com.smd.bookstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.smd.bookstore.dataaccessobject.BookRepository;
import com.smd.bookstore.domainobject.BookDO;
import com.smd.bookstore.domainvalue.BookType;
import com.smd.bookstore.exception.ConstraintsViolationException;
import com.smd.bookstore.exception.DuplicateEntityException;
import com.smd.bookstore.exception.EntityNotFoundException;
import com.smd.bookstore.service.DefaultBookService;

@ExtendWith(MockitoExtension.class)
class BookstoreServiceTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private DefaultBookService bookService;

	@Test
	void shouldCheckoutBooks() throws EntityNotFoundException {
		Mockito.when(bookRepository.findByIsbnInAndDeleted(Mockito.anyList(), Mockito.anyBoolean()))
				.thenAnswer(invocation -> {
					List<BookDO> bookDOs = new ArrayList<>();
					bookDOs.add(createBookDO());
					return bookDOs;
				});
		var bookDO = bookService.checkout(checkoutBookDO());
		assertNotNull(bookDO);
		assertEquals(139.5, bookDO.getPrice());
	}

	@Test
	void shouldCheckoutBooksThrowNotFoundError() {
		Mockito.when(bookRepository.findByIsbnInAndDeleted(ArgumentMatchers.anyList(), ArgumentMatchers.anyBoolean()))
				.thenAnswer(invocation -> {
					return new ArrayList<BookDO>();
				});
		assertThrows(EntityNotFoundException.class, () -> bookService.checkout(checkoutBookDO()));
	}

	@Test
	void shouldCreateBook() throws ConstraintsViolationException, DuplicateEntityException {
		Mockito.when(bookRepository.save(Mockito.any(BookDO.class))).thenReturn(createBookDO());
		var bookDO = bookService.create(createBookDO());
		assertNotNull(bookDO);
		assertEquals("The Hunger Games", bookDO.getName());
	}

	@Test
	void shouldCreateBookThrowConstraintsViolationError() {
		Mockito.when(bookRepository.save(Mockito.any(BookDO.class)))
				.thenThrow(new DataIntegrityViolationException("Please use another ISBN and retry."));
		assertThrows(ConstraintsViolationException.class, () -> bookService.create(createBookDO()));
	}

	@Test
	void shouldFindBookByIsbn() throws EntityNotFoundException {
		Mockito.when(bookRepository.findByIsbnAndDeleted(ArgumentMatchers.eq("978-1-56619-909-4"),
				ArgumentMatchers.eq(false))).thenReturn(createOptionalBookDO());
		var bookDO = bookService.find("978-1-56619-909-4");
		assertNotNull(bookDO);
		assertEquals(BookType.COMIC, bookDO.getBookType());
	}

	@Test
	void shouldFindBookByIsbnThrowNotFoundError() {
		Mockito.when(bookRepository.findByIsbnAndDeleted(ArgumentMatchers.eq("978-1-56619-909-6"),
				ArgumentMatchers.eq(false))).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> bookService.find("978-1-56619-909-6"));
	}

	@Test
	void shouldToggleBookStatus() throws EntityNotFoundException {
		Mockito.when(bookRepository.findByIsbn(ArgumentMatchers.eq("978-1-56619-909-4")))
				.thenReturn(createOptionalBookDO());
		bookService.toggleStatus("978-1-56619-909-4", true);
		Mockito.verify(bookRepository).findByIsbn(ArgumentMatchers.anyString());
		Mockito.verifyNoMoreInteractions(bookRepository);
	}

	@Test
	void shouldToggleStatusThrowEntityNotFoundError() {
		Mockito.when(bookRepository.findByIsbn(ArgumentMatchers.eq("978-1-56619-909-4"))).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> bookService.toggleStatus("978-1-56619-909-4", true));
	}

	@Test
	void shouldUpdateBook() throws EntityNotFoundException {
		Mockito.when(bookRepository.findByIsbnAndDeleted(ArgumentMatchers.eq("978-1-56619-909-5"),
				ArgumentMatchers.anyBoolean())).thenReturn(createOptionalBookDO());
		bookService.updateBook("978-1-56619-909-5", createBookDO());
		Mockito.verify(bookRepository).findByIsbnAndDeleted(ArgumentMatchers.anyString(),
				ArgumentMatchers.anyBoolean());
		Mockito.verifyNoMoreInteractions(bookRepository);
	}

	@Test
	void shouldUpdateBookThrowEntityNotFoundError() {
		Mockito.when(bookRepository.findByIsbnAndDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
				.thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> bookService.updateBook("978-1-56619-909-5", createBookDO()));
	}

	private BookDO createBookDO() {
		return new BookDO("The Hunger Games", "It's a novel", "Suzanne Collins", BookType.SCIENCE, 150.0,
				"978-1-56619-909-5");
	}

	private Optional<BookDO> createOptionalBookDO() {
		return Optional.of(new BookDO("The Tales Of Summer", "Summer", "Gary Williams", BookType.COMIC, 40.50,
				"978-1-56619-909-4"));
	}

	private BookDO checkoutBookDO() {
		BookDO bookDO = new BookDO(List.of("978-1-56619-909-4", "978-1-56619-909-4"), "dxb-promo", 150.56);
		bookDO.setBookType(BookType.FICTION);
		return bookDO;
	}

}
