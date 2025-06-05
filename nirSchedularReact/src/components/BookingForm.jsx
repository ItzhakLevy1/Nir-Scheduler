import handleSubmit from "../utils/formHandlers";

// inside your form's submit handler:
const onFormSubmit = (e) => {
  e.preventDefault();
  handleSubmit(formData, () => {
    setFormData(initialState); // Reset form
  });
};
