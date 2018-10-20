package com.h8.nh.nhoodlocationsvc.e2e;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import com.h8.nh.nhoodlocationsvc.dto.request.LocationEntryRequestDTO;
import com.h8.nh.nhoodlocationsvc.dto.response.LocationEntryResponseDTO;
import com.h8.nh.nhoodlocationsvc.repositories.LocationEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class LocationServiceApplicationTest {

    @Autowired
    private LocationEntryRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private ModelMapper mapper = new ModelMapper();

    private LocationEntry[] testData;
    private LocationEntryResponseDTO[] testDataDto;

    @BeforeEach
    void setUp() {
        var data = initializeData();
        testData = StreamSupport.stream(data.spliterator(), false)
                .toArray(LocationEntry[]::new);
        testDataDto = StreamSupport.stream(data.spliterator(), false)
                .map(l -> mapper.map(l, LocationEntryResponseDTO.class))
                .toArray(LocationEntryResponseDTO[]::new);
    }

    @Test
    void shouldListAllLocationsWhenGetIsCalled() {
        // when
        var response = restTemplate.getForEntity(
                "/locations", LocationEntryResponseDTO[].class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .containsExactlyInAnyOrder(testDataDto[0], testDataDto[1]);
    }

    @Test
    void shouldReturnSingleLocationWhenGetIsCalledWithIdParameter() {
        // given
        var id = testData[0].getId();

        // when
        var response = restTemplate.getForEntity(
                "/locations/" + id, LocationEntryResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(testDataDto[0]);
    }

    @Test
    void shouldReturnNotFoundWhenGetIsCalledWithNonExistingIdParameter() {
        // given
        var id = Long.MAX_VALUE;

        // when
        var response = restTemplate.getForEntity(
                "/locations/" + id, LocationEntryResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldCreateObjectWhenPostIsCalled() {
        // given
        var entry = initializeLocationEntry("MESSAGE3", 10.0, 100.0);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/locations", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNull();

        // when
        var location = created.getHeaders().get("Location").get(0);
        var response = restTemplate.getForEntity(
                location, LocationEntryResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage())
                .isEqualTo(entry.getMessage());
        assertThat(response.getBody().getLatitude())
                .isEqualTo(entry.getLatitude());
        assertThat(response.getBody().getLongitude())
                .isEqualTo(entry.getLongitude());
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithMissingMessage() {
        // given
        var entry = initializeLocationEntry(null, 10.0, 100.0);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/locations", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithMissingLatitude() {
        // given
        var entry = initializeLocationEntry("MESSAGE3", null, 100.0);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/locations", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithMissingLongitude() {
        // given
        var entry = initializeLocationEntry("MESSAGE3", 10.0, null);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/locations", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldModifyObjectWhenPutIsCalled() {
        // given
        var id = testData[0].getId();

        var entry = initializeLocationEntry("MESSAGE3", 10.0, 100.0);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var req = new HttpEntity<>(dto, new HttpHeaders());
        var modified = restTemplate.exchange(
                "/locations/" + id, HttpMethod.PUT, req, Void.class);

        // then
        assertThat(modified.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(modified.getBody()).isNull();

        // when
        var response = restTemplate.getForEntity(
                "/locations/" + id, LocationEntryResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage())
                .isEqualTo(entry.getMessage());
        assertThat(response.getBody().getLatitude())
                .isEqualTo(entry.getLatitude());
        assertThat(response.getBody().getLongitude())
                .isEqualTo(entry.getLongitude());
    }

    @Test
    void shouldReturnNotFoundWhenPutIsCalledWithNonExistingIdParameter() {
        // given
        var id = Long.MAX_VALUE;

        var entry = initializeLocationEntry("MESSAGE3", 10.0, 100.0);
        var dto = mapper.map(entry, LocationEntryRequestDTO.class);

        // when
        var req = new HttpEntity<>(dto, new HttpHeaders());
        var modified = restTemplate.exchange(
                "/locations/" + id, HttpMethod.PUT, req, Void.class);

        // then
        assertThat(modified.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(modified.getBody()).isNull();
    }

    @Test
    void shouldDeleteObjectWhenDeleteIsCalled() {
        // given
        var id = testData[0].getId();

        // when
        var req = new HttpEntity<>(new HttpHeaders());
        var deleted = restTemplate.exchange(
                "/locations/" + id, HttpMethod.DELETE, req, Void.class);

        // then
        assertThat(deleted.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(deleted.getBody()).isNull();

        // when
        var response = restTemplate.getForEntity(
                "/locations/" + id, LocationEntryResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnNotFoundWhenDeleteIsCalledWithNonExistingIdParameter() {
        // given
        var id = Long.MAX_VALUE;

        // when
        var req = new HttpEntity<>(new HttpHeaders());
        var deleted = restTemplate.exchange(
                "/locations/" + id, HttpMethod.DELETE, req, Void.class);

        // then
        assertThat(deleted.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(deleted.getBody()).isNull();
    }

    private Iterable<LocationEntry> initializeData() {
        repository.deleteAll();
        return repository.saveAll(
                Arrays.asList(
                        initializeLocationEntry("MESSAGE1", 1.0, 10.0),
                        initializeLocationEntry("MESSAGE2", 2.0, 20.0)));
    }

    private LocationEntry initializeLocationEntry(String m, Double lt, Double ln) {
        return LocationEntry.builder()
                .message(m)
                .latitude(lt)
                .longitude(ln)
                .build();
    }
}
