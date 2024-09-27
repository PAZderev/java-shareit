import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.StateRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItApp.class) // Указываем основной класс приложения
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking_ShouldReturnBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(); // создайте DTO с необходимыми полями
        Booking booking = new Booking(); // создайте объект Booking с необходимыми полями

        when(bookingService.createBooking(any(BookingDto.class), anyLong())).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"field\": \"value\"}")) // замените на правильный JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()));

        verify(bookingService, times(1)).createBooking(any(BookingDto.class), anyLong());
    }

    @Test
    void updateBooking_ShouldReturnUpdatedBooking() throws Exception {
        Booking booking = new Booking(); // создайте объект Booking

        when(bookingService.updateBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()));

        verify(bookingService, times(1)).updateBooking(anyLong(), anyBoolean(), anyLong());
    }

    @Test
    void getBooking_ShouldReturnBooking() throws Exception {
        Booking booking = new Booking();

        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()));

        verify(bookingService, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    void getAllUsersBookings_ShouldReturnListOfBookings() throws Exception {
        List<Booking> bookings = List.of(new Booking());

        when(bookingService.getUsersBookings(any(StateRequest.class), anyLong())).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingService, times(1)).getUsersBookings(any(StateRequest.class), anyLong());
    }

    @Test
    void getAllUsersItemsBookings_ShouldReturnListOfBookings() throws Exception {
        List<Booking> bookings = List.of(new Booking());

        when(bookingService.getUsersItemsBookings(any(StateRequest.class), anyLong())).thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingService, times(1)).getUsersItemsBookings(any(StateRequest.class), anyLong());
    }
}
