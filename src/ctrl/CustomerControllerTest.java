package ctrl;

import db.CustomerDBIF;
import db.DataAccessException;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerControllerTest {

    private CustomerController controller;

    // Fake database implementation for testing
    class FakeCustomerDB implements CustomerDBIF {
        @Override
        public List<Customer> findAllCustomers() throws DataAccessException {
            Customer c1 = new Customer("John", "Main Street 1", "9000", "12345678", "Privat");
            Customer c2 = new Customer("Jane", "Main Street 2", "9000", "87654321", "Privat");
            return Arrays.asList(c1, c2);
        }

        @Override
        public void upsert(Customer customer) throws DataAccessException {
            // Not needed for this test
        }

        @Override
        public Customer findCustomerByPhone(int phone) throws DataAccessException {
            // Not needed for this test
            return null;
        }
    }

    @BeforeEach
    void setUp() {
        controller = new CustomerController();
        controller.customerDB = new FakeCustomerDB(); // Replace real DB with fake one
    }

    @Test
    void testFindCustomerByPhone_returnsCorrectCustomer() throws Exception {
        Customer result = controller.findCustomerByPhone(12345678);

        assertNotNull(result, "Customer should not be null");
        assertEquals("John", result.getName(), "Should return the customer with phone 12345678");
    }

    @Test
    void testFindCustomerByPhone_returnsNullIfNotFound() throws Exception {
        Customer result = controller.findCustomerByPhone(99999999);

        assertNull(result, "Should return null if no customer matches");
    }
}
