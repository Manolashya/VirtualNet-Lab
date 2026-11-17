package com.virtualnetlab.application;

import java.util.ArrayList;
import java.util.List;

/** Optional simple email send/receive flow for the Application Layer demo. */
public class EmailSimulation {
    public record EmailMessage(String from, String to, String subject, String body) {}

    public record EmailResult(
            String smtpStep,
            String dnsStep,
            String pop3Step,
            EmailMessage message) {

        public String toDisplayString() {
            return "EMAIL SEND / RECEIVE\n"
                    + "------------------------------\n"
                    + "SMTP        : " + smtpStep + "\n"
                    + "DNS         : " + dnsStep + "\n"
                    + "POP3/RECEIVE: " + pop3Step + "\n"
                    + "From        : " + message.from() + "\n"
                    + "To          : " + message.to() + "\n"
                    + "Subject     : " + message.subject() + "\n"
                    + "Body        : " + message.body();
        }
    }

    private final List<EmailMessage> inbox = new ArrayList<>();

    public EmailResult sendAndReceive(EmailMessage message) {
        String mailDomain = extractDomain(message.to());
        inbox.add(message);
        return new EmailResult(
                "Message accepted by simulated SMTP server",
                "MX lookup simulated for " + mailDomain,
                "Message received from simulated mailbox",
                message);
    }

    public int getInboxSize() {
        return inbox.size();
    }

    private String extractDomain(String email) {
        int at = email == null ? -1 : email.indexOf('@');
        return at >= 0 ? email.substring(at + 1) : "unknown";
    }
}
