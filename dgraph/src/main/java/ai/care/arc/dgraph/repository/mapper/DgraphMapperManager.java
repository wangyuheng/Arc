package ai.care.arc.dgraph.repository.mapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class DgraphMapperManager implements InitializingBean {

    private static final Map<String, String> PATH_AND_SQL = new HashMap<>();
    private static final List<DgraphMapper> MAPPERS = new ArrayList<>();
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DgraphMapperManager.class);

    @Value("${dgraph.location:dgraph}")
    private String location;

    private static final String DGRAPH_FILE_SUFFIX = "Dgraph.xml";

    public String getSql(String path) {
        return PATH_AND_SQL.get(path.toUpperCase());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(location + "/*" + DGRAPH_FILE_SUFFIX);
            for (Resource resource : resources) {
                DgraphMapper mapper = xmlMapper.readValue(resource.getInputStream(), DgraphMapper.class);
                MAPPERS.add(mapper);
                final String baseName = Objects.requireNonNull(resource.getFilename()).replace(DGRAPH_FILE_SUFFIX, "");
                mapper.getQueries()
                        .forEach(dgraphQuery -> this.addSql(baseName, dgraphQuery.id, dgraphQuery.value, mapper.getVars()));
                mapper.getMutations()
                        .forEach(dgraphMutation -> this.addSql(baseName, dgraphMutation.id, dgraphMutation.value, mapper.getVars()));
            }
        } catch (FileNotFoundException fileNotFoundException) {
            log.warn("dgraph location is not found! location:{}", location);
        } catch (IOException e) {
            log.error("read dgraph xml fail! location:{}", location, e);
        }

    }

    private void addSql(String baseName, String id, String value, List<DgraphMapper.DgraphVar> vars) {
        String path = baseName + "." + id;
        String sql = value;
        if (!CollectionUtils.isEmpty(vars)) {
            for (DgraphMapper.DgraphVar dgraphVar : vars) {
                sql = sql.replaceAll("#" + dgraphVar.id, Matcher.quoteReplacement(dgraphVar.value));
            }
        }
        PATH_AND_SQL.put(path.toUpperCase(), sql);
    }

}
