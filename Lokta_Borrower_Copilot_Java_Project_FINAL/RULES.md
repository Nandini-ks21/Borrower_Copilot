# Borrower Copilot — Rules & Assumptions

This is a transparent prototype, not a lender underwriting engine. The challenge brief asks for a borrower self-assessment, ranges, explainability, India-specific rupee outputs, APR/fee awareness, adaptive questioning, and honest uncertainty. The implementation below intentionally exposes its assumptions.

| What | Value | Why | Source / basis |
|---|---:|---|---|
| Lender-style FOIR cap: home | 50% | Illustrative lender capacity | My judgement; not a lender policy |
| Lender-style FOIR cap: LAP/business | 55% | Illustrative secured/business capacity | My judgement |
| Lender-style FOIR cap: other | 45–50% | Product-sensitive capacity | My judgement |
| Borrower safe FOIR: home/LAP/business | 40% | More conservative borrower ceiling | My judgement |
| Borrower safe FOIR: other | 35% | Conservative ceiling for consumer credit | My judgement |
| Extra expense guard | Preserve 10% of income after EMI + expenses | Adds resilience instead of relying only on FOIR | My judgement |
| Maximum calculation tenure | 60 months | Makes the illustrative comparison consistent | My judgement |
| Stress tenure | 48 months | Shows that rate/tenure changes can matter | My judgement |
| Base home rate | 9.5% | Starting assumption | My judgement; replace with a dated lender-market dataset before production |
| Base LAP rate | 11.5% | Starting assumption | My judgement |
| Base business rate | 13.0% | Starting assumption | My judgement |
| Base gold rate | 12.0% | Starting assumption | My judgement |
| Base two-wheeler rate | 14.0% | Starting assumption | My judgement |
| Base personal rate | 13.5% | Starting assumption | My judgement |
| Excellent score adjustment | -1.25 pts at 750+ | Rewards stronger profile | My judgement |
| Score 700–749 adjustment | -0.50 pts | Moderate credit adjustment | My judgement |
| Score <650 adjustment | +2 pts | Higher uncertainty/risk | My judgement |
| Unknown score | Do not substitute a fake score; widen band | Meets “unknown is never zero” requirement | Challenge requirement |
| Self-employed adjustment | +0.50 pts | Recognition of documentation/income variability | My judgement |
| Informal-income adjustment | +2 pts | Wider uncertainty | My judgement |
| Variable income >50% | +0.5 to +0.75 pts | More uncertainty | My judgement |
| Fair-rate band | Base ± profile adjustments, then -1.25 to +1.75 | A range is more honest than a point quote | My judgement |
| Processing fee: personal/business | 1.5% | Needed for all-in comparison | My judgement |
| Processing fee: home | 0.5% | Needed for all-in comparison | My judgement |
| Processing fee: LAP/gold | 1.0% | Needed for all-in comparison | My judgement |
| Processing fee: two-wheeler | 2.0% | Needed for all-in comparison | My judgement |
| Product routing | Business/collateral can use secured capacity; personal remains unsecured | Reflects Ravi-like profile | My judgement |
| Don't-borrow trigger | Existing EMI >35% income OR expenses+existing EMI >80% income | Prevents obvious overextension | My judgement |
| Borrow-less trigger | Request >120% of safe maximum | Leaves room between “eligible” and “safe” | My judgement |
| Confidence | Starts 72; lower for unknown score, missing affordability inputs, high variable share | Confidence must widen with silence | My judgement |

## APR note

The challenge asks for “APR including processing fee”. A production implementation should calculate an effective annualized cost from the actual amortization schedule, principal disbursed net of fees, and all mandatory charges. This prototype displays the processing-fee assumption beside the fair rate instead of pretending a nominal rate plus a fee is a fully computed APR. That limitation is deliberate and should be fixed when actual fee/timing data is available.

## What the prototype does not know

- RBI's current product-specific regulatory ceilings or disclosure wording are not hard-coded as legal claims.
- No live lender quotes, bureau data, policy rules, GST/insurance/other mandatory charges, or lender-specific age/tenure policies are connected.
- A lender may sanction less or more than the illustrative capacity.
- Income verification and documentation can dominate eligibility for self-employed and informal borrowers.
- “Productive use” does not automatically mean the return will cover debt service.

## Three challenge personas

### Priya
29, Bengaluru, salaried, ₹1,10,000 net/month, ₹14,000 car EMI, ₹28,000 rent, score 780, wants ₹8,00,000 personal loan for wedding.

### Ravi
42, Mysuru, self-employed kirana, ₹40,000–80,000 cash/month, ITR ₹4,20,000/year, ₹45,00,000 unencumbered shop, no formal score, wife ₹18,000/month, wants ₹15,00,000 for stock + delivery vehicle.

### Anita
35, Hubballi, informal mixed income ₹26,000–30,000/month, husband unemployed, 3 app loans, ₹35,000 outstanding at 30%+, one recent bounce, wants ₹1,50,000 for an electric scooter.

The UI collects a single-point income/expense input for a run. For these personas, use representative midpoint assumptions and explicitly record them in the run-through notes.

## Security/privacy

No database, authentication, cookies for personal data, or external API is used. The assessment is submitted as a normal form and is only held for rendering the result page.
