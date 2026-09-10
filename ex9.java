<%@page import="java.sql.*"%>

<%
String customerName = request.getParameter("customer_name");
String email = request.getParameter("email");
String product = request.getParameter("product");
String quantityStr = request.getParameter("quantity");
String priceStr = request.getParameter("price");

boolean ordered = false;

if (customerName != null && email != null && product != null &&
    quantityStr != null && priceStr != null) {

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/shoppingdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            "root",
            "test@123"
        );

        int quantity = Integer.parseInt(quantityStr);
        double price = Double.parseDouble(priceStr);

        String sql = "INSERT INTO orders " +
                     "(customer_name, email, product, quantity, price) " +
                     "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, email);
        ps.setString(3, product);
        ps.setInt(4, quantity);
        ps.setDouble(5, price);

        ps.executeUpdate();

        ps.close();
        con.close();

        ordered = true;

    } catch (Exception e) {
        out.println("<h3>Error: " + e.getMessage() + "</h3>");
    }
}
%>

<!DOCTYPE html>
<html>
<head>
    <title>Online Shopping</title>

    <style>
        body {
            font-family: Arial;
            text-align: center;
        }

        table {
            margin: auto;
            border-collapse: collapse;
            width: 80%;
        }

        th, td {
            border: 1px solid black;
            padding: 10px;
        }

        th {
            background-color: lightgray;
        }

        button {
            padding: 10px 20px;
        }
    </style>
</head>

<body>

<% if (!ordered) { %>

    <h1>SHOPPING</h1>

    <form action="New1.jsp" method="post">

        Customer Name:
        <input type="text" name="customer_name" required>
        <br><br>

        Email:
        <input type="email" name="email" required>
        <br><br>

        Product:
        <select name="product" required>
            <option value="">Select Product</option>
            <option value="Mobile">DRESS</option>
            <option value="Laptop">SHOE</option>
            <option value="Tablet">COSMETICS</option>
            <option value="Headphone">PERFUME</option>
        </select>
        <br><br>

        Quantity:
        <input type="number" name="quantity" min="1" required>
        <br><br>

        Price:
        <input type="number" name="price" step="0.01" required>
        <br><br>

        <input type="submit" value="Place Order">

    </form>

<% } else { %>

    <h2>Order placed successfully!</h2>

    <h1>Order Details</h1>

    <table>

        <tr>
            <th>Customer Name</th>
            <th>Email</th>
            <th>Product</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>

<%
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/shoppingdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            "root",
            "test@123"
        );

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(
            "SELECT customer_name, email, product, quantity, price FROM orders"
        );

        while (rs.next()) {
%>

        <tr>
            <td><%= rs.getString("customer_name") %></td>
            <td><%= rs.getString("email") %></td>
            <td><%= rs.getString("product") %></td>
            <td><%= rs.getInt("quantity") %></td>
            <td>Rs. <%= rs.getDouble("price") %></td>
        </tr>

<%
        }

        rs.close();
        st.close();
        con.close();

    } catch (Exception e) {
%>

        <tr>
            <td colspan="5">
                Error: <%= e.getMessage() %>
            </td>
        </tr>

<%
    }
%>

    </table>

    <br><br>

    <a href="New1.jsp">
        <button>Book Another Item</button>
    </a>

<% } %>

</body>
</html>
