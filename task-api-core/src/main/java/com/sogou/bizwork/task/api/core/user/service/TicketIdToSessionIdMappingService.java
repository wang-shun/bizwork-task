package com.sogou.bizwork.task.api.core.user.service;

import com.sogou.bizdev.polestar.cache.annotation.CacheEvict;
import com.sogou.bizdev.polestar.cache.annotation.CacheFlush;
import com.sogou.bizdev.polestar.cache.annotation.CacheKey;
import com.sogou.bizdev.polestar.cache.annotation.CacheValue;
import com.sogou.bizdev.polestar.cache.annotation.Cacheable;
import com.sogou.bizwork.task.api.constant.cache.CacheKeyspaceConstants;

public interface TicketIdToSessionIdMappingService {

    @CacheFlush(keyspaces = { CacheKeyspaceConstants.TICKET_TO_SESSION_ID_MAPPING }, livetime = 3600 * 24 * 7)
    public void recordSessionIdByTicketId(@CacheKey String ticketId, @CacheValue String sessionId);

    @Cacheable(keyspace = CacheKeyspaceConstants.TICKET_TO_SESSION_ID_MAPPING, livetime = 3600 * 24)
    public String retriveSessionIdByTicketId(@CacheKey String ticketId);

    @CacheEvict(keyspaces = { CacheKeyspaceConstants.TICKET_TO_SESSION_ID_MAPPING })
    public void destroySessionIdByTicketId(String ticketId);

}
