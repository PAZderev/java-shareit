import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {BookingDto.class})
public class BookingDtoJsonTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        String json = objectMapper.writeValueAsString(bookingDto);
        assertThat(json).contains("\"itemId\":1");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2024-10-10T10:00:00\",\"end\":\"2024-10-11T10:00:00\"}";
        BookingDto bookingDto = objectMapper.readValue(json, BookingDto.class);
        assertThat(bookingDto.getItemId()).isEqualTo(1L);
    }

    @Test
    void testValidation_Valid() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidation_InvalidDates() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2));
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
    }
}
