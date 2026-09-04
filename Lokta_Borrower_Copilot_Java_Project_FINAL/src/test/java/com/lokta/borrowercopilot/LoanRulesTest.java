package com.lokta.borrowercopilot;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanRulesTest {
    private Map<String,Object> base() {
        Map<String,Object> m = new HashMap<>();
        m.put("loanType","PERSONAL"); m.put("purpose","WEDDING"); m.put("requested",800000);
        m.put("age",29); m.put("income",110000); m.put("incomeType","SALARIED");
        m.put("existingEmi",14000); m.put("expenses",28000); m.put("variablePct",0);
        m.put("creditScore",780); m.put("collateral",0);
        return m;
    }

    @Test void unknownScoreWidensRateBand() {
        var known = LoanRules.assess(base());
        var x = base(); x.remove("creditScore");
        var unknown = LoanRules.assess(x);
        assertTrue(unknown.fairHigh - unknown.fairLow > known.fairHigh - known.fairLow);
        assertTrue(unknown.confidence < known.confidence);
    }

    @Test void excessiveBurdenCanTriggerDontBorrow() {
        var x = base(); x.put("existingEmi", 50000); x.put("expenses", 50000);
        assertEquals("DON'T BORROW", LoanRules.assess(x).verdict());
    }

    @Test void lenderAndSafeNumbersAreSeparate() {
        var r = LoanRules.assess(base());
        assertTrue(r.lenderMax() >= r.safeMax());
    }
}
