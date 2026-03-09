package com.diegoferreiracaetano.dlearn.ui.sdui

/**
 * Catalog of all localized string keys.
 * Used by the Backend to fetch translations from ResourceBundles.
 */
enum class AppStringType {
    // Profile
    PROFILE_TITLE,
    PREMIUM_MEMBER,
    PREMIUM_DESCRIPTION,
    SECTION_ACCOUNT,
    ITEM_MEMBER,
    ITEM_PASSWORD,
    SECTION_GENERAL,
    ITEM_NOTIFICATION,
    ITEM_LANGUAGE,
    ITEM_COUNTRY,
    ITEM_CLEAR_CACHE,
    SECTION_MORE,
    ITEM_LEGAL,
    ITEM_HELP,
    ITEM_ABOUT,
    LOGOUT,

    // Home
    HOME_TITLE,
    HOME_SUBTITLE,
    FILTER_SERIES,
    FILTER_MOVIES,
    FILTER_CATEGORIES,
    SECTION_TOP_10,
    SECTION_POPULAR,
    NAV_HOME,
    NAV_SEARCH,
    NAV_FAVORITES,
    NAV_PROFILE,

    // Movie Detail
    DETAIL_TRAILER,
    DETAIL_STORY_LINE,
    DETAIL_CAST_CREW,
    DETAIL_EPISODE,

    UNKNOWN
}
