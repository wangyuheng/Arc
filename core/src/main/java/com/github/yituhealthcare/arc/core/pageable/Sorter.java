package com.github.yituhealthcare.arc.core.pageable;

/**
 * @author junhao.chen
 */
public class Sorter {

    private String field;

    private Order order;

    public Sorter(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public Sorter() {
    }

    public static SorterBuilder builder() {
        return new SorterBuilder();
    }

    public String getField() {
        return this.field;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String toString() {
        return "Sorter(field=" + this.getField() + ", order=" + this.getOrder() + ")";
    }

    public static class SorterBuilder {
        private String field;
        private Order order;

        SorterBuilder() {
        }

        public Sorter.SorterBuilder field(String field) {
            this.field = field;
            return this;
        }

        public Sorter.SorterBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public Sorter build() {
            return new Sorter(field, order);
        }

        public String toString() {
            return "Sorter.SorterBuilder(field=" + this.field + ", order=" + this.order + ")";
        }
    }
}
