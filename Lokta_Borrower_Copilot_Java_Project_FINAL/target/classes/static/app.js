const steps = [...document.querySelectorAll('.step')];
let current = 0;

function showStep(index) {
  if (!steps.length) return;
  current = Math.max(0, Math.min(index, steps.length - 1));
  steps.forEach((step, i) => step.classList.toggle('active', i === current));
  const bar = document.getElementById('progressBar');
  if (bar) bar.style.width = `${((current + 1) / steps.length) * 100}%`;
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function validateCurrentStep() {
  if (!steps[current]) return true;
  const fields = steps[current].querySelectorAll('input[required], select[required]');
  for (const field of fields) {
    if (!field.checkValidity()) {
      field.reportValidity();
      return false;
    }
  }
  return true;
}

document.querySelectorAll('.next').forEach(button => {
  button.addEventListener('click', () => {
    if (validateCurrentStep() && current < steps.length - 1) {
      showStep(current + 1);
    }
  });
});

document.querySelectorAll('.prev').forEach(button => {
  button.addEventListener('click', () => {
    if (current > 0) showStep(current - 1);
  });
});

function toggleTheme() {
  document.documentElement.classList.toggle('dark');
  localStorage.setItem('theme', document.documentElement.classList.contains('dark') ? 'dark' : 'light');
}

if (localStorage.getItem('theme') === 'dark') {
  document.documentElement.classList.add('dark');
}

// Build My Card uses normal HTML form submission. The three required checkboxes
// are enforced by the browser; this handler only provides a clear message.
const assessment = document.getElementById('assessment');
const checkError = document.getElementById('checkError');

if (assessment) {
  assessment.addEventListener('submit', event => {
    const checkboxes = [...assessment.querySelectorAll('input[type="checkbox"][required]')];
    const allChecked = checkboxes.length === 3 && checkboxes.every(box => box.checked);

    if (!allChecked) {
      event.preventDefault();
      if (checkError) checkError.hidden = false;
      const firstUnchecked = checkboxes.find(box => !box.checked);
      if (firstUnchecked) firstUnchecked.focus();
      return;
    }

    if (checkError) checkError.hidden = true;
    // Do not call preventDefault: POST /assess must continue to the Spring controller.
  });
}

showStep(0);
