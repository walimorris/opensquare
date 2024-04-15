package com.morris.opensquare.services;

import com.morris.opensquare.models.validations.DisposableEmailDomain;

public interface EmailValidationService {

    /**
     * Adds a new disposable email domain to repository collection.
     * <br><br>
     * Disposable email addressing, also known as DEA, dark mail or masked email,
     * refers to an approach that involves using a unique email address for every
     * contact or entity, or for a limited number of times or uses. The benefit
     * is that if anyone compromises the address or utilizes it in connection with
     * email abuse, the address owner can easily cancel (or "dispose" of) it
     * without affecting any of their other contacts.
     * <br><br>
     * These "burner" email addresses can be traced by the given email domain and
     * there are numerous databases that contain a list of domains. Opensquare
     * owns a list of such domains.
     *
     * @param domain {@link String} domain address
     * @see <a href="https://en.wikipedia.org/wiki/Disposable_email_address">What are disposable emails?</a>
     *
     * @return {@link DisposableEmailDomain}
     */
    DisposableEmailDomain addDisposableEmailDomain(String domain);

    /**
     * Finds disposable email domain from a given email address.
     * <br><br>
     * Disposable email addressing, also known as DEA, dark mail or masked email,
     * refers to an approach that involves using a unique email address for every
     * contact or entity, or for a limited number of times or uses. The benefit
     * is that if anyone compromises the address or utilizes it in connection with
     * email abuse, the address owner can easily cancel (or "dispose" of) it
     * without affecting any of their other contacts.
     * <br><br>
     * These "burner" email addresses can be traced by the given email domain and
     * there are numerous databases that contain a list of domains. Opensquare
     * owns a list of such domains.
     *
     * @param emailAddress {@link String}
     * @see <a href="https://en.wikipedia.org/wiki/Disposable_email_address">What are disposable emails?</a>
     *
     * @return {@link DisposableEmailDomain}
     */
    DisposableEmailDomain findDisposableEmailDomain(String emailAddress);

    /**
     * Get domain name from email-address.
     *
     * @param emailAddress email address
     *
     * @return {@link String} email address domain
     */
    String getEmailDomain(String emailAddress);
}
