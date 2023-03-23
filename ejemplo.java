import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ejemplo {

    private  static Connection c;
    private static CallableStatement cstm;
    private static ResultSet rs;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("bienvenido al sistema");
        System.out.println("Ingrese el usuario");
        String user = sc.next();
        System.out.println("Ingrese la contraseña");
        String password = sc.next();
        sc.close();


        List<BeanUser> listUsers = new ArrayList<>();

        listUsers = getUsers(user,password);
        listUsers.forEach(u -> System.out.println(u.getName()));
     
    }

    public static List<BeanUser> getUsers(String p_user , String p_password){
        List<BeanUser> listUsers = new ArrayList<>();
        try {
            c = getConnection(p_user,p_password);
            cstm = c.prepareCall("select * fom user");
            rs = cstm.executeQuery();

            while(rs.next()){
                BeanUser beanUser = new BeanUser();
                beanUser.setId(rs.getInt("id"));
                beanUser.setName(rs.getString("name"));

                listUsers.add(beanUser);
            }

        } catch (SQLException e) {
            System.out.println("Error" +e.getMessage());
        } finally{
            try {
                rs.close();
                cstm.close();
                c.close();
            } catch (SQLException e) {
                
            }

        }
        return listUsers;
    }

    public static Connection getConnection( String p_user , String p_password) throws SQLException {
        Connection c;
        String host = "127.0.0.1";
        String user = p_user;
        String password = p_password;
        String port = "3306";
        String database = "zythumbeer";
        boolean ssl = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Libreria no encontrada: " + e.getMessage());
        }
        String url = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=%b", host, database, user, password, ssl);
        return  DriverManager.getConnection(url);
    }
}
