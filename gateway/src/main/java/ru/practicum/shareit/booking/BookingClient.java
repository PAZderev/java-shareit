package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.utils.StateRequest;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBooking(long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> createBooking(BookingDto requestDto, Long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateBooking(Long bookingId, Boolean approved, Long userId) {
        String path = "/" + bookingId + "?approved=" + approved;
        return patch(path, userId, null);
    }


    public ResponseEntity<Object> getUsersBookings(StateRequest state, Long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getUsersItemsBookings(StateRequest state, Long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner", userId, parameters);
    }

}
