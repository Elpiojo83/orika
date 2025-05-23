/*
 * Orika - simpler, better and faster Java bean mapping
 *
 *  Copyright (C) 2011-2019 Orika authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ma.glasnost.orika.test.jdk8;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Support for JSR-310 (Date and Time).
 *
 * <p>
 *
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/170">https://github.com/orika-mapper/orika/issues/170</a>
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/96">https://github.com/orika-mapper/orika/issues/96</a>
 */
public class JavaDateApiTest {

  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(JavaDateApiTest.class);

  @Test
  public void testJavaDateApiMappings() {
    DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    // prepare input
    A a = new A();
    a.setInstant(Instant.parse("2007-12-03T10:15:30.00Z"));
    a.setDuration(Duration.parse("-PT6H3M"));
    a.setLocalDate(LocalDate.parse("2007-12-03"));
    a.setLocalTime(LocalTime.parse("10:15:30.00"));
    a.setLocalDateTime(LocalDateTime.parse("2007-12-03T10:15:30.00"));
    a.setZonedDateTime(ZonedDateTime.parse("2007-12-03T10:15:30.00+02:00[Europe/Vienna]"));
    a.setDayOfWeek(DayOfWeek.MONDAY);
    a.setMonth(Month.JULY);
    a.setMonthDay(MonthDay.parse("--12-03"));
    a.setOffsetDateTime(OffsetDateTime.parse("2007-12-03T10:15:30.00+02:00"));
    a.setOffsetTime(OffsetTime.parse("10:15:30.00+02:00"));
    a.setPeriod(Period.parse("-P1Y2M"));
    a.setYear(Year.parse("2007"));
    a.setYearMonth(YearMonth.parse("2007-12"));
    a.setZoneOffset(ZoneOffset.of("+02:00"));

    // run Test:
    A mappedA = mapperFactory.getMapperFacade().map(a, A.class);

    // validate result
    assertThat(mappedA, notNullValue());
    assertThat(mappedA.getInstant(), is(a.getInstant()));
    assertThat(mappedA.getDuration(), is(a.getDuration()));
    assertThat(mappedA.getLocalDate(), is(a.getLocalDate()));
    assertThat(mappedA.getLocalTime(), is(a.getLocalTime()));
    assertThat(mappedA.getLocalDateTime(), is(a.getLocalDateTime()));
    assertThat(mappedA.getZonedDateTime(), is(a.getZonedDateTime()));
    assertThat(mappedA.getDayOfWeek(), is(a.getDayOfWeek()));
    assertThat(mappedA.getMonth(), is(a.getMonth()));
    assertThat(mappedA.getMonthDay(), is(a.getMonthDay()));
    assertThat(mappedA.getOffsetDateTime(), is(a.getOffsetDateTime()));
    assertThat(mappedA.getOffsetTime(), is(a.getOffsetTime()));
    assertThat(mappedA.getOffsetTime(), is(a.getOffsetTime()));
    assertThat(mappedA.getPeriod(), is(a.getPeriod()));
    assertThat(mappedA.getYear(), is(a.getYear()));
    assertThat(mappedA.getYearMonth(), is(a.getYearMonth()));
    assertThat(mappedA.getZoneOffset(), is(a.getZoneOffset()));
  }

  @Test
  public void testJavaDateApiMappings_withCustomConverter_shouldOverwriteDefaultBehavior() {
    DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory
        .getConverterFactory()
        .registerConverter(
            new CustomConverter<Instant, Instant>() {
              public Instant convert(
                  Instant source, Type<? extends Instant> destType, MappingContext mappingContext) {
                if (source == null) {
                  return null;
                }
                // TestCase: add 28 days during Mapping
                return source.plus(Period.parse("P28D"));
              }
            });

    // prepare input
    A a = new A();
    a.setInstant(Instant.parse("2007-03-04T10:15:30.00Z"));

    // run Test:
    A mappedA = mapperFactory.getMapperFacade().map(a, A.class);

    // validate result
    assertThat(mappedA, notNullValue());
    assertThat(mappedA.getInstant(), is(Instant.parse("2007-04-01T10:15:30.00Z")));
  }

