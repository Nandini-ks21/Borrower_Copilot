# Lokta Borrower Copilot — Java/Spring Boot

A working Java 17 + Spring Boot borrower self-assessment app for the Lokta build challenge.

## Run

Requirements: Java 17+ and Maven 3.9+.

```bash
mvn spring-boot:run
```

Open `http://localhost:8080`.

## Build My Card flow

1. Complete each of the four steps.
2. On step 4, tick all three acknowledgement checkboxes.
3. Click **Build my card →**.
4. The browser submits a normal `POST /assess` request.
5. `AssessmentController` receives the answers, `LoanRules` calculates the assessment, and Thymeleaf renders the borrower brief and Negotiation Card.

The three acknowledgement checkboxes are real HTML required checkboxes and are unchecked by default. JavaScript does not cancel a valid submission.

## Project files

- `src/main/java/.../AssessmentController.java` — HTTP routes and form handling.
- `src/main/java/.../LoanRules.java` — affordability/rate rules kept separate from UI.
- `src/main/resources/templates/index.html` — four-step form and result/Negotiation Card.
- `src/main/resources/static/app.js` — step navigation, validation, theme toggle.
- `src/main/resources/static/app.css` — responsive frontend.
- `RULES.md` — assumptions and domain rules.
- `RUN_THROUGHS.md` — Priya, Ravi, and Anita scenarios.
- `src/test/.../LoanRulesTest.java` — rule tests.
