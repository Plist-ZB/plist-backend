package com.zerobase.plistbackend.module.participant.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("year로 년도를 검색하는 쿼리를 발생시킨다")
    void findByUserIdAndDate() {
        //given
        long userId = 1L;
        LocalDateTime startDate = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2024 + 1, 1, 1).atStartOfDay();
        //when
        List<Participant> byUserIdAndDate = participantRepository.findByUserIdAndDate(
                userId,
                startDate,
                endDate);
        //then
        assertThat(byUserIdAndDate).isEmpty();
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}