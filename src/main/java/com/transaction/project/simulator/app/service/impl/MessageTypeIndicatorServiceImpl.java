package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.repository.MessageTypeIndicatorRepository;
import com.transaction.project.simulator.app.service.MessageTypeIndicatorService;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import com.transaction.project.simulator.app.service.mapper.MessageTypeIndicatorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.MessageTypeIndicator}.
 */
@Service
@Transactional
public class MessageTypeIndicatorServiceImpl implements MessageTypeIndicatorService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageTypeIndicatorServiceImpl.class);

    private final MessageTypeIndicatorRepository messageTypeIndicatorRepository;

    private final MessageTypeIndicatorMapper messageTypeIndicatorMapper;

    public MessageTypeIndicatorServiceImpl(
        MessageTypeIndicatorRepository messageTypeIndicatorRepository,
        MessageTypeIndicatorMapper messageTypeIndicatorMapper
    ) {
        this.messageTypeIndicatorRepository = messageTypeIndicatorRepository;
        this.messageTypeIndicatorMapper = messageTypeIndicatorMapper;
    }

    @Override
    public MessageTypeIndicatorDto save(MessageTypeIndicatorDto messageTypeIndicatorDto) {
        LOG.debug("Request to save MessageTypeIndicator : {}", messageTypeIndicatorDto);
        MessageTypeIndicator messageTypeIndicator = messageTypeIndicatorMapper.toEntity(messageTypeIndicatorDto);
        messageTypeIndicator = messageTypeIndicatorRepository.save(messageTypeIndicator);
        return messageTypeIndicatorMapper.toDto(messageTypeIndicator);
    }

    @Override
    public MessageTypeIndicatorDto update(MessageTypeIndicatorDto messageTypeIndicatorDto) {
        LOG.debug("Request to update MessageTypeIndicator : {}", messageTypeIndicatorDto);
        MessageTypeIndicator messageTypeIndicator = messageTypeIndicatorMapper.toEntity(messageTypeIndicatorDto);
        messageTypeIndicator = messageTypeIndicatorRepository.save(messageTypeIndicator);
        return messageTypeIndicatorMapper.toDto(messageTypeIndicator);
    }

    @Override
    public Optional<MessageTypeIndicatorDto> partialUpdate(MessageTypeIndicatorDto messageTypeIndicatorDto) {
        LOG.debug("Request to partially update MessageTypeIndicator : {}", messageTypeIndicatorDto);

        return messageTypeIndicatorRepository
            .findById(messageTypeIndicatorDto.getId())
            .map(existingMessageTypeIndicator -> {
                messageTypeIndicatorMapper.partialUpdate(existingMessageTypeIndicator, messageTypeIndicatorDto);

                return existingMessageTypeIndicator;
            })
            .map(messageTypeIndicatorRepository::save)
            .map(messageTypeIndicatorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageTypeIndicatorDto> findOne(Long id) {
        LOG.debug("Request to get MessageTypeIndicator : {}", id);
        return messageTypeIndicatorRepository.findById(id).map(messageTypeIndicatorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MessageTypeIndicator : {}", id);
        messageTypeIndicatorRepository.deleteById(id);
    }
}
