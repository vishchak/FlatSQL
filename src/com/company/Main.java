package com.company;


import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/flat1?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "password";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add flat");
                    System.out.println("2: view flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":
                            System.out.println("Arguments: id, district, address, area, rooms, price");
                            System.out.println("Enter the parameters (Example: area>10)");
                            String type = sc.nextLine();
                            viewFlats(type);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "district VARCHAR(30) DEFAULT NULL,address VARCHAR(30) DEFAULT NULL," +
                    "area NUMERIC(10,2),rooms SMALLINT,price NUMERIC(10,2))");
        } finally {
            st.close();
        }

    }

    private static void addFlat(Scanner sc) throws SQLException {
        System.out.print("Enter the district: ");
        String district = sc.nextLine();
        System.out.print("Enter the address: ");
        String address = sc.nextLine();
        System.out.print("Enter the area: ");
        String sArea = sc.nextLine();
        Double area = Double.parseDouble(sArea);
        System.out.println("Enter the rooms count");
        String sRooms = sc.nextLine();
        int rooms = Integer.parseInt(sRooms);
        System.out.print("Enter the price: ");
        String sPrice = sc.nextLine();
        Double price = Double.parseDouble(sPrice);


        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Flats (district, address, area, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setDouble(3, area);
            ps.setInt(4, rooms);
            ps.setDouble(5, price);
            ps.executeUpdate();
        } finally {
            ps.close();
        }

    }

    private static void viewFlats(String cond) throws SQLException {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT *  FROM Flats WHERE " + cond);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();

            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.print(md.getColumnName(i) + "\t\t");
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


