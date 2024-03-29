package com.morris.opensquare.models;

/**
 * Opensquare Platforms should be explicitly defined so client's can reuse platforms without
 * worrying about versioning (future platform changes, ex. [Twitter] becoming [X]). Platforms
 * may be added and removed as Applications die, others are born and Opensquare grows.
 * <br><br>
 * Interesting enough, Enums are marshalled to JSON. This means, clients can pass the {@link Platform}
 * Enum as an API Request Param and the Platform will be marshalled to its exact String value.
 */
public enum Platform {
    YOUTUBE,
    TWITTER,
    VKONTAKTE
}
