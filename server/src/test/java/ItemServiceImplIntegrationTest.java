import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private Long userId;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        userId = userService.addUser(user).getId();

        // Создаем тестовые предметы для пользователя
        ItemDto item1 = new ItemDto();
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);

        ItemDto item2 = new ItemDto();
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setAvailable(true);

        itemService.addItem(item1, userId);
        itemService.addItem(item2, userId);
    }

    @Test
    @Rollback
    void getUserItems_ShouldReturnItems() {
        // Тестируем метод получения предметов пользователя
        List<ItemDtoBookings> items = itemService.getItems(userId);

        assertThat(items).hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("Item 1");
        assertThat(items.get(1).getName()).isEqualTo("Item 2");
    }

    @Test
    @Rollback
    void getItemById_ShouldReturnCorrectItem() {
        // Получаем все предметы и проверяем один по ID
        List<ItemDtoBookings> items = itemService.getItems(userId);
        ItemDtoBookings item = itemService.getItem(items.get(0).getId());

        assertThat(item.getName()).isEqualTo("Item 1");
        assertThat(item.getDescription()).isEqualTo("Description 1");
    }
}
