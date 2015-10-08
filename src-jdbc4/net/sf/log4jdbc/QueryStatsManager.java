/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.log4jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yang.wang
 */
public class QueryStatsManager {

    private final Map<String, List<Double>> queryStatsMap = new HashMap<>();
    private final Map<String, Double> estimatedSubtreeCostMap = new HashMap<>();
    private boolean isLogging;
    private boolean clearingCache;
    private boolean gettingESC;

    private static class QueryTimeManagerSingletonInstance {

        private static final QueryStatsManager instance = new QueryStatsManager();
    }

    public static QueryStatsManager getInstance() {
        return QueryTimeManagerSingletonInstance.instance;
    }

    private QueryStatsManager() {
        reset();
    }

    public void reset() {
        queryStatsMap.clear();
    }

    public Map<String, List<Double>> getExecutionTimes() {
        return queryStatsMap;
    }

    public List<Double> findStats( String sql ) {
        return queryStatsMap.get( sql );
    }

    public void queryExecuted( String sql, Double runtime ) {
        List<Double> runtimes = queryStatsMap.get( sql );

        if ( runtimes == null ) {
            runtimes = new ArrayList<>();
            queryStatsMap.put( sql, runtimes );
        }

        runtimes.add( runtime );
    }

    public boolean doesEstimatedSubtreeCostExist( String query ) {
        return estimatedSubtreeCostMap.get( query ) != null;
    }

    public void putEstimatedSubtreeCost( String query, Double estimatedSubtreeCost ) {
        estimatedSubtreeCostMap.put( query, estimatedSubtreeCost );
    }

    public Double getEstimateSubtreeCost( String query ) {
        return estimatedSubtreeCostMap.get( query );
    }

    public void setLogging( boolean isLogging ) {
        this.isLogging = isLogging;
    }

    public boolean isLogging() {
        return isLogging;
    }

    public void setClearingCache( boolean clearingCache ) {
        this.clearingCache = clearingCache;
    }

    public boolean isClearingCache() {
        return clearingCache;
    }

    public void setGettingEstimatedSubtreeCost( boolean gettingESC ) {
        this.gettingESC = gettingESC;
    }

    public boolean isGettingESC() {
        return gettingESC;
    }
}
