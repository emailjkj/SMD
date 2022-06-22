package com.smd.bookstore.service;

import com.smd.bookstore.domainobject.BookDO;
import com.smd.bookstore.exception.ConstraintsViolationException;
import com.smd.bookstore.exception.DuplicateEntityException;
import com.smd.bookstore.exception.EntityNotFoundException;

public interface BookService {

	BookDO create(BookDO bookDO) throws ConstraintsViolationException, DuplicateEntityException;

	BookDO find(String isbn) throws EntityNotFoundException;

	void toggleStatus(String isbn, boolean status) throws EntityNotFoundException;

	void delete(String isbn) throws EntityNotFoundException;

	BookDO updateBook(String isbn, BookDO updateBookDO) throws EntityNotFoundException;

	BookDO checkout(BookDO bookDO) throws EntityNotFoundException;

}
