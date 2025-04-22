package com.example.shop.infrastructure.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageBase {
    MessageMeta meta;
    private String messageCode;

    public MessageBase() {
    }

    public MessageMeta getMeta() {
        return this.meta;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public void setMeta(MessageMeta meta) {
        this.meta = meta;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MessageBase)) {
            return false;
        } else {
            MessageBase other = (MessageBase) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$meta = this.getMeta();
                Object other$meta = other.getMeta();
                if (this$meta == null) {
                    if (other$meta != null) {
                        return false;
                    }
                } else if (!this$meta.equals(other$meta)) {
                    return false;
                }

                Object this$messageCode = this.getMessageCode();
                Object other$messageCode = other.getMessageCode();
                if (this$messageCode == null) {
                    if (other$messageCode != null) {
                        return false;
                    }
                } else if (!this$messageCode.equals(other$messageCode)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MessageBase;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $meta = this.getMeta();
        result = result * 59 + ($meta == null ? 43 : $meta.hashCode());
        Object $messageCode = this.getMessageCode();
        result = result * 59 + ($messageCode == null ? 43 : $messageCode.hashCode());
        return result;
    }

    public String toString() {
        return "MessageBase(meta=" + this.getMeta() + ", messageCode=" + this.getMessageCode() + ")";
    }
}