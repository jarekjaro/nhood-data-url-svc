package com.h8.nh.nhoodlocationsvc.services;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import com.h8.nh.nhoodlocationsvc.repositories.LocationEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationEntryServiceTest {

    private LocationEntryRepository repository;

    private LocationEntryService service;

    @BeforeEach
    void setUp() {
        repository = mock(LocationEntryRepository.class);
        service = new LocationEntryService(repository);
    }

    @Test
    void shouldReturnLocationsReturnedFromRepositoryOnFindAll() {
        // given
        List<LocationEntry> all = List.of();
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
    void shouldReturnOptionalOfLocationReturnedFromRepositoryOnFindById() {
        // given
        var id = new Random().nextLong();

        Optional<LocationEntry> one = Optional.empty();
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
    void shouldReturnSavedNewLocationOnCreate() {
        // given
        var entry = mock(LocationEntry.class);
        var created = mock(LocationEntry.class);

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
        var entry = mock(LocationEntry.class);

        ArgumentCaptor<LocationEntry> captor = ArgumentCaptor.forClass(LocationEntry.class);

        // when
        service.create(entry);

        // then
        verify(repository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(entry);
    }

    @Test
    void shouldReturnEmptyLocationIfIdDoesNotExistInRepositoryOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(LocationEntry.class);

        Optional<LocationEntry> one = Optional.empty();
        when(repository.findById(any()))
                .thenReturn(one);

        // when
        var result = service.modify(id, entry);

        // then
        assertThat(result).isEqualTo(one);
    }

    @Test
    void shouldNotCallRepositorySaveWhenThereIsNoLocationToModifyOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(LocationEntry.class);

        when(repository.findById(any()))
                .thenReturn(Optional.empty());

        // when
        service.modify(id, entry);

        // then
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnedSavedLocationFromRepositoryOnModify() {
        // given
        var id = new Random().nextLong();

        var entry = mock(LocationEntry.class);
        var modified = mock(LocationEntry.class);

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
    void shouldCallRepositorySaveWithModifiedPreviousLocationOnModify() {
        // given
        var id = new Random().nextLong();
        var entry = LocationEntry.builder()
                .message("MESSAGE")
                .latitude(10.0)
                .longitude(100.0)
                .build();
        var previous = LocationEntry.builder()
                .id(id)
                .message("")
                .latitude(0.0)
                .longitude(0.0)
                .build();

        ArgumentCaptor<LocationEntry> captor = ArgumentCaptor.forClass(LocationEntry.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(previous));

        // when
        service.modify(id, entry);

        // then
        verify(repository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId())
                .isEqualTo(previous.getId());
        assertThat(captor.getValue().getMessage())
                .isEqualTo(entry.getMessage());
        assertThat(captor.getValue().getLatitude())
                .isEqualTo(entry.getLatitude());
        assertThat(captor.getValue().getLongitude())
                .isEqualTo(entry.getLongitude());
    }

    @Test
    void shouldReturnEmptyLocationIfIdDoesNotExistInRepositoryOnDelete() {
        // given
        var id = new Random().nextLong();

        Optional<LocationEntry> one = Optional.empty();
        when(repository.findById(any()))
                .thenReturn(one);

        // when
        var result = service.delete(id);

        // then
        assertThat(result).isEqualTo(one);
    }

    @Test
    void shouldNotCallRepositoryDeleteWhenThereIsNoLocationToDeleteOnDelete() {
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
    void shouldReturnedDeleterLocationFromRepositoryOnDelete() {
        // given
        var id = new Random().nextLong();

        var deleted = mock(LocationEntry.class);

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
    void shouldCallRepositoryDeleteWithFoundLocationOnDelete() {
        // given
        var id = new Random().nextLong();

        var found = mock(LocationEntry.class);

        ArgumentCaptor<LocationEntry> captor = ArgumentCaptor.forClass(LocationEntry.class);

        when(repository.findById(any()))
                .thenReturn(Optional.of(found));

        // when
        service.delete(id);

        // then
        verify(repository, times(1)).delete(captor.capture());
        assertThat(captor.getValue()).isEqualTo(found);
    }
}
