/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigquery;

import com.google.auto.value.AutoValue;
import com.google.cloud.bigquery.JobInfo.CreateDisposition;
import com.google.cloud.bigquery.JobInfo.SchemaUpdateOption;
import com.google.cloud.bigquery.JobInfo.WriteDisposition;
import com.google.cloud.bigquery.QueryJobConfiguration.Priority;
import java.util.List;
import java.util.Map;

/** ConnectionSettings for setting up a BigQuery query connection. */
@AutoValue
public abstract class ConnectionSettings {
  ConnectionSettings() {
    // Package private so users can't subclass it but AutoValue can.
  }

  /** Returns the synchronous response timeoutMs associated with this query */
  public abstract Long getRequestTimeout();

  /** Returns the connection properties for connection string with this query */
  public abstract List<ConnectionProperty> getConnectionProperties();

  /** Returns the default dataset */
  public abstract DatasetId getDefaultDataset();

  /** Returns the limits the bytes billed for this job */
  public abstract Long getMaximumBytesBilled();

  /** Returns the maximum number of rows of data */
  public abstract Long getMaxResults();

  /** Returns the number of rows of data to pre-fetch */
  public abstract Long getPrefetchedRowLimit();

  /** Returns whether to look for the result in the query cache */
  public abstract Boolean getUseQueryCache();

  /**
   * Returns whether nested and repeated fields should be flattened. If set to {@code false} {@link
   * QueryJobConfiguration.Builder#setAllowLargeResults(Boolean)} must be {@code true}.
   *
   * @see <a href="https://cloud.google.com/bigquery/docs/data#flatten">Flatten</a>
   */
  public abstract Boolean getFlattenResults();

  /** Returns the BigQuery Storage read API configuration */
  public abstract ReadClientConnectionConfiguration getReadClientConnectionConfiguration();

  /**
   * Below properties are only supported by jobs.insert API and not yet supported by jobs.query API
   * *
   */

  /** Returns the clustering specification for the destination table. */
  public abstract Clustering getClustering();

  /**
   * Returns whether the job is allowed to create new tables.
   *
   * @see <a
   *     href="https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.createDisposition">
   *     Create Disposition</a>
   */
  public abstract CreateDisposition getCreateDisposition();

  /** Returns the custom encryption configuration (e.g., Cloud KMS keys) */
  public abstract EncryptionConfiguration getDestinationEncryptionConfiguration();

  /**
   * Returns the table where to put query results. If not provided a new table is created. This
   * value is required if {@link #allowLargeResults()} is {@code true}.
   */
  public abstract TableId getDestinationTable();

  /** Returns the timeout associated with this job */
  public abstract Long getJobTimeoutMs();

  /** Returns the optional billing tier limit for this job. */
  public abstract Integer getMaximumBillingTier();

  /** Returns the query priority. */
  public abstract Priority getPriority();

  /**
   * Returns whether the job is enabled to create arbitrarily large results. If {@code true} the
   * query is allowed to create large results at a slight cost in performance. the query is allowed
   * to create large results at a slight cost in performance.
   *
   * @see <a href="https://cloud.google.com/bigquery/querying-data#largequeryresults">Returning
   *     Large Query Results</a>
   */
  public abstract Boolean getAllowLargeResults();

  /** Returns the range partitioning specification for the table */
  public abstract RangePartitioning getRangePartitioning();

  /**
   * [Experimental] Returns options allowing the schema of the destination table to be updated as a
   * side effect of the query job. Schema update options are supported in two cases: when
   * writeDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination
   * table is a partition of a table, specified by partition decorators. For normal tables,
   * WRITE_TRUNCATE will always overwrite the schema.
   */
  public abstract List<SchemaUpdateOption> getSchemaUpdateOptions();

  /**
   * Returns the external tables definitions. If querying external data sources outside of BigQuery,
   * this value describes the data format, location and other properties of the data sources. By
   * defining these properties, the data sources can be queried as if they were standard BigQuery
   * tables.
   */
  public abstract Map<String, ExternalTableDefinition> getTableDefinitions();

  /** Returns the time partitioning specification for the destination table. */
  public abstract TimePartitioning getTimePartitioning();

