package com.example.shop.infrastructure.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KafkaMessage<T> extends MessageBase {
    private T payload;

    public KafkaMessage() {
    }

    public T getPayload() {
        return this.payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof KafkaMessage)) {
            return false;
        } else {
            KafkaMessage<?> other = (KafkaMessage) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$payload = this.getPayload();
                Object other$payload = other.getPayload();
                if (this$payload == null) {
                    if (other$payload != null) {
                        return false;
                    }
                } else if (!this$payload.equals(other$payload)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof KafkaMessage;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $payload = this.getPayload();
        result = result * 59 + ($payload == null ? 43 : $payload.hashCode());
        return result;
    }

    public String toString() {
        return "KafkaMessage(payload=" + this.getPayload() + ")";
    }
}