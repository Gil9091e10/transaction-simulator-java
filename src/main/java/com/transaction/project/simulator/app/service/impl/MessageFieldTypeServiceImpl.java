package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.repository.MessageFieldTypeRepository;
import com.transaction.project.simulator.app.service.MessageFieldTypeService;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.MessageFieldType}.
 */
@Service
@Transactional
public class MessageFieldTypeServiceImpl implements MessageFieldTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldTypeServiceImpl.class);

    private final MessageFieldTypeRepository messageFieldTypeRepository;

    private final MessageFieldTypeMapper messageFieldTypeMapper;

    public MessageFieldTypeServiceImpl(
        MessageFieldTypeRepository messageFieldTypeRepository,
        MessageFieldTypeMapper messageFieldTypeMapper
    ) {
        this.messageFieldTypeRepository = messageFieldTypeRepository;
        this.messageFieldTypeMapper = messageFieldTypeMapper;
    }

    @Override
    public MessageFieldTypeDto save(MessageFieldTypeDto messageFieldTypeDto) {
        LOG.debug("Request to save MessageFieldType : {}", messageFieldTypeDto);
        MessageFieldType messageFieldType = messageFieldTypeMapper.toEntity(messageFieldTypeDto);
        messageFieldType = messageFieldTypeRepository.save(messageFieldType);
        return messageFieldTypeMapper.toDto(messageFieldType);
    }

    @Override
    public MessageFieldTypeDto update(MessageFieldTypeDto messageFieldTypeDto) {
        LOG.debug("Request to update MessageFieldType : {}", messageFieldTypeDto);
        MessageFieldType messageFieldType = messageFieldTypeMapper.toEntity(messageFieldTypeDto);
        messageFieldType = messageFieldTypeRepository.save(messageFieldType);
        return messageFieldTypeMapper.toDto(messageFieldType);
    }

    @Override
    public Optional<MessageFieldTypeDto> partialUpdate(MessageFieldTypeDto messageFieldTypeDto) {
        LOG.debug("Request to partially update MessageFieldType : {}", messageFieldTypeDto);

        return messageFieldTypeRepository
            .findById(messageFieldTypeDto.getId())
            .map(existingMessageFieldType -> {
                messageFieldTypeMapper.partialUpdate(existingMessageFieldType, messageFieldTypeDto);

                return existingMessageFieldType;
            })
            .map(messageFieldTypeRepository::save)
            .map(messageFieldTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageFieldTypeDto> findOne(Long id) {
        LOG.debug("Request to get MessageFieldType : {}", id);
        return messageFieldTypeRepository.findById(id).map(messageFieldTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MessageFieldType : {}", id);
        messageFieldTypeRepository.deleteById(id);
    }
}
