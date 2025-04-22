package com.example.shop.infrastructure.common.dto;

import com.example.shop.infrastructure.common.contants.EventType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageMeta {
    protected String messageId;
    private String originalMessageId;
    private EventType type;
    private String serviceId;
    private long timestamp;
    private boolean autoRetry;

    MessageMeta(String messageId, String originalMessageId, EventType type, String serviceId, long timestamp, boolean autoRetry) {
        this.type = EventType.EVENT;
        this.autoRetry = false;
        this.messageId = messageId;
        this.originalMessageId = originalMessageId;
        this.type = type;
        this.serviceId = serviceId;
        this.timestamp = timestamp;
        this.autoRetry = autoRetry;
    }

    public static MessageMetaBuilder builder() {
        return new MessageMetaBuilder();
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getOriginalMessageId() {
        return this.originalMessageId;
    }

    public EventType getType() {
        return this.type;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isAutoRetry() {
        return this.autoRetry;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setOriginalMessageId(String originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAutoRetry(boolean autoRetry) {
        this.autoRetry = autoRetry;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MessageMeta)) {
            return false;
        } else {
            MessageMeta other = (MessageMeta) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getTimestamp() != other.getTimestamp()) {
                return false;
            } else if (this.isAutoRetry() != other.isAutoRetry()) {
                return false;
            } else {
                label64:
                {
                    Object this$messageId = this.getMessageId();
                    Object other$messageId = other.getMessageId();
                    if (this$messageId == null) {
                        if (other$messageId == null) {
                            break label64;
                        }
                    } else if (this$messageId.equals(other$messageId)) {
                        break label64;
                    }

                    return false;
                }

                label57:
                {
                    Object this$originalMessageId = this.getOriginalMessageId();
                    Object other$originalMessageId = other.getOriginalMessageId();
                    if (this$originalMessageId == null) {
                        if (other$originalMessageId == null) {
                            break label57;
                        }
                    } else if (this$originalMessageId.equals(other$originalMessageId)) {
                        break label57;
                    }

                    return false;
                }

                Object this$type = this.getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }

                Object this$serviceId = this.getServiceId();
                Object other$serviceId = other.getServiceId();
                if (this$serviceId == null) {
                    if (other$serviceId != null) {
                        return false;
                    }
                } else if (!this$serviceId.equals(other$serviceId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MessageMeta;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        long $timestamp = this.getTimestamp();
        result = result * 59 + (int) ($timestamp >>> 32 ^ $timestamp);
        result = result * 59 + (this.isAutoRetry() ? 79 : 97);
        Object $messageId = this.getMessageId();
        result = result * 59 + ($messageId == null ? 43 : $messageId.hashCode());
        Object $originalMessageId = this.getOriginalMessageId();
        result = result * 59 + ($originalMessageId == null ? 43 : $originalMessageId.hashCode());
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $serviceId = this.getServiceId();
        result = result * 59 + ($serviceId == null ? 43 : $serviceId.hashCode());
        return result;
    }

    public String toString() {
        return "MessageMeta(messageId=" + this.getMessageId() + ", originalMessageId=" + this.getOriginalMessageId() + ", type=" + this.getType() + ", serviceId=" + this.getServiceId() + ", timestamp=" + this.getTimestamp() + ", autoRetry=" + this.isAutoRetry() + ")";
    }

    public static class MessageMetaBuilder {
        private String messageId;
        private String originalMessageId;
        private EventType type;
        private String serviceId;
        private long timestamp;
        private boolean autoRetry;

        MessageMetaBuilder() {
        }

        public MessageMetaBuilder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public MessageMetaBuilder originalMessageId(String originalMessageId) {
            this.originalMessageId = originalMessageId;
            return this;
        }

        public MessageMetaBuilder type(EventType type) {
            this.type = type;
            return this;
        }

        public MessageMetaBuilder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public MessageMetaBuilder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageMetaBuilder autoRetry(boolean autoRetry) {
            this.autoRetry = autoRetry;
            return this;
        }

        public MessageMeta build() {
            return new MessageMeta(this.messageId, this.originalMessageId, this.type, this.serviceId, this.timestamp, this.autoRetry);
        }

        public String toString() {
            return "MessageMeta.MessageMetaBuilder(messageId=" + this.messageId + ", originalMessageId=" + this.originalMessageId + ", type=" + this.type + ", serviceId=" + this.serviceId + ", timestamp=" + this.timestamp + ", autoRetry=" + this.autoRetry + ")";
        }
    }
}