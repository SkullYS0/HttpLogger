package me.skully.utils;

import me.skully.Main;

import java.sql.*;
import java.util.ArrayList;

public class MySQL {

    static Connection connection;

    public static MySQL instance = new MySQL();

    public Connection getConnection(String host, String port, String username, String password, String table) {
        try {
            if (this.connection != null && !this.connection.isClosed() && !this.connection.isValid(20)) {
                return this.connection;
            }

            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + table + "?autoReconnect=true", username, password);
        } catch (SQLException var2) {
            System.out.println("[skullyspace] MySQL error!");
        }

        return this.connection;
    }

    public boolean getter(String query) {

        boolean contains = false;
        try {
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                contains = true;
            }
            st.close();
        } catch (Exception ex) {
            contains = false;
            System.out.println("not contains");
        }

        return contains;
    }

    public int getInt(String what,String query) {
        int am = 0;
        try {
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                am = rs.getInt(what);
            }
            st.close();
        } catch (Exception ex) {
            am = 0;
            System.out.println("not contains");
        }
        return am;
    }

    public String getString(String what,String query) {
        String am = null;
        try {
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                am = rs.getString(what);
            }
            st.close();
        } catch (Exception ex) {
            am = null;
            System.out.println("not contains");
        }
        return am;
    }

    public ArrayList<String> getArrayList(String what,String query) {
        ArrayList<String> arr = new ArrayList<>();

        try {
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                arr.add(rs.getString(what));
            }
            st.close();
        } catch (Exception ex) {
            arr = null;
            System.out.println("not contains");
        }

        return arr;
    }

    public void execute(String query){
        try {
            /*PreparedStatement statement = this.getConnection("116.202.129.162",
                    "3306",
                    "u16_Q4lbr45szm",
                    "W0B4hK@=b+cPgJw.V2tTtV6.",
                    "s16_skullyspace").prepareStatement(query);*/
            PreparedStatement statement = this.getConnection(Main.dbHost,Main.dbPort,Main.dbUser,Main.dbPassword,Main.dbName).prepareStatement(query);
            statement.execute();
            statement.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}
