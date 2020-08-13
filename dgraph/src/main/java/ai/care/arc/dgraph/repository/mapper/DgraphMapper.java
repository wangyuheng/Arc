package ai.care.arc.dgraph.repository.mapper;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

