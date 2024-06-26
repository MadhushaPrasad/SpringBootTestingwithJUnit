import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rest.application.Book;
import com.rest.application.BookController;
import com.rest.application.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    Book recordOne = new Book(1L, "Harry Potter", "Test Summary", 6);
    Book recordTwo = new Book(2L, "Harry Potter", "Test Summary", 6);
    Book recordThree = new Book(3L, "Grokking Algorithms", "Test Summary", 6);

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllRecordsSuccess() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(recordOne, recordTwo, recordThree));

        Mockito.when(bookRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders.get("/book").contentType(MediaType.APPLICATION_JSON)).andDo(print())  // This will print the response to the console
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3))).andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Harry Potter")));
    }

    @Test
    public void getBookById() throws Exception {
        Mockito.when(bookRepository.findById(recordOne.getBookId())).thenReturn(java.util.Optional.of(recordOne));
        mockMvc.perform(MockMvcRequestBuilders.get("/book/1").contentType(MediaType.APPLICATION_JSON)).andDo(print())  // This will print the response to the console
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue())).andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Harry Potter")));
    }

    @Test
    public void createBookSuccess() throws Exception {
        Book bookRecord = Book.builder().bookId(1L).name("Load of the ring").summery("this is the summery of the Load of the ring book").rating(5).build();

        Mockito.when(bookRepository.save(bookRecord)).thenReturn(bookRecord);

        String resContent = objectWriter.writeValueAsString(bookRecord);


        MockHttpServletRequestBuilder mockHttpRequest = MockMvcRequestBuilders.post("/book").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(resContent);

        mockMvc.perform(mockHttpRequest).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue())).andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Load of the ring")));
    }

    @Test
    public void updateBookSuccess() throws Exception {
        Book bookRecord = Book.builder().bookId(1L).name("Load of the ring part 2").summery("this is the summery of the Load of the ring book part 2").rating(4).build();

        Mockito.when(bookRepository.findById(recordOne.getBookId())).thenReturn(java.util.Optional.of(bookRecord));
        Mockito.when(bookRepository.save(bookRecord)).thenReturn(bookRecord);

        String updatedContentString = objectWriter.writeValueAsString(bookRecord);

        MockHttpServletRequestBuilder mockHttpRequest = MockMvcRequestBuilders.put("/book").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(updatedContentString);

        mockMvc.perform(mockHttpRequest).andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.name", is("Load of the ring part 2")));
    }
}