  @Test
  public void testJdk8Type_shouldBeImmutable() throws Exception {
    // 1. find all SubClasses of TemporalAccessor, TemporalAmount
    List<Class<?>> jdkClasses = new ArrayList<>();
    jdkClasses.addAll(
        Arrays.asList(
            Instant.class,
            Duration.class,
            LocalDate.class,
            LocalTime.class,
            LocalDateTime.class,
            ZonedDateTime.class,
            DayOfWeek.class,
            Month.class,
            MonthDay.class,
            OffsetDateTime.class,
            OffsetTime.class,
            Period.class,
            Year.class,
            YearMonth.class,
            ZoneOffset.class));

    // 2. filter Classes which should be immutable (TemporalAccessor and TemporalAmount should be
    // implemented as immutable):
    List<Class<?>> immutableJdkClasses =
        jdkClasses.stream().filter(this::immutableRequirements).collect(Collectors.toList());

    LOG.info("Found the following immutable classes: ");
    immutableJdkClasses.forEach((type) -> LOG.info("\t {}", type.getName()));

    // validation: collect classes which are not identified as immutable (there should be none):
    List<Class<?>> missingTypes =
        immutableJdkClasses.stream()
            .filter((type) -> !TypeFactory.valueOf(type).isImmutable())
            .collect(Collectors.toList());

    // throw Validation Exception if there are some classes.
    // This can happen if the JDK introduces new Classes which most likely must be added to
    // ma.glasnost.orika.metadata.Type.
    if (!missingTypes.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Add the following immutable Types to ma.glasnost.orika.metadata.Type:\n");
      missingTypes.forEach(
          (type) -> {
            sb.append(statement(type));
            sb.append('\n');
          });
      LOG.warn(sb.toString());
      Assert.fail(sb.toString());
    }
  }

  private String statement(Class<?> subType) {
    return "        addClassIfExists(tmpImmutableJdk8Types, \"" + subType.getName() + "\");";
  }

  private boolean immutableRequirements(Class<?> subType) {
    return Modifier.isFinal(subType.getModifiers()) // Immutable Classes are typical final
        && Modifier.isPublic(subType.getModifiers()) // We are not interested in non-public classes
        && !subType
            .isEnum(); // We don't need register Enums in ma.glasnost.orika.metadata.Type (they
                       // always are immutable)
  }

  public static class A {

    private Instant instant;
    private Duration duration;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private ZonedDateTime zonedDateTime;
    private DayOfWeek dayOfWeek;
    private Month month;
    private MonthDay monthDay;
    private OffsetDateTime offsetDateTime;
    private OffsetTime offsetTime;
    private Period period;
    private Year year;
    private YearMonth yearMonth;
    private ZoneOffset zoneOffset;

    public Instant getInstant() {
      return instant;
    }

    public void setInstant(Instant instant) {
      this.instant = instant;
    }

    public Duration getDuration() {
      return duration;
    }

    public void setDuration(Duration duration) {
      this.duration = duration;
    }

    public LocalDate getLocalDate() {
      return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
      this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
      return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
      this.localTime = localTime;
    }

    public LocalDateTime getLocalDateTime() {
      return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
      this.localDateTime = localDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
      return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
      this.zonedDateTime = zonedDateTime;
    }

    public DayOfWeek getDayOfWeek() {
      return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
      this.dayOfWeek = dayOfWeek;
    }

    public Month getMonth() {
      return month;
    }

    public void setMonth(Month month) {
      this.month = month;
    }

    public MonthDay getMonthDay() {
      return monthDay;
    }

    public void setMonthDay(MonthDay monthDay) {
      this.monthDay = monthDay;
    }

    public OffsetDateTime getOffsetDateTime() {
      return offsetDateTime;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
      this.offsetDateTime = offsetDateTime;
    }

    public OffsetTime getOffsetTime() {
      return offsetTime;
    }

    public void setOffsetTime(OffsetTime offsetTime) {
      this.offsetTime = offsetTime;
    }

    public Period getPeriod() {
      return period;
    }

    public void setPeriod(Period period) {
      this.period = period;
    }

    public Year getYear() {
      return year;
    }

    public void setYear(Year year) {
      this.year = year;
    }

    public YearMonth getYearMonth() {
      return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
      this.yearMonth = yearMonth;
    }

    public ZoneOffset getZoneOffset() {
      return zoneOffset;
    }

    public void setZoneOffset(ZoneOffset zoneOffset) {
      this.zoneOffset = zoneOffset;
    }
  }
}