  /**
   * Returns user defined function resources that can be used by this query. Function resources can
   * either be defined inline ({@link UserDefinedFunction.Type#INLINE}) or loaded from a Google
   * Cloud Storage URI ({@link UserDefinedFunction.Type#FROM_URI}.
   */
  public abstract List<UserDefinedFunction> getUserDefinedFunctions();

  /**
   * Returns the action that should occur if the destination table already exists.
   *
   * @see <a
   *     href="https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition">
   *     Write Disposition</a>
   */
  public abstract WriteDisposition getWriteDisposition();

  /** Returns a builder pre-populated using the current values of this field. */
  public abstract Builder toBuilder();

  /** Returns a builder for a {@code ConnectionSettings} object. */
  public static Builder newBuilder() {
    return new AutoValue_ConnectionSettings.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Sets how long to wait for the query to complete, in milliseconds, before the request times
     * out and returns. Note that this is only a timeout for the request, not the query. If the
     * query takes longer to run than the timeout value, the call returns without any results and
     * with the 'jobComplete' flag set to false. You can call GetQueryResults() to wait for the
     * query to complete and read the results. The default value is 10000 milliseconds (10 seconds).
     *
     * @param timeoutMs or {@code null} for none
     */
    public abstract Builder setRequestTimeout(Long timeoutMs);

    /**
     * Sets a connection-level property to customize query behavior.
     *
     * @param connectionProperties connectionProperties or {@code null} for none
     */
    public abstract Builder setConnectionProperties(List<ConnectionProperty> connectionProperties);

    /**
     * Sets the default dataset. This dataset is used for all unqualified table names used in the
     * query.
     */
    public abstract Builder setDefaultDataset(DatasetId datasetId);

    /**
     * Limits the bytes billed for this job. Queries that will have bytes billed beyond this limit
     * will fail (without incurring a charge). If unspecified, this will be set to your project
     * default.
     *
     * @param maximumBytesBilled maximum bytes billed for this job
     */
    public abstract Builder setMaximumBytesBilled(Long maximumBytesBilled);

    /**
     * Sets the maximum number of rows of data to return per page of results. Setting this flag to a
     * small value such as 1000 and then paging through results might improve reliability when the
     * query result set is large. In addition to this limit, responses are also limited to 10 MB. By
     * default, there is no maximum row count, and only the byte limit applies.
     *
     * @param maxResults maxResults or {@code null} for none
     */
    public abstract Builder setMaxResults(Long maxResults);

    /**
     * Sets the number of rows of data to pre-fetch during query execution.
     *
     * @param prefetchedRowLimit prefetchedRowLimit or {@code null} for none
     */
    public abstract Builder setPrefetchedRowLimit(Long prefetchedRowLimit);

    /**
     * Sets whether to look for the result in the query cache. The query cache is a best-effort
     * cache that will be flushed whenever tables in the query are modified. Moreover, the query
     * cache is only available when {@link
     * QueryJobConfiguration.Builder#setDestinationTable(TableId)} is not set.
     *
     * @see <a href="https://cloud.google.com/bigquery/querying-data#querycaching">Query Caching</a>
     */
    public abstract Builder setUseQueryCache(Boolean useQueryCache);

    /**
     * Sets whether nested and repeated fields should be flattened. If set to {@code false} {@link
     * QueryJobConfiguration.Builder#setAllowLargeResults(Boolean)} must be {@code true}. By default
     * results are flattened.
     *
     * @see <a href="https://cloud.google.com/bigquery/docs/data#flatten">Flatten</a>
     */
    public abstract Builder setFlattenResults(Boolean flattenResults);

    /**
     * Sets the values necessary to determine whether table result will be read using the BigQuery
     * Storage client Read API. The BigQuery Storage client Read API will be used to read the query
     * result when the totalToFirstPageSizeRatio (default 3) and minimumTableSize (default 100MB)
     * conditions set are met. A ReadSession will be created using Apache Avro data format for
     * serialization.
     *
     * <p>It also sets the maximum number of table rows allowed in buffer before streaming them to
     * the BigQueryResultSet.
     *
     * @param readClientConnectionConfiguration or {@code null} for none
     */
    public abstract Builder setReadClientConnectionConfiguration(
        ReadClientConnectionConfiguration readClientConnectionConfiguration);

    /** Sets the clustering specification for the destination table. */
    public abstract Builder setClustering(Clustering clustering);

    /**
     * Sets whether the job is allowed to create tables.
     *
     * @see <a
     *     href="https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.createDisposition">
     *     Create Disposition</a>
     */
    public abstract Builder setCreateDisposition(CreateDisposition createDisposition);

    /**
     * Sets the custom encryption configuration (e.g., Cloud KMS keys).
     *
     * @param destinationEncryptionConfiguration destinationEncryptionConfiguration or {@code null}
     *     for none
     */
    public abstract Builder setDestinationEncryptionConfiguration(
        EncryptionConfiguration destinationEncryptionConfiguration);

    /**
     * Sets the table where to put query results. If not provided a new table is created. This value
     * is required if {@link QueryJobConfiguration.Builder#setAllowLargeResults(Boolean)} is set to
     * {@code true}.
     */
    public abstract Builder setDestinationTable(TableId destinationTable);

    /**
     * [Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt
     * to terminate the job.
     *
     * @param jobTimeoutMs jobTimeoutMs or {@code null} for none
     */
    public abstract Builder setJobTimeoutMs(Long jobTimeoutMs);

    /**
     * Limits the billing tier for this job. Queries that have resource usage beyond this tier will
     * fail (without incurring a charge). If unspecified, this will be set to your project default.
     *
     * @param maximumBillingTier maximum billing tier for this job
     */
    public abstract Builder setMaximumBillingTier(Integer maximumBillingTier);

    /**
     * Sets a priority for the query. If not specified the priority is assumed to be {@link
     * Priority#INTERACTIVE}.
     */
    public abstract Builder setPriority(Priority priority);

    /**
     * Sets whether the job is enabled to create arbitrarily large results. If {@code true} the
     * query is allowed to create large results at a slight cost in performance. If {@code true}
     * {@link ConnectionSettings.Builder#setDestinationTable(TableId)} must be provided.
     *
     * @see <a href="https://cloud.google.com/bigquery/querying-data#largequeryresults">Returning
     *     Large Query Results</a>
     */
    public abstract Builder setAllowLargeResults(Boolean allowLargeResults);

    /**
     * Range partitioning specification for this table. Only one of timePartitioning and
     * rangePartitioning should be specified.
     *
     * @param rangePartitioning rangePartitioning or {@code null} for none
     */
    public abstract Builder setRangePartitioning(RangePartitioning rangePartitioning);

    /**
     * [Experimental] Sets options allowing the schema of the destination table to be updated as a
     * side effect of the query job. Schema update options are supported in two cases: when
     * writeDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination
     * table is a partition of a table, specified by partition decorators. For normal tables,
     * WRITE_TRUNCATE will always overwrite the schema.
     */
    public abstract Builder setSchemaUpdateOptions(List<SchemaUpdateOption> schemaUpdateOptions);

    /**
     * Sets the external tables definitions. If querying external data sources outside of BigQuery,
     * this value describes the data format, location and other properties of the data sources. By
     * defining these properties, the data sources can be queried as if they were standard BigQuery
     * tables.
     */
    public abstract Builder setTableDefinitions(
        Map<String, ExternalTableDefinition> tableDefinitions);

    /** Sets the time partitioning specification for the destination table. */
    public abstract Builder setTimePartitioning(TimePartitioning timePartitioning);

    /**
     * Sets user defined function resources that can be used by this query. Function resources can
     * either be defined inline ({@link UserDefinedFunction#inline(String)}) or loaded from a Google
     * Cloud Storage URI ({@link UserDefinedFunction#fromUri(String)}.
     */
    public abstract Builder setUserDefinedFunctions(List<UserDefinedFunction> userDefinedFunctions);

    /**
     * Sets the action that should occur if the destination table already exists.
     *
     * @see <a
     *     href="https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition">
     *     Write Disposition</a>
     */
    public abstract Builder setWriteDisposition(WriteDisposition writeDisposition);

    /** Creates a {@code ConnectionSettings} object. */
    public abstract ConnectionSettings build();
  }
}