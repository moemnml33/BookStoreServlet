import mycartpackage.Cart;
import mycartpackage.CartItem;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // retrieve cart from session
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        // output HTML content
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Shopping Cart</title>");
        out.println("<link rel='stylesheet' href='Styles.css'></head><body>");
        out.println("<h1>My Shopping Cart</h1>");
        // if cart is not null/empty display table
        if (cart != null && !cart.getItems().isEmpty()) {
            out.println("<table class='book-item'><tr>");
            out.println("<th>Type</th><th>Price</th><th>Quantity</th><th>Total</th><th>Action</th>");
            out.println("</tr>");
            // dynamic displaying
            for (CartItem item : cart.getItems()) {
                double totalPrice = item.getBook().getPrice() * item.getQuantity();
                out.println("<tr>");
                out.println("<td>" + item.getBook().getName() + "</td>");
                out.println("<td>$" + String.format("%.2f", item.getBook().getPrice()) + "</td>");
                out.println("<td>");
                out.println("<form method='post' action='CartServlet'>");
                out.println("<input type='number' name='quantity' value='" + item.getQuantity() + "' min='1'/>");
                out.println("<input type='hidden' name='bookTitle' value='" + item.getBook().getName() + "'>");
                out.println("<button type='submit'>Update</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("<td>$" + String.format("%.2f", totalPrice) + "</td>");
                out.println("<td>");
                out.println("<form method='post' action='CartServlet'>");
                out.println("<input type='hidden' name='bookTitle' value='" + item.getBook().getName() + "'>");
                out.println("<button type='submit' name='remove'>Remove</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
         // else display cart is empty
        } else {
            out.println("<p class='book-item'>Your cart is empty.</p>");
        }

        out.println("<a href='BrowseServlet'>Continue Shopping</a>");
        out.println("<button>Checkout</button>");
        out.println("</body></html>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        String bookTitle = request.getParameter("bookTitle");
        String quantityStr = request.getParameter("quantity");

        if (cart != null && bookTitle != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getBook().getName().equals(bookTitle)) {
                    if (request.getParameter("remove") != null) {
                        // Remove item from cart
                        cart.removeItem(item);
                    } else if (quantityStr != null) {
                        // Update quantity
                        int quantity = Integer.parseInt(quantityStr);
                        item.setQuantity(quantity);
                    }
                    break;
                }
            }
        }

        // Redirect back to the cart page
        response.sendRedirect("CartServlet");
    }
}
