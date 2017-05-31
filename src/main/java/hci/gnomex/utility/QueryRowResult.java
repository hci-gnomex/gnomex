package hci.gnomex.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryRowResult {
    private Object[] fields;
    private Map<String, Integer> fieldIndexMap;
    private String queryString;

    private static final boolean DEFAULT_COPY_BEHAVIOR = true;

    public QueryRowResult(String queryString, Object[] rowResults, boolean copyRowResults) {
        this.queryString = queryString;
        this.fields = copyRowResults ? Arrays.copyOf(rowResults, rowResults.length) : rowResults;
        this.fieldIndexMap = parseQuery(queryString);
    }

    public QueryRowResult(String queryString, Object[] rowResults) {
        this(queryString, rowResults, DEFAULT_COPY_BEHAVIOR);
    }

    public QueryRowResult(QueryRowResult rowResult) {
        this.fields = Arrays.copyOf(rowResult.fields, rowResult.fields.length);
        this.queryString = rowResult.queryString;
        this.fieldIndexMap = new HashMap<String, Integer>(rowResult.fieldIndexMap);
    }

    public Object getFieldValue(String fieldName) {
        Integer index = fieldIndexMap.get(fieldName);
        return index != null ? fields[index] : null;
    }

    public String getFieldValueString(String fieldName, String defaultString) {
        try {
            String value = (String) getFieldValue(fieldName);
            return value != null ? value : defaultString;
        } catch (ClassCastException e) {
            return defaultString;
        }
    }

    public String getFieldValueString(String fieldName) {
        return getFieldValueString(fieldName, "");
    }

    public Integer getFieldValueInteger(String fieldName) {
        try {
            return (Integer) getFieldValue(fieldName);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public String getFieldValueIntegerAsString(String fieldName, String defaultString) {
        Integer value = getFieldValueInteger(fieldName);
        return value != null ? value.toString() : defaultString;
    }

    public String getFieldValueIntegerAsString(String fieldName) {
        Integer value = getFieldValueInteger(fieldName);
        return value != null ? value.toString() : "";
    }

    public boolean setFieldValue(String fieldName, Object value) {
        Integer index = fieldIndexMap.get(fieldName);
        if (index != null) {
            fields[index] = value;
            return true;
        }
        return false;
    }

    public QueryRowResult getCopy() {
        return new QueryRowResult(this);
    }

    private static Map<String, Integer> parseQuery(String queryString) {
        Map<String, Integer> indexMap = new HashMap<String, Integer>();
        Pattern selectFromPattern = Pattern.compile("SELECT(\\s+DISTINCT\\s+)?(.*)FROM", Pattern.CASE_INSENSITIVE);
        Matcher selectFromMatcher = selectFromPattern.matcher(queryString);
        if (selectFromMatcher.find()) {
            Pattern fieldsPattern = Pattern.compile("\\w+(\\.\\w+)?");
            Matcher fieldsMatcher = fieldsPattern.matcher(selectFromMatcher.group(2));
            int fieldsIndex = 0;
            while (fieldsMatcher.find()) {
                indexMap.put(fieldsMatcher.group(), fieldsIndex++);
            }
        }
        return indexMap;
    }
}
