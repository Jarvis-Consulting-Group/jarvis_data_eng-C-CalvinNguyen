package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JdbcExecutor is the entrypoint for the program.
 * main() method uses the DatabaseConnectionManager to create a connection and uses that
 * connection to execute statements that returns a ResultSet.
 */
public class JdbcExecutor {

  final Logger logger = LoggerFactory.getLogger(JdbcExecutor.class);

  /**
   * main() method creates a connection using the DatabaseConnectionManager and executes
   * a statement to get the count of all customers in the customers table.
   * @param args string array args.
   */
  public static void main(String[] args) {
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "hplussport", "postgres", "password");

    BasicConfigurator.configure();

    JdbcExecutor jdbcExecutor = new JdbcExecutor();

    try {
      Connection connection = dcm.getConnection();
      CustomerDao customerDao = new CustomerDao(connection);
      Customer customer = customerDao.findById(10000);

      jdbcExecutor.logger.info(customer.getFirstName() + " " + customer.getLastName() + " "
          + customer.getEmail());

      customer.setEmail("gwashington@wh.gov");
      customer = customerDao.update(customer);

      jdbcExecutor.logger.info(customer.getFirstName() + " " + customer.getLastName() + " "
          + customer.getEmail());
    } catch (SQLException e) {
      jdbcExecutor.logger.error("Error: SQLException: ", e);
    }

  }
}
