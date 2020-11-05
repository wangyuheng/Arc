package ai.care.arc.core.pageable;

import java.util.List;


/**
 * @author junhao.chen
 * @date 2020/6/15
 */
public class QueryBody {

    private Sorter sorter;
    private Pagination pagination;
    private List<Filter> filters;
    private String searchText;
    private List<String> searchFields;

    public QueryBody(Sorter sorter, Pagination pagination, List<Filter> filters, String searchText, List<String> searchFields) {
        this.sorter = sorter;
        this.pagination = pagination;
        this.filters = filters;
        this.searchText = searchText;
        this.searchFields = searchFields;
    }

    public QueryBody() {
    }

    public static QueryBodyBuilder builder() {
        return new QueryBodyBuilder();
    }

    public Sorter getSorter() {
        return this.sorter;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    public String getSearchText() {
        return this.searchText;
    }

    public List<String> getSearchFields() {
        return this.searchFields;
    }

    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setSearchFields(List<String> searchFields) {
        this.searchFields = searchFields;
    }

    public String toString() {
        return "QueryBody(sorter=" + this.getSorter() + ", pagination=" + this.getPagination() + ", filters=" + this.getFilters() + ", searchText=" + this.getSearchText() + ", searchFields=" + this.getSearchFields() + ")";
    }

    public static class QueryBodyBuilder {
        private Sorter sorter;
        private Pagination pagination;
        private List<Filter> filters;
        private String searchText;
        private List<String> searchFields;

        QueryBodyBuilder() {
        }

        public QueryBody.QueryBodyBuilder sorter(Sorter sorter) {
            this.sorter = sorter;
            return this;
        }

        public QueryBody.QueryBodyBuilder pagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public QueryBody.QueryBodyBuilder filters(List<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public QueryBody.QueryBodyBuilder searchText(String searchText) {
            this.searchText = searchText;
            return this;
        }

        public QueryBody.QueryBodyBuilder searchFields(List<String> searchFields) {
            this.searchFields = searchFields;
            return this;
        }

        public QueryBody build() {
            return new QueryBody(sorter, pagination, filters, searchText, searchFields);
        }

        public String toString() {
            return "QueryBody.QueryBodyBuilder(sorter=" + this.sorter + ", pagination=" + this.pagination + ", filters=" + this.filters + ", searchText=" + this.searchText + ", searchFields=" + this.searchFields + ")";
        }
    }
}
