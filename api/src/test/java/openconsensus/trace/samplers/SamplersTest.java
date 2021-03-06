/*
 * Copyright 2019, OpenConsensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openconsensus.trace.samplers;

import static com.google.common.truth.Truth.assertThat;
import static openconsensus.trace.TestUtils.generateRandomSpanId;
import static openconsensus.trace.TestUtils.generateRandomTraceId;

import java.util.Collections;
import java.util.Random;
import openconsensus.trace.Span;
import openconsensus.trace.SpanContext;
import openconsensus.trace.SpanId;
import openconsensus.trace.TraceId;
import openconsensus.trace.TraceOptions;
import openconsensus.trace.Tracestate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link Samplers}. */
@RunWith(JUnit4.class)
public class SamplersTest {
  private final Random random = new Random(1234);
  private final TraceId traceId = generateRandomTraceId(random);
  private final SpanId parentSpanId = generateRandomSpanId(random);
  private final SpanId spanId = generateRandomSpanId(random);
  private final Tracestate tracestate = Tracestate.builder().build();
  private final SpanContext sampledSpanContext =
      SpanContext.create(
          traceId, parentSpanId, TraceOptions.builder().setIsSampled(true).build(), tracestate);
  private final SpanContext notSampledSpanContext =
      SpanContext.create(traceId, parentSpanId, TraceOptions.DEFAULT, tracestate);

  @Test
  public void alwaysSampleSampler_AlwaysReturnTrue() {
    // Sampled parent.
    assertThat(
            Samplers.alwaysSample()
                .shouldSample(
                    sampledSpanContext,
                    false,
                    traceId,
                    spanId,
                    "Another name",
                    Collections.<Span>emptyList()))
        .isTrue();
    // Not sampled parent.
    assertThat(
            Samplers.alwaysSample()
                .shouldSample(
                    notSampledSpanContext,
                    false,
                    traceId,
                    spanId,
                    "Yet another name",
                    Collections.<Span>emptyList()))
        .isTrue();
  }

  @Test
  public void alwaysSampleSampler_ToString() {
    assertThat(Samplers.alwaysSample().toString()).isEqualTo("AlwaysSampleSampler");
  }

  @Test
  public void neverSampleSampler_AlwaysReturnFalse() {
    // Sampled parent.
    assertThat(
            Samplers.neverSample()
                .shouldSample(
                    sampledSpanContext,
                    false,
                    traceId,
                    spanId,
                    "bar",
                    Collections.<Span>emptyList()))
        .isFalse();
    // Not sampled parent.
    assertThat(
            Samplers.neverSample()
                .shouldSample(
                    notSampledSpanContext,
                    false,
                    traceId,
                    spanId,
                    "quux",
                    Collections.<Span>emptyList()))
        .isFalse();
  }

  @Test
  public void neverSampleSampler_ToString() {
    assertThat(Samplers.neverSample().toString()).isEqualTo("NeverSampleSampler");
  }
}
