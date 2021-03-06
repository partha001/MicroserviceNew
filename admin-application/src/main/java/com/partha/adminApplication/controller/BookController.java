package com.partha.adminApplication.controller;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.partha.adminApplication.dto.BookDto;
import com.partha.adminApplication.entities.Book;
import com.partha.adminApplication.model.BookModel;
import com.partha.adminApplication.service.BookService;

@Controller
public class BookController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookService;

	@GetMapping(value="/addBook")
	public ModelAndView addBookGet(Model model){
		logger.info("inside BookController.addBookGet()");
		ModelAndView mv = new ModelAndView("addBook");
		mv.addObject("module", "addBook");
		mv.addObject("book", new BookModel());
		return mv;
	}

	@PostMapping(value="/addBook")
	public ModelAndView addBookPost(@ModelAttribute("book") @Valid BookModel book, 
			BindingResult bindingResult,
			Model model,
			HttpServletRequest req){
		logger.info("inside BookController.addBookPost()");
		ModelAndView mv = new ModelAndView("addBook");
		mv.addObject("module", "addBook");
		if("add".equals(req.getParameter("actionType"))){
			try{
				if(bindingResult.hasErrors()){
					return mv;
				}else{
					bookService.addBook(book);

					mv.addObject("book",new BookModel());
					mv.addObject("message","book saved successfully");
					System.out.println("book saved successfully");
				}
			}
			catch(DataIntegrityViolationException ex){
				logger.error("inside BookController.addBookPost()",ex);
				ex.printStackTrace();
				mv.addObject("message", "title already exists in database");
			}
			catch(Exception ex){
				logger.error("inside BookController.addBookPost()",ex);
				ex.printStackTrace();
				mv.addObject("message", "please try later");
			}
		}else{
			mv.addObject("book", new BookModel());
		}
		return mv;
	}

	@PostMapping(value="/viewDeleteBook")
	public ModelAndView viewDeleteBook(HttpServletRequest req){
		logger.info("inside BookController.viewDeleteBook()");
		ModelAndView mv = new ModelAndView();
		if("delete".equals(req.getParameter("action"))){
			bookService.deleteBook(Integer.parseInt(req.getParameter("id")));
			mv.setViewName("books");
			mv.addObject("books",bookService.showBooks());
			mv.addObject("module", "books");
		}else{
			mv.setViewName("viewBookDetails");
			int bookId = Integer.parseInt(req.getParameter("id"));
			mv.addObject("book", bookService.getBookDetails(bookId));
		}		
		return mv;
	}	

	@GetMapping(value="/editBook")
	public ModelAndView editBookGet(Model model,HttpServletRequest req){
		logger.info("inside BookController.editBookGet()");
		ModelAndView mv = new ModelAndView("editBook");
		int id= Integer.parseInt(req.getParameter("id"));
		System.out.println(req.getParameter("id"));
		mv.addObject("module", "editBook");
		BookDto bookDetails = bookService.getBookDetails(id);
		BookModel bookModel = BookModel.builder()
				.id(bookDetails.getId())
				.bookName(bookDetails.getTitle())
				.author(bookDetails.getAuthor())
				.publisher(bookDetails.getPublisher())
				.pages(bookDetails.getPages())
				.availableUnits(bookDetails.getAvailableUnits())
				.active("active".equalsIgnoreCase(bookDetails.getStatus())? true : false)
				.description(bookDetails.getDescription())
				.category(bookDetails.getCategory())
				.format(bookDetails.getFormat())
				.price(bookDetails.getPrice())
				.isbn(bookDetails.getIsbn())
				.existingImage(bookDetails.getImage())
				.language(bookDetails.getLanguage())
				.build();
		mv.addObject("book", bookModel);
		return mv;
	}


	@PostMapping(value="/editBook")
	public ModelAndView editBookPost(@ModelAttribute("book") @Valid BookModel book, 
			BindingResult bindingResult,
			Model model,
			HttpServletRequest req){
		logger.info("inside BookController.editBookPost()");
		ModelAndView mv = new ModelAndView("editBook");
		mv.addObject("module", "editBook");
		if("update".equals(req.getParameter("actionType"))){
			try{
				if(bindingResult.hasErrors()){
					return mv;
				}else{
					Book updatedBook = bookService.updateBook(book);
					book.setExistingImage( Base64.getEncoder().encodeToString(updatedBook.getImage()));

					mv.addObject("book",book);
					mv.addObject("message","book details updated successfully");
					System.out.println("book updated successfully");
				}
			}
			catch(DataIntegrityViolationException ex){
				logger.error("inside BookController.editBookPost()",ex);
				ex.printStackTrace();
				mv.addObject("message", "title already exists in database");
			}
			catch(Exception ex){
				logger.error("inside BookController.editBookPost()",ex);
				ex.printStackTrace();
				mv.addObject("message", "please try later");
			}
		}else{
			BookDto bookDetails = bookService.getBookDetails(book.getId());
			BookModel bookModel = BookModel.builder()
					.id(bookDetails.getId())
					.bookName(bookDetails.getTitle())
					.author(bookDetails.getAuthor())
					.publisher(bookDetails.getPublisher())
					.pages(bookDetails.getPages())
					.availableUnits(bookDetails.getAvailableUnits())
					.active("active".equalsIgnoreCase(bookDetails.getStatus())? true : false)
					.description(bookDetails.getDescription())
					.category(bookDetails.getCategory())
					.format(bookDetails.getFormat())
					.price(bookDetails.getPrice())
					.isbn(bookDetails.getIsbn())
					.existingImage(bookDetails.getImage())
					.language(bookDetails.getLanguage())
					.build();
			mv.addObject("book", bookModel);
		}
		return mv;
	}



	@GetMapping(value="/books")
	public ModelAndView showBooks(Model model){
		logger.info("inside BookController.showBooks()");
		ModelAndView mv = new ModelAndView("books");
		mv.addObject("books",bookService.showBooks());
		mv.addObject("module", "books");
		return mv;
	}


}
