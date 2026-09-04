package com.lokta.borrowercopilot;

import java.util.*;

public final class LoanRules {
    private LoanRules() {}

    public record Result(
            String verdict,
            String verdictReason,
            String product,
            double lenderMax,
            double safeMax,
            double fairLow,
            double fairHigh,
            double processingFeePct,
            double emiCeiling,
            double baseRate,
            double stressRate,
            int baseTenureMonths,
            int stressTenureMonths,
            int confidence,
            List<String> reasons,
            List<String> warnings) {}

    public static Result assess(Map<String, Object> a) {
        double income = n(a, "income");
        double existingEmi = n(a, "existingEmi");
        double expenses = n(a, "expenses");
        double requested = n(a, "requested");
        int age = (int)n(a, "age");
        double score = n(a, "creditScore");
        double collateral = n(a, "collateral");
        double variablePct = n(a, "variablePct");
        String incomeType = s(a, "incomeType");
        String loanType = s(a, "loanType");
        String purpose = s(a, "purpose");
        double fee = feeFor(loanType);
        double lenderFoir = foirCap(loanType);
        double borrowerFoir = safeFoir(loanType);

        double lenderMonthly = Math.max(0, income * lenderFoir - existingEmi);
        double safeMonthly = Math.max(0, income * borrowerFoir - existingEmi);
        // A second affordability guard: preserve the entered household expense level.
        safeMonthly = Math.min(safeMonthly, Math.max(0, income - existingEmi - expenses - income * 0.10));

        double lenderMax = pvToPrincipal(lenderMonthly, rateFor(loanType, score, incomeType), 60);
        if (loanType.equals("BUSINESS") && collateral > 0) lenderMax = Math.min(Math.max(lenderMax, collateral * 0.50), collateral * 0.65);
        double safeMax = pvToPrincipal(safeMonthly, rateFor(loanType, score, incomeType), 60);
        if (loanType.equals("BUSINESS") && collateral > 0) safeMax = Math.min(safeMax, collateral * 0.55);

        double baseRate = rateFor(loanType, score, incomeType);
        double fairLow = Math.max(8.0, baseRate - 1.25);
        double fairHigh = baseRate + 1.75;
        if (score <= 0) { fairLow += 1.0; fairHigh += 2.0; }
        if (variablePct > 50) { fairLow += 0.5; fairHigh += 0.75; }
        if (incomeType.equals("INFORMAL")) { fairLow += 1.0; fairHigh += 2.0; }

        double emiCeiling = safeMonthly;
        String verdict;
        String reason;
        if (existingEmi > income * 0.35 || expenses + existingEmi > income * 0.80) {
            verdict = "DON'T BORROW";
            reason = "Existing obligations and household outflow leave too little resilient monthly cash flow.";
        } else if (requested > safeMax * 1.20) {
            verdict = "BORROW LESS";
            reason = "The requested amount is materially above the amount this profile can safely carry.";
        } else {
            verdict = "BORROW";
            reason = purpose.equals("WEDDING")
                    ? "The requested loan is affordable within the calculated ceiling, but it is discretionary spending, so keep the amount conservative."
                    : "The requested amount fits within the calculated affordability range.";
        }

        List<String> reasons = new ArrayList<>();
        reasons.add("Safe EMI uses a " + pct(borrowerFoir) + " borrower affordability cap after existing EMI.");
        reasons.add("Lender capacity uses a " + pct(lenderFoir) + " FOIR-style cap; this is not a lender underwriting decision.");
        reasons.add("Fair-rate band starts from product and profile assumptions, then widens for unknown score, variable income, and informal income.");
        if (loanType.equals("BUSINESS") && collateral > 0) reasons.add("Secured-business capacity is also capped against 50–65% of declared collateral value.");
        reasons.add("APR comparison should add the processing fee to interest cost; this prototype displays the fee separately so the quote can be normalized.");

        List<String> warnings = new ArrayList<>();
        if (score <= 0) warnings.add("Credit score unknown: the rate band is wider rather than treating unknown as a low score.");
        if (incomeType.equals("INFORMAL")) warnings.add("Income is informal/variable; lender acceptance may be materially narrower than the affordability math.");
        if (requested > lenderMax) warnings.add("Requested amount is above the illustrative lender-sanction capacity.");
        if (age > 55) warnings.add("Age can reduce maximum tenure with some lenders; this prototype uses a conservative 60-month calculation.");
        int confidence = 72;
        if (score <= 0) confidence -= 18;
        if (income <= 0 || expenses <= 0) confidence -= 25;
        if (variablePct > 50) confidence -= 8;
        confidence = Math.max(30, Math.min(92, confidence));

        double stressRate = baseRate + 2.0;
        return new Result(verdict, reason, loanType, round(lenderMax), round(safeMax),
                round(fairLow), round(fairHigh), fee, round(emiCeiling), baseRate, stressRate,
                60, 48, confidence, reasons, warnings);
    }

    private static double foirCap(String type) {
        return switch (type) {
            case "HOME" -> .50;
            case "LAP" -> .55;
            case "BUSINESS" -> .55;
            case "GOLD" -> .45;
            case "TWO_WHEELER" -> .50;
            default -> .50;
        };
    }

    private static double safeFoir(String type) {
        return switch (type) {
            case "HOME", "LAP", "BUSINESS" -> .40;
            default -> .35;
        };
    }

    private static double rateFor(String type, double score, String incomeType) {
        double base = switch (type) {
            case "HOME" -> 9.5;
            case "LAP" -> 11.5;
            case "BUSINESS" -> 13.0;
            case "GOLD" -> 12.0;
            case "TWO_WHEELER" -> 14.0;
            default -> 13.5;
        };
        if (score >= 750) base -= 1.25;
        else if (score >= 700) base -= .50;
        else if (score > 0 && score < 650) base += 2.0;
        if (incomeType.equals("SELF_EMPLOYED")) base += .5;
        if (incomeType.equals("INFORMAL")) base += 2.0;
        return base;
    }

    private static double pvToPrincipal(double emi, double annualRate, int months) {
        if (emi <= 0) return 0;
        double r = annualRate / 1200.0;
        return emi * (1 - Math.pow(1 + r, -months)) / r;
    }

    private static double n(Map<String,Object> m, String k) {
        Object v = m.get(k);
        if (v == null || v.toString().isBlank()) return 0;
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return 0; }
    }
    private static String s(Map<String,Object> m, String k) {
        Object v = m.get(k); return v == null ? "" : v.toString();
    }
    private static double round(double x) { return Math.round(x * 100.0) / 100.0; }
    private static String pct(double x) { return Math.round(x * 100) + "%"; }
    private static double feeFor(String type) {
        return switch (type) {
            case "HOME" -> .50; case "LAP" -> 1.00; case "BUSINESS" -> 1.50;
            case "GOLD" -> 1.00; case "TWO_WHEELER" -> 2.00; default -> 1.50;
        };
    }
}
