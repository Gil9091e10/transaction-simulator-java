package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.MessageFieldsConfig;
import com.transaction.project.simulator.app.repository.MessageFieldsConfigRepository;
import com.transaction.project.simulator.app.service.MessageFieldsConfigService;
import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldsConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.MessageFieldsConfig}.
 */
@Service
@Transactional
public class MessageFieldsConfigServiceImpl implements MessageFieldsConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldsConfigServiceImpl.class);

    private final MessageFieldsConfigRepository messageFieldsConfigRepository;

    private final MessageFieldsConfigMapper messageFieldsConfigMapper;

    public MessageFieldsConfigServiceImpl(
        MessageFieldsConfigRepository messageFieldsConfigRepository,
        MessageFieldsConfigMapper messageFieldsConfigMapper
    ) {
        this.messageFieldsConfigRepository = messageFieldsConfigRepository;
        this.messageFieldsConfigMapper = messageFieldsConfigMapper;
    }

    @Override
    public MessageFieldsConfigDto save(MessageFieldsConfigDto messageFieldsConfigDto) {
        LOG.debug("Request to save MessageFieldsConfig : {}", messageFieldsConfigDto);
        MessageFieldsConfig messageFieldsConfig = messageFieldsConfigMapper.toEntity(messageFieldsConfigDto);
        messageFieldsConfig = messageFieldsConfigRepository.save(messageFieldsConfig);
        return messageFieldsConfigMapper.toDto(messageFieldsConfig);
    }

    @Override
    public MessageFieldsConfigDto update(MessageFieldsConfigDto messageFieldsConfigDto) {
        LOG.debug("Request to update MessageFieldsConfig : {}", messageFieldsConfigDto);
        MessageFieldsConfig messageFieldsConfig = messageFieldsConfigMapper.toEntity(messageFieldsConfigDto);
        messageFieldsConfig = messageFieldsConfigRepository.save(messageFieldsConfig);
        return messageFieldsConfigMapper.toDto(messageFieldsConfig);
    }

    @Override
    public Optional<MessageFieldsConfigDto> partialUpdate(MessageFieldsConfigDto messageFieldsConfigDto) {
        LOG.debug("Request to partially update MessageFieldsConfig : {}", messageFieldsConfigDto);

        return messageFieldsConfigRepository
            .findById(messageFieldsConfigDto.getId())
            .map(existingMessageFieldsConfig -> {
                messageFieldsConfigMapper.partialUpdate(existingMessageFieldsConfig, messageFieldsConfigDto);

                return existingMessageFieldsConfig;
            })
            .map(messageFieldsConfigRepository::save)
            .map(messageFieldsConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageFieldsConfigDto> findOne(Long id) {
        LOG.debug("Request to get MessageFieldsConfig : {}", id);
        return messageFieldsConfigRepository.findById(id).map(messageFieldsConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MessageFieldsConfig : {}", id);
        messageFieldsConfigRepository.deleteById(id);
    }
}
