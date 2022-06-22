package com.smd.bookstore.dataaccessobject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.smd.bookstore.domainobject.BookDO;

/**
 * Database Access Object for book table.
 * <p/>
 */
public interface BookRepository extends CrudRepository<BookDO, Long> {

	Optional<BookDO> findByIsbnAndDeleted(String isbn, boolean deleted);

	Optional<BookDO> findByIsbn(String isbn);

	List<BookDO> findByIsbnInAndDeleted(List<String> isbn, boolean deleted);

}
