package com.unilib.api.service;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.book.BookRequestDTO;
import com.unilib.api.domain.book.BookResponseDTO;
import com.unilib.api.domain.category.Category;
import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.permission.Permission;
import com.unilib.api.domain.permission.Permissions;
import com.unilib.api.domain.user.User;
import com.unilib.api.repositories.BooksRepository;
import com.unilib.api.repositories.CategoriesRepository;
import com.unilib.api.repositories.CompaniesRepository;
import com.unilib.api.repositories.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class BooksService {
    @Autowired
    private BooksRepository repository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CompaniesService companiesService;

    public Book createBook(BookRequestDTO data) {
        String imgUrl = null;
        String pdf = null;


        if(data.image() != null){
            imgUrl = this.uploadFile(data.image());
        }

        if(data.pdf() != null){
            pdf = this.uploadFile(data.pdf());
        }

        Boolean hasPermission = this.companiesService.userHasPermission(data.company_id(), "manager");

        if(!hasPermission) {
            throw new IllegalArgumentException("You don't have permission to create books");
        }

        Company company = this.companiesService.getByID(data.company_id());

        Book newBook = new Book();

        newBook.setTitle(data.title());
        newBook.setDescription(data.description());
        newBook.setImage(imgUrl);
        newBook.setPdf(pdf);
        newBook.setCompany(company);
        newBook.setCreatedAt(new Date());

        if (data.categories().isPresent()) {
            List<UUID> categoryIds = data.categories().get();
            List<Category> categories = this.categoriesRepository.findAllById(categoryIds);

            // Transforma em Set<>
            Set<Category> categoriesSet = new HashSet<>(categories);

            newBook.setCategories(categoriesSet);
        }

        this.repository.save(newBook);

        return newBook;
    }

    public Book getByID(UUID id){
        return this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

    }

    public List<BookResponseDTO> getBooks(String title, String sort, int page, int size){
        title = (title != null) ? title : "";

        Sort sortOrder = Sort.by(Sort.Direction.ASC, "createdAt"); // Default sorting
        if ("desc".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Book> booksPage = this.repository.findFilteredBooks(title, pageable);

        //Isso aq é pra no futuro retornar paginação (total_pages, total_items, etc)
        return booksPage.map(book -> new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getImage(),
                book.getPdf(),
                book.getCompany(),
                book.getReviews(),
                book.getCategories(),
                book.getCreatedAt()
        )).stream().toList();
    }

    public Void deleteBook(UUID bookId){
        Optional<Book> book = this.repository.findById(bookId);

        if(book.isEmpty()){
            throw new IllegalArgumentException("Book not found");
        }

        UUID companyID = book.get().getCompany().getId();

        if(!this.companiesService.userHasPermission(companyID, "manager")){
            throw new IllegalArgumentException("You don't have permission to delete books.");
        }

        this.repository.delete(book.get());

       return null;
    }

    private String uploadFile(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();

        if (contentType != null && contentType.startsWith("image/")) {
            return "https://example.com/uploaded/image.png";
        }

        if ("application/pdf".equals(contentType)) {
            return "https://example.com/uploaded/pdf";
        }

        // Tipo de arquivo não suportado
        throw new IllegalArgumentException("Apenas imagens e PDFs são suportados.");
    }
}
