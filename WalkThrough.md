1. Overview

Borrower Copilot helps Indian borrowers make informed borrowing decisions before approaching a lender.

The app provides four outputs:

Borrow / Don't Borrow / Borrow Less
Likely lender sanction amount vs. safe borrower amount
Fair interest-rate range and APR
Recommended EMI ceiling with a stress scenario

It works using borrower-provided information without login, bureau integration, or storing personal data.

2. Borrower Flow

The borrower answers a set of must-have questions such as loan purpose, loan amount, income, existing EMIs, expenses, age, and credit score if known.

Additional questions are shown only when they can improve an output. Unknown information is not treated as zero; instead, the relevant range becomes wider and confidence is reduced.

3. Outputs

The application calculates affordability and provides clear explanations for each recommendation.

The Negotiation Card summarises the borrower's:

Recommended loan amount
Fair interest-rate range
APR
Maximum EMI
Key reasons behind the recommendation

This allows the borrower to compare and negotiate a lender's offer.

4. Three Borrowers

The application was evaluated using the three scenarios provided in the challenge:

Priya: Salaried software engineer with strong credit history.
Ravi: Self-employed kirana owner with variable income and property collateral.
Anita: Informal-income borrower with existing high-cost loans and a recent EMI bounce.

The application adapts its assessment based on each borrower's circumstances.

5. Rules & Explainability

All thresholds, assumptions, interest-rate bands, and affordability rules are documented in RULES.md.

The application separates rules from the UI so that assumptions can be changed easily.

6. What I Would Build Next
More loan-product-specific rules
Better income and interest-rate stress testing
Lender-offer comparison
More personalised adaptive questions
Improved borrower education
7. What I Would Cut

For this challenge, I would avoid:

Credit bureau integration
Machine-learning credit scoring
User accounts and data storage
A large loan-product catalogue

I would prioritise transparent rules, explainability, adaptive questions, and a useful Negotiation Card.

8. Limitations

The results are estimates based on borrower-provided information and documented assumptions. They are not guarantees of loan approval, interest rate, or lender sanction.

The goal is to help the borrower enter a lender conversation better informed and able to negotiate.
