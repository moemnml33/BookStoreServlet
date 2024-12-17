import mybookpackage.Book;
import mycartpackage.Cart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/BrowseServlet")
public class BrowseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // create list of books
        ArrayList<Book> bookList = new ArrayList<>();
        bookList.add(new Book("Design Patterns: Elements of Reusable Object-Oriented Software", 59.99));
        bookList.add(new Book("Patterns of Enterprise Application Architecture", 47.99));
        bookList.add(new Book("Node.js Design Patterns", 39.99));
        // retrieve cart from session or create a new one
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        // output HTML content
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Browse Books</title>");
        out.println("<link rel='stylesheet' href='Styles.css'></head><body>");
        out.println("<h1>Your Shopping Cart</h1>");
        out.println("<h2>Available Books</h2>");
        out.println("<ul class='book-list'>");
        // dynamic displaying
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            out.println("<li class='book-item'>");
            out.println("<p class='book-header'>");
            out.println("<span class='book-title'>" + book.getName() + "</span>");
            out.println("<span class='book-price'>$" + String.format("%.2f", book.getPrice()) + "</span>");
            out.println("</p>");
            out.println("<form method='post' action='BrowseServlet'>");
            out.println("<label for='quantity-" + i + "'>Quantity:</label>");
            out.println("<input type='number' id='quantity-" + i + "' name='quantity' min='1' value='1' required>");
            out.println("<input type='hidden' name='bookTitle' value='" + book.getName() + "'>");
            out.println("<input type='hidden' name='bookPrice' value='" + book.getPrice() + "'>");
            out.println("<button class='add-to-cart' type='submit'>Add to Cart</button>");
            out.println("</form>");
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("<div class='cart-icon'>");
        out.println("<a href='CartServlet'><img src='assets/shoppingcart.png' alt='Shopping Cart'>Items in Cart</a>");
        out.println("</div>");
        out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // retrieve cart from session or create a new one
		HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        // get data from form
        String bookTitle = request.getParameter("bookTitle");
        String bookPriceStr = request.getParameter("bookPrice");
        String quantityStr = request.getParameter("quantity");
        // if available add book to cart
        if (bookTitle != null && bookPriceStr != null && quantityStr != null) {
            int quantity = Integer.parseInt(quantityStr);
            double bookPrice = Double.parseDouble(bookPriceStr);

            Book book = new Book(bookTitle, bookPrice);
            cart.addItem(book, quantity);
        // stay on page 
        response.sendRedirect("BrowseServlet");
        }
	}
}
