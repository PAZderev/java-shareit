import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItGateway.class)
public class ItemDtoJsonTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Item Name", "Item Description", true, null, 1L);
        String json = objectMapper.writeValueAsString(itemDto);
        assertThat(json).contains("\"name\":\"Item Name\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"name\":\"Item Name\",\"description\":\"Item Description\",\"available\":true,\"requestId\":1}";
        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);
        assertThat(itemDto.getName()).isEqualTo("Item Name");
    }

    @Test
    void testValidation_Valid() {
        ItemDto itemDto = new ItemDto(1L, "Valid Name", "Valid Description", true, null, 1L);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidation_BlankName() {
        ItemDto itemDto = new ItemDto(1L, "", "Valid Description", true, null, 1L);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void testValidation_NullDescription() {
        ItemDto itemDto = new ItemDto(1L, "Valid Name", null, true, null, 1L);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        assertThat(violations).isNotEmpty();
    }
}
