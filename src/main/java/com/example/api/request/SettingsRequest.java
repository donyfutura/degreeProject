package com.example.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsRequest {

    @JsonProperty("MULTIUSER_MODE")
    private boolean multiUser;

    @JsonProperty("POST_PREMODERATION")
    private boolean preModeration;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean publicStats;

    public SettingsRequest() {
    }

    public boolean isMultiUser() {
        return multiUser;
    }

    public void setMultiUser(boolean multiUser) {
        this.multiUser = multiUser;
    }

    public boolean isPreModeration() {
        return preModeration;
    }

    public void setPreModeration(boolean preModeration) {
        this.preModeration = preModeration;
    }

    public boolean isPublicStats() {
        return publicStats;
    }

    public void setPublicStats(boolean publicStats) {
        this.publicStats = publicStats;
    }
}
