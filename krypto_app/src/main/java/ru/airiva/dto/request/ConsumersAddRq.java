package ru.airiva.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsumersAddRq {
    private String phone;
    private List<Long> consumers;

    public ConsumersAddRq() {
    }

    public ConsumersAddRq(String phone, List<Long> consumers) {
        this.phone = phone;
        this.consumers = consumers;
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Long> getConsumers() {
        return consumers == null ? new ArrayList<>() : consumers;
    }

    public void setConsumers(List<Long> consumers) {
        this.consumers = consumers;
    }
}
