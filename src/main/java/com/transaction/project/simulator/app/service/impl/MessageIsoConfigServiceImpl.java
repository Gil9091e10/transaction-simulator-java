package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.repository.MessageIsoConfigRepository;
import com.transaction.project.simulator.app.service.MessageIsoConfigService;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageIsoConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.MessageIsoConfig}.
 */
@Service
@Transactional
public class MessageIsoConfigServiceImpl implements MessageIsoConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageIsoConfigServiceImpl.class);

    private final MessageIsoConfigRepository messageIsoConfigRepository;

    private final MessageIsoConfigMapper messageIsoConfigMapper;

    public MessageIsoConfigServiceImpl(
        MessageIsoConfigRepository messageIsoConfigRepository,
        MessageIsoConfigMapper messageIsoConfigMapper
    ) {
        this.messageIsoConfigRepository = messageIsoConfigRepository;
        this.messageIsoConfigMapper = messageIsoConfigMapper;
    }

    @Override
    public MessageIsoConfigDto save(MessageIsoConfigDto messageIsoConfigDto) {
        LOG.debug("Request to save MessageIsoConfig : {}", messageIsoConfigDto);
        MessageIsoConfig messageIsoConfig = messageIsoConfigMapper.toEntity(messageIsoConfigDto);
        messageIsoConfig = messageIsoConfigRepository.save(messageIsoConfig);
        return messageIsoConfigMapper.toDto(messageIsoConfig);
    }

    @Override
    public MessageIsoConfigDto update(MessageIsoConfigDto messageIsoConfigDto) {
        LOG.debug("Request to update MessageIsoConfig : {}", messageIsoConfigDto);
        MessageIsoConfig messageIsoConfig = messageIsoConfigMapper.toEntity(messageIsoConfigDto);
        messageIsoConfig = messageIsoConfigRepository.save(messageIsoConfig);
        return messageIsoConfigMapper.toDto(messageIsoConfig);
    }

    @Override
    public Optional<MessageIsoConfigDto> partialUpdate(MessageIsoConfigDto messageIsoConfigDto) {
        LOG.debug("Request to partially update MessageIsoConfig : {}", messageIsoConfigDto);

        return messageIsoConfigRepository
            .findById(messageIsoConfigDto.getId())
            .map(existingMessageIsoConfig -> {
                messageIsoConfigMapper.partialUpdate(existingMessageIsoConfig, messageIsoConfigDto);

                return existingMessageIsoConfig;
            })
            .map(messageIsoConfigRepository::save)
            .map(messageIsoConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageIsoConfigDto> findOne(Long id) {
        LOG.debug("Request to get MessageIsoConfig : {}", id);
        return messageIsoConfigRepository.findById(id).map(messageIsoConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MessageIsoConfig : {}", id);
        messageIsoConfigRepository.deleteById(id);
    }
}
