package ai.care.arc.dgraph.repository.mapper;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "dgraph")
public class DgraphMapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "query")
    private List<DgraphQuery> queries = new ArrayList<>();

    @JacksonXmlCData
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "mutation")
    private List<DgraphMutation> mutations = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "var")
    private List<DgraphVar> vars;

    public DgraphMapper() {
    }

    public List<DgraphQuery> getQueries() {
        return this.queries;
    }

    public List<DgraphMutation> getMutations() {
        return this.mutations;
    }

    public List<DgraphVar> getVars() {
        return this.vars;
    }

    public void setQueries(List<DgraphQuery> queries) {
        this.queries = queries;
    }

    public void setMutations(List<DgraphMutation> mutations) {
        this.mutations = mutations;
    }

    public void setVars(List<DgraphVar> vars) {
        this.vars = vars;
    }

    public String toString() {
        return "DgraphMapper(queries=" + this.getQueries() + ", mutations=" + this.getMutations() + ", vars=" + this.getVars() + ")";
    }

    static class DgraphVar {
        @JacksonXmlProperty(isAttribute = true)
        public String id;
        @JacksonXmlText
        public String value;
    }

    static class DgraphQuery {
        @JacksonXmlProperty(isAttribute = true)
        public String id;
        @JacksonXmlText
        public String value;
    }

    static class DgraphMutation {
        @JacksonXmlProperty(isAttribute = true)
        public String id;
        @JacksonXmlText
        public String value;
    }

}

