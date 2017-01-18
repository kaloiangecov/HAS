package has.mailsender;

/**
 * Created by gundev on 18.1.2017 г..
 */
public enum MailTemplates {

    RESERVATION_CONFIRMATION(
            "Your reservation is now confirmed!"), NEW_USER(
            "Welcome to the company!");
    private String message;

    /**
     * Gets string from enum.
     *
     * @return String enum description
     */
    public String getString() {
        return this.message;
    }

    /**
     * Parameterized constructor.
     *
     * @param cDuty
     *            cashiers duty
     */
    private MailTemplates(String cDuty) {
        this.message = cDuty;
    }
}