package com.smd.bookstore.controller;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smd.bookstore.controller.mapper.BookMapper;
import com.smd.bookstore.datatransferobject.BookDTO;
import com.smd.bookstore.datatransferobject.CheckoutDTO;
import com.smd.bookstore.datatransferobject.UpdateBookDTO;
import com.smd.bookstore.domainobject.BookDO;
import com.smd.bookstore.exception.ConstraintsViolationException;
import com.smd.bookstore.exception.DuplicateEntityException;
import com.smd.bookstore.exception.EntityNotFoundException;
import com.smd.bookstore.service.BookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * All operations with a book will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/books")
@Api(tags = "Operations pertaining to Books")
public class BookController {

	private final BookService bookService;

	public BookController(@Qualifier("book1") final BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping("/{isbn}")
	@ApiOperation(value = "View Book Details")
	@ApiResponses(value = { @ApiResponse(code = 200, response = BookDTO.class, message = ""),
			@ApiResponse(code = 404, message = "Could not find book with the given ISBN") })
	public BookDTO getBook(
			@PathVariable @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", message = "Invalid ISBN") String isbn)
			throws EntityNotFoundException {
		return BookMapper.makeBookDTO(bookService.find(isbn));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create Book")
	@ApiResponses(value = { @ApiResponse(code = 201, response = BookDTO.class, message = ""),
			@ApiResponse(code = 400, message = "Constraint violation"),
			@ApiResponse(code = 409, message = "Please use another ISBN and retry.") })
	public BookDTO createBook(@Valid @RequestBody BookDTO bookDTO)
			throws ConstraintsViolationException, DuplicateEntityException {
		BookDO bookDO = BookMapper.makeBookDO(bookDTO);
		return BookMapper.makeBookDTO(bookService.create(bookDO));
	}

	@PatchMapping("/{isbn}/{status}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Enable/Disable Book")
	@ApiResponses(value = { @ApiResponse(code = 204, message = ""),
			@ApiResponse(code = 404, message = "Could not find book with id: XX") })
	public void toggleBookStatus(@PathVariable String isbn, @PathVariable boolean status)
			throws EntityNotFoundException {
		bookService.toggleStatus(isbn, status);
	}

	@DeleteMapping("/{isbn}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete Book")
	@ApiResponses(value = { @ApiResponse(code = 204, message = ""),
			@ApiResponse(code = 404, message = "Could not find book with id: XX") })
	public void deleteBook(@PathVariable String isbn) throws EntityNotFoundException {
		bookService.delete(isbn);
	}

	@PutMapping("/{isbn}")
	@ApiOperation(value = "Update Book")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "Could not find book with id: XX") })
	public UpdateBookDTO updateBook(@PathVariable String isbn, @Valid @RequestBody UpdateBookDTO bookDTO)
			throws EntityNotFoundException {
		BookDO updateBookDO = BookMapper.updateBookDO(bookDTO);
		return BookMapper.updateBookDTO(bookService.updateBook(isbn, updateBookDO));
	}

	@PostMapping("/{checkout}")
	@ApiOperation(value = "Checkout Books")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Constraint violation"),
			@ApiResponse(code = 409, message = "Please use another ISBN and retry.") })
	public ResponseEntity<CheckoutDTO> checkoutBooks(@Valid @RequestBody CheckoutDTO checkoutDTO)
			throws ConstraintsViolationException, DuplicateEntityException, EntityNotFoundException {
		BookDO bookDO = BookMapper.checkoutBookDO(checkoutDTO);
		CheckoutDTO responseDto = BookMapper.checkoutPriceDTO(bookService.checkout(bookDO));
		return responseDto.getIsbn().isEmpty() ? ResponseEntity.ok(responseDto)
				: ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(responseDto);
	}

}
