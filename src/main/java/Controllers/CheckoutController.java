/*
 */
package Controllers;

import DAOs.FoodDAO;
import DAOs.OrderDAO;
import Models.Cart;
import Models.CartItem;
import Models.Food;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LEGION
 */
public class CheckoutController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckoutController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lưu trữ URL hiện tại vào session attribute
        HttpSession session = request.getSession();
        session.setAttribute("previousUrl", request.getRequestURI());
        //processRequest(request, response);
//        HttpSession session = request.getSession();
//        Cart cart = (Cart) object1;
//        OrderDAO odao = new OrderDAO();
//        odao.insertOrderStatusNotCFYet(cart);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            request.setAttribute("mess", "Giỏ hàng trống, vui lòng chọn món để thanh toán");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }
        if (cart == null) {
            cart = new Cart();
        }
        String quantityParam = "";
        for (CartItem cartItem : cart.getItems()) {
            Short foodId = cartItem.getFood().getFoodID();

            if (request.getParameter("quantity-" + foodId) != null) {
              // Thông thường nếu truy cập /checkout thì sẽ có 2 cách truy cập:
              // 1 là từ nút Thanh toán (từ modal Giỏ hàng)
              // 2 là từ nút Đặt món (từ chính trang /checkout)
              // Tất cả đều sử dụng POST và dữ liệu lấy từ form của trang trước đó
              // nên ta sẽ lấy dữ liệu từ parameter
                quantityParam = request.getParameter("quantity-" + foodId);
            } else if (session.getAttribute("quantity-" + foodId) != null) {
              // Tuy nhiên nếu yêu cầu là GET, ví dụ như sau khi đăng nhập
              // hoặc đăng xuất thành công thì trả về trang hiện tại tức /checkout 
              // thì parameter của request sau khi submit form không còn,
              // nên ta phải lưu giá trị của form bằng session attribute,
              // do đó ta phải lấy từ session
              quantityParam = (String) session.getAttribute("quantity-" + foodId);
            }
            int quantity = Integer.parseInt(quantityParam);
            cartItem.setFoodQuantity(quantity); // Cập nhật số lượng cho mục trong giỏ hàng
            // Lưu lại số lượng của các mục trong trường hợp request là GET
            // ví dụ như sau khi đăng nhập/đăng xuất thành công tại /checkout
            session.setAttribute("quantity-" + foodId, quantityParam);
        }
        session.setAttribute("cart", cart);
        
        
        request.getRequestDispatcher("checkout.jsp").forward(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
