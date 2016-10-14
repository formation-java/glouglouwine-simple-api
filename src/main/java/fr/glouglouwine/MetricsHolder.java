package fr.glouglouwine;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

public class MetricsHolder {
	public static final MetricsHolder INSTANCE = new MetricsHolder();

	public final MetricRegistry metrics = new MetricRegistry();

	public void startJmxExporter() {
		final JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
		reporter.start();
	}
}
