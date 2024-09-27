import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User testUser;
    private ItemRequest testRequest;
    private Item testItem;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        userRepository.save(testUser);

        // Создаем тестовый запрос
        testRequest = new ItemRequest();
        testRequest.setRequestor(testUser);
        testRequest.setDescription("Test Item Request");
        testRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(testRequest);

        // Создаем тестовый предмет, который относится к запросу
        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setDescription("Test Item Description");
        testItem.setAvailable(true);
        testItem.setOwner(testUser);
        testItem.setRequest(testRequest);
        itemRepository.save(testItem);
    }

    @Test
    void getUsersItemRequests_ShouldReturnRequestsForUser() {
        // Получаем запросы для пользователя
        Collection<ItemRequestResponseDto> requests = itemRequestService.getUsersItemRequests(testUser.getId());

        // Проверяем, что запросы не пустые
        assertThat(requests).isNotNull();
        assertThat(requests.size()).isGreaterThanOrEqualTo(1);

        // Проверяем, что запрос корректный
        ItemRequestResponseDto requestDto = requests.iterator().next();
        assertThat(requestDto.getDescription()).isEqualTo(testRequest.getDescription());

        // Проверяем наличие предмета в ответе
        assertThat(requestDto.getItems()).isNotNull();
        assertThat(requestDto.getItems().size()).isGreaterThanOrEqualTo(1);

        // Проверяем, что данные предмета корректны
        assertThat(requestDto.getItems().get(0).getName()).isEqualTo(testItem.getName());
    }
}
