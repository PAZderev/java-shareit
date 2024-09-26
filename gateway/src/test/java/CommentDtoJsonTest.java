import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItGateway.class)
public class CommentDtoJsonTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "Test comment", "John Doe", null);
        String json = objectMapper.writeValueAsString(commentDto);
        assertThat(json).contains("\"text\":\"Test comment\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"text\":\"Test comment\",\"authorName\":\"John Doe\"}";
        CommentDto commentDto = objectMapper.readValue(json, CommentDto.class);
        assertThat(commentDto.getText()).isEqualTo("Test comment");
    }

    @Test
    void testValidation_Valid() {
        CommentDto commentDto = new CommentDto(1L, "This is a valid comment", "John Doe", null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidation_BlankText() {
        CommentDto commentDto = new CommentDto(1L, "", "John Doe", null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void testValidation_TextTooLong() {
        String longText = "a".repeat(1001);
        CommentDto commentDto = new CommentDto(1L, longText, "John Doe", null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        assertThat(violations).isNotEmpty();
    }
}
