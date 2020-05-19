/*
 * Copyright (c) 2018 AppDynamics,Inc.
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

package com.appdynamics.extensions.aws.kinesis.firehose;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.config.Configuration;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.google.common.collect.Lists;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.aws.kinesis.firehose.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.extensions.aws.kinesis.firehose.util.Constants.MONITOR_NAME;

/**
 * Created by pradeep.nair on 8/7/18.
 */
public class KinesisDataFirehoseMonitor extends SingleNamespaceCloudwatchMonitor<Configuration> {

    private static final Logger LOGGER = ExtensionsLoggerFactory.getLogger(KinesisDataFirehoseMonitor.class);

    public KinesisDataFirehoseMonitor() {
        super(Configuration.class);
        LOGGER.info(String.format("Using AWS Kinesis Data Firehose Monitor Version [%s]",
                this.getClass().getPackage().getImplementationTitle()));
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(Configuration config) {
        MetricsProcessor metricsProcessor = createMetricsProcessor(config);
        return new NamespaceMetricStatisticsCollector.Builder(config.getAccounts(),
                config.getConcurrencyConfig(),
                config.getMetricsConfig(),
                metricsProcessor,
                config.getMetricPrefix())
                .withCredentialsDecryptionConfig(config.getCredentialsDecryptionConfig())
                .withProxyConfig(config.getProxyConfig())
                .build();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return Lists.newArrayList();
    }

    private MetricsProcessor createMetricsProcessor(Configuration config) {
        return new KinesisDataFirehoseMetricsProcessor(config.getMetricsConfig().getIncludeMetrics(),
                config.getDimensions());
    }
}

