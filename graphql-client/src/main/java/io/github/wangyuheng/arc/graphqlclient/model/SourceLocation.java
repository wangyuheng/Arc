package io.github.wangyuheng.arc.graphqlclient.model;

/**
 * the location(s) within the GraphQL document
 *
 * @author yuheng.wang
 */
public class SourceLocation {

    private int line;
    private int column;
    private String sourceName;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return "SourceLocation{" +
                "line=" + line +
                ", column=" + column +
                ", sourceName='" + sourceName + '\'' +
                '}';
    }
}
