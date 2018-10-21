package com.h8.nh.nhooddataurlsvc.e2e;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import com.h8.nh.nhooddataurlsvc.dto.request.DataUrlRequestDTO;
import com.h8.nh.nhooddataurlsvc.dto.response.DataUrlResponseDTO;
import com.h8.nh.nhooddataurlsvc.repositories.DataUrlRepository;
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
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DataUrlServiceApplicationTest {

    @Autowired
    private DataUrlRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private ModelMapper mapper = new ModelMapper();

    private DataUrl[] testData;
    private DataUrlResponseDTO[] testDataDto;

    @BeforeEach
    void setUp() {
        var data = initializeData();
        testData = StreamSupport.stream(data.spliterator(), false)
                .toArray(DataUrl[]::new);
        testDataDto = StreamSupport.stream(data.spliterator(), false)
                .map(l -> mapper.map(l, DataUrlResponseDTO.class))
                .toArray(DataUrlResponseDTO[]::new);
    }

    @Test
    void shouldListAllDataUrlsWhenGetIsCalled() {
        // when
        var response = restTemplate.getForEntity(
                "/urls", DataUrlResponseDTO[].class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .containsExactlyInAnyOrder(testDataDto[0], testDataDto[1]);
    }

    @Test
    void shouldReturnSingleDataUrlWhenGetIsCalledWithIdParameter() {
        // given
        var id = testData[0].getId();

        // when
        var response = restTemplate.getForEntity(
                "/urls/" + id, DataUrlResponseDTO.class);

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
                "/urls/" + id, DataUrlResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldCreateDataUrlWhenPostIsCalled() {
        // given
        var entry = initializeDataUrl("URL3", "KEY3.1", "KEY3.2");
        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/urls", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNull();

        // when
        var location = created.getHeaders().get("Location").get(0);
        var response = restTemplate.getForEntity(
                location, DataUrlResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getKey())
                .isEqualTo(entry.getKey());
        assertThat(response.getBody().getUrl())
                .isEqualTo(entry.getUrl());
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithMissingUrl() {
        // given
        var entry = initializeDataUrl(null, "KEY3.1", "KEY3.2");
        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/urls", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithMissingKey() {
        // given
        var entry = initializeDataUrl("URL3", "KEY3.1", "KEY3.2");
        entry.setKey(null);

        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/urls", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldReturnBadRequestWhenPostIsCalledWithEmptyKey() {
        // given
        var entry = initializeDataUrl("URL3");
        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var created = restTemplate.postForEntity(
                "/urls", dto, Void.class);

        // then
        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(created.getBody()).isNull();
    }

    @Test
    void shouldModifyObjectWhenPutIsCalled() {
        // given
        var id = testData[0].getId();

        var entry = initializeDataUrl("URL3", "KEY3.1", "KEY3.2");
        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var req = new HttpEntity<>(dto, new HttpHeaders());
        var modified = restTemplate.exchange(
                "/urls/" + id, HttpMethod.PUT, req, Void.class);

        // then
        assertThat(modified.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(modified.getBody()).isNull();

        // when
        var response = restTemplate.getForEntity(
                "/urls/" + id, DataUrlResponseDTO.class);

        // then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getKey())
                .isEqualTo(entry.getKey());
        assertThat(response.getBody().getUrl())
                .isEqualTo(entry.getUrl());
    }

    @Test
    void shouldReturnNotFoundWhenPutIsCalledWithNonExistingIdParameter() {
        // given
        var id = Long.MAX_VALUE;

        var entry = initializeDataUrl("URL3", "KEY3.1", "KEY3.2");
        var dto = mapper.map(entry, DataUrlRequestDTO.class);

        // when
        var req = new HttpEntity<>(dto, new HttpHeaders());
        var modified = restTemplate.exchange(
                "/urls/" + id, HttpMethod.PUT, req, Void.class);

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
                "/urls/" + id, HttpMethod.DELETE, req, Void.class);

        // then
        assertThat(deleted.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(deleted.getBody()).isNull();

        // when
        var response = restTemplate.getForEntity(
                "/urls/" + id, DataUrlResponseDTO.class);

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
                "/urls/" + id, HttpMethod.DELETE, req, Void.class);

        // then
        assertThat(deleted.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(deleted.getBody()).isNull();
    }

    private Iterable<DataUrl> initializeData() {
        repository.deleteAll();
        return repository.saveAll(
                Arrays.asList(
                        initializeDataUrl("URL1", "KEY1.1", "KEY1.2"),
                        initializeDataUrl("URL2", "KEY2.1", "KEY2.2")));
    }

    private DataUrl initializeDataUrl(String url, String... keys) {
        return DataUrl.builder()
                .key(List.of(keys))
                .url(url)
                .build();
    }
}
