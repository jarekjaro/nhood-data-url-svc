package com.h8.nh.nhoodlocationsvc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LocationServiceApplicationTest {

	@Autowired
	private ApplicationContext context;

	@ParameterizedTest
	@MethodSource("listComponents")
	void withMethodSource(String component) {
		assertThat(context.getBean(component)).isNotNull();
	}

	private static Stream<Arguments> listComponents() {
		return Stream.of(
				Arguments.of("locationEntryRepository"),
				Arguments.of("locationEntryService"),
				Arguments.of("locationEntryController"));
	}
}
