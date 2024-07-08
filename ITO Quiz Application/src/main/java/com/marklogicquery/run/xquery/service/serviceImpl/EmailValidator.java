package com.marklogicquery.run.xquery.service.serviceImpl;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class EmailValidator {

    // Define the regex for email validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Compile the regex into a pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Validates the given email string against the email regex pattern.
     *
     * @param email The email string to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        // Create a matcher for the email string
        Matcher matcher = EMAIL_PATTERN.matcher(email);

        // Return whether the email matches the pattern
        return matcher.matches();
    }

}
