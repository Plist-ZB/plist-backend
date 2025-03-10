package com.zerobase.plistbackend.module.channel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(ChannelRepositoryTest.TestConfig.class)
class ChannelRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByChannelHostId() {
        // given
        User host = createHost();
        createTwoClosedChannel(host);
        entityManager.flush();

        int queryYear = 2025;
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now()
                .withYear(queryYear)
                .withMonth(1));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().withYear(queryYear + 1).withMonth(1));

        // when
        List<Channel> closedChannelList = channelRepository.findByChannelHostId(host.getUserId(), startDate, endDate, ChannelStatus.CHANNEL_STATUS_CLOSED);
        // then
        assertThat(closedChannelList).hasSize(2);
        for (Channel channel : closedChannelList) {
            assertThat(channel.getChannelCreatedAt()).isAfterOrEqualTo(startDate);
            assertThat(channel.getChannelCreatedAt()).isBefore(endDate);
            assertThat(channel.getChannelHost().getUserId()).isEqualTo(host.getUserId());
            assertThat(channel.getChannelStatus()).isEqualTo(ChannelStatus.CHANNEL_STATUS_CLOSED);
        }
    }

    private void createTwoClosedChannel(User host) {
        Channel closedChannel1 = Channel.builder()
                .channelName("Closed Channel1")
                .channelStatus(ChannelStatus.CHANNEL_STATUS_CLOSED)
                .channelCreatedAt(Timestamp.valueOf(LocalDateTime.now()
                        .withYear(2025)
                        .withHour(15)
                        .withMinute(30)))
                .channelFinishedAt(Timestamp.valueOf(LocalDateTime.now()
                        .withYear(2025)
                        .withHour(20)
                        .withMinute(55)))
                .channelHost(host)
                .build();


        Channel closedChannel2 = Channel.builder()
                .channelName("Closed Channel2")
                .channelStatus(ChannelStatus.CHANNEL_STATUS_CLOSED)
                .channelCreatedAt(Timestamp.valueOf(LocalDateTime.now()
                        .withYear(2025)
                        .withHour(20)
                        .withMinute(55)))
                .channelFinishedAt(Timestamp.valueOf(LocalDateTime.now()
                        .withYear(2025)
                        .withHour(23)
                        .withMinute(55)))
                .channelHost(host)
                .build();
        entityManager.persist(closedChannel1);
        entityManager.persist(closedChannel2);
    }

    private User createHost() {
        User host = User.builder()
                .userEmail("test@plist.com")
                .userName("test")
                .build();
        entityManager.persist(host);
        return host;
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
