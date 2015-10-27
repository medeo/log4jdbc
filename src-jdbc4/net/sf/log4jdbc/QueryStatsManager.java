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
    private boolean trackingEstimatedSubtreeCost;

    private static class QueryTimeManagerSingletonInstance {

        private static final QueryStatsManager instance = new QueryStatsManager();
    }

    /**
     * @return singleton instance of QueryStatsManager
     */
    public static QueryStatsManager getInstance() {
        return QueryTimeManagerSingletonInstance.instance;
    }

    private QueryStatsManager() {
        reset();
    }

    /**
     * reset the all stats collected previously, including executed query and execution time
     */
    public void reset() {
        queryStatsMap.clear();
    }

    /**
     * @return A map including all executed queries and its corresponding execution times
     */
    public Map<String, List<Double>> getExecutionTimes() {
        return queryStatsMap;
    }

    /**
     * @param sql
     * @return a list of execution times in milliseconds
     */
    public List<Double> findStats( String sql ) {
        return queryStatsMap.get( sql );
    }

    /**
     * A sql execution was noticed and now QueryStatsManager needs to record this execution
     *
     * @param sql
     * @param runtime the execution time (in milliseconds) for the sql
     */
    public void queryExecuted( String sql, Double runtime ) {
        List<Double> runtimes = queryStatsMap.get( sql );

        if ( runtimes == null ) {
            runtimes = new ArrayList<>();
            queryStatsMap.put( sql, runtimes );
        }

        runtimes.add( runtime );
    }

    /**
     * find if a Estimated Subtree Cost exist for the query
     *
     * @param query
     * @return true if Estimated Subtree Cost exist for the query
     */
    public boolean doesEstimatedSubtreeCostExist( String query ) {
        return estimatedSubtreeCostMap.get( query ) != null;
    }

    /**
     * record the Estimated Subtree Cost for the query
     *
     * @param query
     * @param estimatedSubtreeCost
     */
    public void putEstimatedSubtreeCost( String query, Double estimatedSubtreeCost ) {
        estimatedSubtreeCostMap.put( query, estimatedSubtreeCost );
    }

    /**
     * get the Estimated Subtree Cost for the query
     *
     * @param query
     * @return the Estimated Subtree Cost
     */
    public Double getEstimateSubtreeCost( String query ) {
        return estimatedSubtreeCostMap.get( query );
    }

    /**
     * setter for isLogging flag, true if SQL events are expected to be logged, false otherwise
     *
     * @param isLogging
     */
    public void setLogging( boolean isLogging ) {
        this.isLogging = isLogging;
    }

    /**
     * getter for isLogging flag, true if SQL events are expected to be logged, false otherwise
     *
     * @return
     */
    public boolean isLogging() {
        return isLogging;
    }

    /**
     * setter for clearingCache flag, during Integration testing, SQL cache
     * should be cleared after every execution for accurate data
     *
     * @param clearingCache
     */

    public void setClearingCache( boolean clearingCache ) {
        this.clearingCache = clearingCache;
    }

    /**
     * getter for clearingCache flag, during Integration testing, SQL cache
     * should be cleared after every execution for accurate data
     *
     * @return
     */
    public boolean isClearingCache() {
        return clearingCache;
    }

    /**
     * setter for trackingEstimatedSubtreeCost, true if Estimated Subtree Cost is going to be collected
     *
     * @param trackingEstimatedSubtreeCost
     */
    public void setGettingEstimatedSubtreeCost( boolean trackingEstimatedSubtreeCost ) {
        this.trackingEstimatedSubtreeCost = trackingEstimatedSubtreeCost;
    }

    /**
     * getter for trackingEstimatedSubtreeCost, true if Estimated Subtree Cost is going to be collected
     *
     * @return if Estimated Subtree Cost is going to be collected
     */
    public boolean trackingQueryEstimatedSubtreeCost() {
        return trackingEstimatedSubtreeCost;
    }
}
