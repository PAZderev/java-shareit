import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;
import ru.practicum.shareit.utils.StateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        userRepository.save(testUser);
        userId = testUser.getId();

        // Создаем тестовый предмет
        Item testItem = new Item();
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setOwner(testUser);
        testItem.setAvailable(true);
        itemRepository.save(testItem);

        // Создаем тестовое бронирование
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(testItem);
        booking.setBooker(testUser);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void getBookings_ShouldReturnBookingsForUser() {
        // Получаем бронирования для пользователя
        List<Booking> bookings = bookingService.getUsersBookings(StateRequest.ALL, userId);

        // Проверяем, что бронирования не пустые
        assertThat(bookings).isNotNull();
        assertThat(bookings.size()).isGreaterThanOrEqualTo(1);

        // Дополнительные проверки данных бронирования
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(userId);
    }

}
