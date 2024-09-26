import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItApp.class) // Указываем основной класс приложения
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void addRequest_ShouldReturnItemRequest() throws Exception {
        // Создаем DTO с заполненным полем
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Test request");

        // Мокаем сервис, чтобы он возвращал этот DTO
        when(itemRequestService.addItemRequest(any(ItemRequestDto.class), anyLong())).thenReturn(requestDto);

        // Отправляем POST-запрос с корректным JSON
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"description\": \"Test request\"}")) // замените на правильный JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test request")); // проверяем поле description

        // Проверяем, что метод сервиса был вызван
        verify(itemRequestService, times(1)).addItemRequest(any(ItemRequestDto.class), anyLong());
    }

    @Test
    void getRequests_ShouldReturnListOfRequests() throws Exception {
        List<ItemRequestResponseDto> requests = List.of(new ItemRequestResponseDto()); // создайте список

        when(itemRequestService.getUsersItemRequests(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemRequestService, times(1)).getUsersItemRequests(anyLong());
    }

    @Test
    void getAllOthersRequests_ShouldReturnListOfRequests() throws Exception {
        List<ItemRequest> requests = List.of(new ItemRequest()); // создайте список

        when(itemRequestService.getAllOthersRequests(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemRequestService, times(1)).getAllOthersRequests(anyLong());
    }

    @Test
    void getRequest_ShouldReturnRequest() throws Exception {
        ItemRequestResponseDto request = new ItemRequestResponseDto(); // создайте DTO

        when(itemRequestService.getItemRequest(anyLong())).thenReturn(request);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()));

        verify(itemRequestService, times(1)).getItemRequest(anyLong());
    }
}
