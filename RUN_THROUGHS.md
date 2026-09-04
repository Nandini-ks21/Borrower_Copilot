# Three Run-throughs

These are illustrative test runs using representative point estimates where the challenge personas provide ranges or incomplete data. They are not claimed to be the “expected” answer from Lokta.

## 1. Priya

Representative inputs:
- Loan: Personal
- Purpose: Wedding
- Wanted: ₹8,00,000
- Age: 29
- Net income: ₹1,10,000
- Income type: Salaried
- Existing EMI: ₹14,000
- Household expenses: ₹28,000
- Variable share: 0%
- Credit score: 780
- Collateral: ₹0

Expected app behavior:
- Borrow / Borrow less depending on the exact expense interpretation; safe ceiling should be visibly lower than a lender-style ceiling.
- Stronger score narrows the rate band.
- Negotiation card should target the fair-rate band and safe EMI ceiling.
- No “unknown score” warning.

## 2. Ravi

Representative inputs:
- Loan: Business
- Purpose: Productive
- Wanted: ₹15,00,000
- Age: 42
- Net income: ₹60,000 (midpoint of stated cash range)
- Income type: Self-employed
- Existing EMI: ₹0
- Household expenses: ₹30,000
- Variable share: 50%
- Credit score: blank/unknown
- Collateral: ₹45,00,000

Expected app behavior:
- Route to Business rather than a generic personal-loan framing.
- Unknown score widens the fair-rate range rather than becoming 300.
- Secured capacity is also constrained by the collateral cap.
- App should show that lender capacity and safe capacity are different.

## 3. Anita

Representative inputs:
- Loan: Two-wheeler
- Purpose: Vehicle
- Wanted: ₹1,50,000
- Age: 35
- Net income: ₹28,000 (midpoint)
- Income type: Informal
- Existing EMI: ₹35,000 outstanding is not itself an EMI, so this run uses ₹12,000 as a representative existing EMI placeholder; replace with the actual EMI when known.
- Household expenses: ₹14,000
- Variable share: 50%
- Credit score: blank/unknown
- Collateral: ₹0

Expected app behavior:
- High existing burden should push toward DON'T BORROW or BORROW LESS.
- Informal income + unknown score widens the rate band.
- A recent bounce is an important additional question in a production version and should be used to tighten/raise risk ranges.
- The productive-scooter thesis should not override affordability.

## Why these are not hard-coded

The challenge explicitly says the app should work from what a borrower tells it and that every rule should be defensible. These personas are test fixtures, not hidden exceptions in production logic.
