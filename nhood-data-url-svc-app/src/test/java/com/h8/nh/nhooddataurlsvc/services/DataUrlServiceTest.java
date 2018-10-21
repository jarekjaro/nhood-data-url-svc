package com.h8.nh.nhooddataurlsvc.services;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import com.h8.nh.nhooddataurlsvc.repositories.DataUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataUrlServiceTest {

    private DataUrlRepository repository;

    private DataUrlService service;

    @BeforeEach
    void setUp() {
        repository = mock(DataUrlRepository.class);
        service = new DataUrlService(repository);
    }

    @Test
    void shouldReturnDataUrlsReturnedFromRepositoryOnFindAll() {
        // given
        List<DataUrl> all = List.of();
        when(repository.findAll())
                .thenReturn(all);

        // when
        var result = service.findAll();

        // then
        assertThat(result).isEqualTo(all);
    }

    @Test
    void shouldCallRepositoryFindAllOnFindAll() {
        // when
        service.findAll();

        // then
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnOptionalOfDataUrlReturnedFromRepositoryOnFindById() {
        // given
        var id = new Random().nextLong();

        Optional<DataUrl> one = Optional.empty();
        when(repository.findById(any()))
                .thenReturn(one);

        // when
        var result = service.findById(id);

        // then
        assertThat(result).isEqualTo(one);
    }

    @Test
    void shouldCallRepositoryFindByIdOnFindById() {
        // given
        var id = new Random().nextLong();

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        // when
        service.findById(id);

        // then
        verify(repository, times(1)).findById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(id);
    }

    @Test
    void shouldReturnSavedNewDataUrlOnCreate() {
        // given
        var entry = mock(DataUrl.class);
        var created = mock(DataUrl.class);

        when(repository.save(any()))
                .thenReturn(created);

        // when
        var result = service.create(entry);

        // then
        assertThat(result).isEqualTo(created);
    }

    @Test
    void shouldCallRepositorySaveOnCreate() {
        // given
        var entry = mock(DataUrl.class);

        ArgumentCaptor<DataUrl> captor = ArgumentCaptor.forClass(DataUrl.class);

        // when
        service.create(entry);

        // then
        verify(repository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(entry);
    }

    @Test
    void shouldReturnEmptyDataUrlIfIdDoesNotExistInRepositoryOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(DataUrl.class);

        Optional<DataUrl> one = Optional.empty();
        when(repository.findById(any()))
                .thenReturn(one);

        // when
        var result = service.modify(id, entry);

        // then
        assertThat(result).isEqualTo(one);
    }

    @Test
    void shouldNotCallRepositorySaveWhenThereIsNoDataUrlToModifyOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(DataUrl.class);

        when(repository.findById(any()))
                .thenReturn(Optional.empty());

        // when
        service.modify(id, entry);

        // then
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnedSavedDataUrlFromRepositoryOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(DataUrl.class);
        var modified = mock(DataUrl.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(modified));
        when(repository.save(any()))
                .thenReturn(modified);

        // when
        var result = service.modify(id, entry);

        // then
        assertThat(result).isEqualTo(Optional.of(modified));
    }

    @Test
    void shouldCallRepositorySaveWithModifiedPreviousDataUrlOnModify() {
        // given
        var id = new Random().nextLong();
        var entry = DataUrl.builder()
                .key(List.of("KEY1"))
                .url("URL1")
                .build();
        var previous = DataUrl.builder()
                .id(id)
                .key(List.of("KEY2"))
                .url("URL2")
                .build();

        ArgumentCaptor<DataUrl> captor = ArgumentCaptor.forClass(DataUrl.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(previous));

        // when
        service.modify(id, entry);

        // then
        verify(repository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId())
                .isEqualTo(previous.getId());
        assertThat(captor.getValue().getKey())
                .isEqualTo(entry.getKey());
        assertThat(captor.getValue().getUrl())
                .isEqualTo(entry.getUrl());
    }

    @Test
    void shouldReturnEmptyDataUrlIfIdDoesNotExistInRepositoryOnDelete() {
        // given
        var id = new Random().nextLong();

        Optional<DataUrl> one = Optional.empty();
        when(repository.findById(any()))
                .thenReturn(one);

        // when
        var result = service.delete(id);

        // then
        assertThat(result).isEqualTo(one);
    }

    @Test
    void shouldNotCallRepositoryDeleteWhenThereIsNoDataUrlToDeleteOnDelete() {
        // given
        var id = new Random().nextLong();

        when(repository.findById(any()))
                .thenReturn(Optional.empty());

        // when
        service.delete(id);

        // then
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldReturnDeletedDataUrlFromRepositoryOnDelete() {
        // given
        var id = new Random().nextLong();

        var deleted = mock(DataUrl.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(deleted));
        when(repository.save(any()))
                .thenReturn(deleted);

        // when
        var result = service.delete(id);

        // then
        assertThat(result).isEqualTo(Optional.of(deleted));
    }

    @Test
    void shouldCallRepositoryDeleteWithFoundDataUrlOnDelete() {
        // given
        var id = new Random().nextLong();

        var found = mock(DataUrl.class);

        ArgumentCaptor<DataUrl> captor = ArgumentCaptor.forClass(DataUrl.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(found));

        // when
        service.delete(id);

        // then
        verify(repository, times(1)).delete(captor.capture());
        assertThat(captor.getValue()).isEqualTo(found);
    }
}
