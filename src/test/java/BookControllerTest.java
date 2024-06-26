import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rest.application.Book;
import com.rest.application.BookController;
import com.rest.application.BookRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    Book recordOne = new Book(1L, "Harry Potter", "Test Summery", 6);
    Book recordTwo = new Book(2L, "Harry Potter", "Test Summery", 6);
    private Mockito mockito;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup().build();
    }
}
