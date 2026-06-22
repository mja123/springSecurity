package com.mja123.security.persistence.mapper;

import com.mja123.security.domain.dto.BookDTO;
import com.mja123.security.domain.dto.UpdateBookDTO;
import com.mja123.security.persistence.entity.BookEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "publishedYear", source = "publishedYear")
    BookDTO entityToBook(BookEntity entity);
    @InheritInverseConfiguration
    BookEntity bookToEntity(BookDTO bookDTO);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "publishedYear", source = "publishedYear")
    UpdateBookDTO entityToUpdateBook(BookEntity entity);
    @InheritInverseConfiguration
    BookEntity updateBookToEntity(UpdateBookDTO bookDTO);

    List<BookDTO> entitiesToBooks(Iterable<BookEntity> entities);
}
