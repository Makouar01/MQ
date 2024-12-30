package com.ayoub.project_management.service.project;

import com.ayoub.project_management.model.PlanType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlanTypeTest {

    @Test
    void testEnumValues() {
        // Test if the enum contains expected values
        assertSame(PlanType.FREE, PlanType.valueOf("FREE"));
        assertSame(PlanType.MONTHLY, PlanType.valueOf("MONTHLY"));
        assertSame(PlanType.ANNUALLY, PlanType.valueOf("ANNUALLY"));
    }

    @Test
    void testEnumName() {
        // Test the name() method of the enum
        assertEquals("FREE", PlanType.FREE.name());
        assertEquals("MONTHLY", PlanType.MONTHLY.name());
        assertEquals("ANNUALLY", PlanType.ANNUALLY.name());
    }

    @Test
    void testEnumComparison() {
        // Test comparison of enum values using `==`
        assertSame(PlanType.FREE, PlanType.FREE);
        assertNotSame(PlanType.FREE, PlanType.MONTHLY);
    }

    @Test
    void testEnumValueOf() {
        // Test if valueOf correctly returns an enum from a string
        assertEquals(PlanType.FREE, PlanType.valueOf("FREE"));
        assertEquals(PlanType.MONTHLY, PlanType.valueOf("MONTHLY"));
        assertEquals(PlanType.ANNUALLY, PlanType.valueOf("ANNUALLY"));
    }

    @Test
    void testEnumValuesLength() {
        // Test if the enum has the correct number of values
        assertEquals(3, PlanType.values().length);
    }

    @Test
    void testEnumValuesIteration() {
        // Test if all enum values can be iterated over
        PlanType[] planTypes = PlanType.values();
        assertNotNull(planTypes);
        assertEquals(3, planTypes.length);
    }
}

