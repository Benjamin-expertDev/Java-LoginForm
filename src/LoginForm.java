import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class LoginForm extends JFrame {
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;
   
        public void  initialize() {
             /*************Form Panel*******/
        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);
        

        /***************Buttons Panel ************/
        JButton btnLogin = new JButton();
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                User user = getAuthenticatedUser(email, password);

                if (user != null){
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                }    
                else {
                        JOptionPane.showMessageDialog(LoginForm.this,
                        "Email or Password Invalid",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);                    
                    
                }
                throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
            }
            
        });
        
        /**********Initialize the Frame ********/
        add(formPanel, BorderLayout.NORTH);

            setTitle("Login Form");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(400,500);
            setMinimumSize(new Dimension(350, 450));
            //setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
            
        }


        private User getAuthenticatedUser(String email, String password){
            User user = null;


            final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC.";
            final String USERNAME = "root";
            final String PASSWORD = "";

            try{
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                //connected to database successfully...

                String sql = "SELECT * FROM users WHERE email=? AND password =?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    user = new User ();
                    user.name = resultSet.getString("name");
                    user.email = resultSet.getString("email");
                    user.phone = resultSet.getString("phone");
                    user.address = resultSet.getString("address");
                    user.password = resultSet.getString("password");
                }
                preparedStatement.close();
                conn.close();
                
            }catch(Exception e){
                System.out.println("Database Connection failed");
            }
            
            return  user;
        }
}
