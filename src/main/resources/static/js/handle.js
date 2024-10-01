function handleInputBlur(input) {
    // Get the parent container of the input (div)
    const container = input.parentElement;

    // Check if an error message already exists (prevents adding duplicates)
    let errorText = container.querySelector('.error-message');

    // If input is empty (after trimming whitespace)
    if (input.value.trim() === '') {
        // If no error text exists, create and add it
        if (!errorText) {
            errorText = document.createElement('p');
            errorText.className = 'error-message';
            errorText.textContent = 'This field is required';
            container.insertBefore(errorText, input); // Insert error above the input
        }
        // Add the error border class
        input.classList.add('input-error');
    } else {
        // If there's an error message, remove it
        if (errorText) {
            errorText.remove();
        }
        // Remove the error border class
        input.classList.remove('input-error');
    }
}
