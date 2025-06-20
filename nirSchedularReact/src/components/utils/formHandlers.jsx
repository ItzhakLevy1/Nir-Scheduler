import ApiService from "../../service/ApiService"; // Service to handle API requests
import toastr from "toastr";

const handleSubmit = async (formData) => {
  const { fullName, email, phone, date, timeSlot } = formData;

  // If date or timeSlot is missing, an error toast is shown, and the function exits early
  try {
    if (!date || !timeSlot) {
      toastr.error("אנא בחר תאריך ומשבצת זמן");
      return;
    }

    // Date Formatting - date is converted to a proper ISO format (e.g., 2025-06-15), accounting for time zone differences.
    const formattedDate = new Date(
      new Date(date).getTime() - new Date().getTimezoneOffset() * 60000
    )
      .toISOString()
      .split("T")[0];

    // Create Appointment Object
    const appointment = {
      fullName,
      userEmail: email,
      phoneNumber: phone,
      date: formattedDate,
      timeSlot,
    };

    // Instead of addAppointment, use bookAppointment (requires appointmentId and userId)
    // You must provide appointmentId and userId in formData or from context
    const { appointmentId, userId } = formData;
    if (!userId) {
      toastr.error("חסרים מזהי תור או משתמש. לא ניתן להזמין.");
      return;
    }
    const booking = {
      fullName,
      userEmail: email,
      phoneNumber: phone,
      date: formattedDate,
      timeSlot,
      appointmentId,
    };

    const response = await ApiService.bookAppointment(userId, booking); // Book appointment

    if (response.data?.statusCode === 200) {
      toastr.success(
        "התור נשלח בהצלחה! קוד אישור: " + response.data.confirmationCode
      );
    }
  } catch (error) {
    toastr.error(error.response?.data?.message || "שגיאה בשליחת התור");
    console.log("Booking error 1 :", error.response?.data || error.message);
    console.error("Booking error 2 :", error);
  }
};

export default handleSubmit;
