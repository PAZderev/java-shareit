import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
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
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void createItem_ShouldReturnItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        Item item = new Item();
        when(itemService.addItem(any(ItemDto.class), anyLong())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\": \"Test item\", \"description\": \"Test description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId())); // замените на реальные проверки

        verify(itemService, times(1)).addItem(any(ItemDto.class), anyLong());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        Item item = new Item();
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\": \"Updated item\", \"description\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));

        verify(itemService, times(1)).updateItem(any(ItemDto.class), anyLong(), anyLong());
    }

    @Test
    void getItem_ShouldReturnItem() throws Exception {
        ItemDtoBookings itemDtoBookings = new ItemDtoBookings();
        when(itemService.getItem(anyLong())).thenReturn(itemDtoBookings);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoBookings.getId()));

        verify(itemService, times(1)).getItem(anyLong());
    }

    @Test
    void getAllItems_ShouldReturnListOfItems() throws Exception {
        List<ItemDtoBookings> items = List.of(new ItemDtoBookings());
        when(itemService.getItems(anyLong())).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemService, times(1)).getItems(anyLong());
    }

    @Test
    void searchItems_ShouldReturnListOfItems() throws Exception {
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.searchItems(anyString())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemService, times(1)).searchItems(anyString());
    }

    @Test
    void addComment_ShouldReturnComment() throws Exception {
        // Создаем CommentDto с текстом комментария
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment"); // Убедитесь, что текст установлен

        // Настройка мока, чтобы сервис возвращал этот объект
        when(itemService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        // Выполняем POST-запрос и проверяем ответ
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"text\": \"Test comment\"}")) // Корректный JSON с текстом
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test comment")); // Проверяем правильность текста

        // Проверяем, что метод addComment был вызван один раз
        verify(itemService, times(1)).addComment(any(CommentDto.class), anyLong(), anyLong());
    }

}
