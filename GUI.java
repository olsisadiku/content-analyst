import java.sql.*;
import java.awt.event.*;
import java.lang.Thread.State;

import javax.swing.*;

/*
  TODO:
  1) Change credentials for your own team's database
  2) Change SQL command to a relevant query that retrieves a small amount of data
  3) Create a JTextArea object using the queried data
  4) Add the new object to the JPanel p
*/

public class GUI extends JFrame implements ActionListener {
    static JFrame f;

    public static void main(String[] args)
    {
      //Building the connection
      
      // create a new frame
      f = new JFrame("DB GUI");

      // create a object
      GUI s = new GUI();

      // create a panel
      JPanel p = new JPanel();

      JButton b = new JButton("Close");

      // add actionlistener to button
      b.addActionListener(s);

      //TODO Step 3
        JTextArea movies = new JTextArea(20,20);



      //TODO Step 4
        p.add(movies);
        JTextArea start_date = new JTextArea(1,5);
        JTextArea end_date = new JTextArea(1,5);
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");
        p.add(start_date);
        p.add(end_date);
        p.add(submit); 
        p.add(clear);
        submit.addActionListener(new ActionListener(){
          public void actionPerformed(final ActionEvent e) {
            Connection conn = null;
            try {
              Class.forName("org.postgresql.Driver");
              conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315902_9db",
                "csce315902_9user", "rotc1738");
            } catch (Exception f) {
              f.printStackTrace();
              System.err.println(e.getClass().getName()+": "+f.getMessage());
              System.exit(0);
            }
            JOptionPane.showMessageDialog(null,"Opened database successfully");

            String name = "";
            try{
              //create a statement object
              Statement stmt = conn.createStatement();
              //create an SQL statement
              //TODO Step 2
              String start = start_date.getText();
              String end = end_date.getText();
              String sqlStatement = "select medianame from tvshowsandmovies where titleid in (select titleid from users where (date >= '"+ start + "' and date <= '"+ end + "') group by titleid order by count(*) desc limit 10);";
              //send statement to DBMS
              ResultSet result = stmt.executeQuery(sqlStatement);
              while (result.next()) {
                name += result.getString("medianame")+"\n";
              }
              movies.append(name);
            } catch (Exception f){
              JOptionPane.showMessageDialog(null,"Query cannot be made");
            }
            try {
              conn.close();
            } catch(Exception f) {
                System.out.println("Connection has an error");
            }
            }
        });

        clear.addActionListener(new ActionListener(){
          public void actionPerformed(final ActionEvent e){
            movies.setText("");
          }
        });
      // add button to panel
      p.add(b);

      // add panel to frame
      f.add(p);

      // set the size of frame
      f.setSize(400, 400);

      f.show();

      //closing the connection
      
    }

    // if button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}