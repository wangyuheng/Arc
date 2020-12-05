package com.github.yituhealthcare.arc.core.pageable;

import java.util.List;

/**
 * @author junhao.chen
 */
public class Filter {

    private FilterOperator operator;

    private String field;

    private List<String> value;

    public Filter(FilterOperator operator, String field, List<String> value) {
        this.operator = operator;
        this.field = field;
        this.value = value;
    }

    public Filter() {
    }

    public static FilterBuilder builder() {
        return new FilterBuilder();
    }

    public FilterOperator getOperator() {
        return this.operator;
    }

    public String getField() {
        return this.field;
    }

    public List<String> getValue() {
        return this.value;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public String toString() {
        return "Filter(operator=" + this.getOperator() + ", field=" + this.getField() + ", value=" + this.getValue() + ")";
    }

    public static class FilterBuilder {
        private FilterOperator operator;
        private String field;
        private List<String> value;

        FilterBuilder() {
        }

        public Filter.FilterBuilder operator(FilterOperator operator) {
            this.operator = operator;
            return this;
        }

        public Filter.FilterBuilder field(String field) {
            this.field = field;
            return this;
        }

        public Filter.FilterBuilder value(List<String> value) {
            this.value = value;
            return this;
        }

        public Filter build() {
            return new Filter(operator, field, value);
        }

        public String toString() {
            return "Filter.FilterBuilder(operator=" + this.operator + ", field=" + this.field + ", value=" + this.value + ")";
        }
    }
}