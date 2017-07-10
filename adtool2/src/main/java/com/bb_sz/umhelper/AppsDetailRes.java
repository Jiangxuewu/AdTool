package com.bb_sz.umhelper;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class AppsDetailRes {



    private int total;
    private String result;
    private List<StatsBean> stats;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<StatsBean> getStats() {
        return stats;
    }

    public void setStats(List<StatsBean> stats) {
        this.stats = stats;
    }

    public static class StatsBean {

        private String name;
        private boolean sdk_tip;
        private String sdk_version;
        private String game;
        private boolean starred;
        private String app_id;
        private String platform;
        private String report_path;
        private int install_today;
        private int install_yesterday;
        private int active_today;
        private int active_yesterday;
        private int launch_today;
        private int launch_yesterday;
        private int install_all;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSdk_tip() {
            return sdk_tip;
        }

        public void setSdk_tip(boolean sdk_tip) {
            this.sdk_tip = sdk_tip;
        }

        public String getSdk_version() {
            return sdk_version;
        }

        public void setSdk_version(String sdk_version) {
            this.sdk_version = sdk_version;
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public boolean isStarred() {
            return starred;
        }

        public void setStarred(boolean starred) {
            this.starred = starred;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getReport_path() {
            return report_path;
        }

        public void setReport_path(String report_path) {
            this.report_path = report_path;
        }

        public int getInstall_today() {
            return install_today;
        }

        public void setInstall_today(int install_today) {
            this.install_today = install_today;
        }

        public int getInstall_yesterday() {
            return install_yesterday;
        }

        public void setInstall_yesterday(int install_yesterday) {
            this.install_yesterday = install_yesterday;
        }

        public int getActive_today() {
            return active_today;
        }

        public void setActive_today(int active_today) {
            this.active_today = active_today;
        }

        public int getActive_yesterday() {
            return active_yesterday;
        }

        public void setActive_yesterday(int active_yesterday) {
            this.active_yesterday = active_yesterday;
        }

        public int getLaunch_today() {
            return launch_today;
        }

        public void setLaunch_today(int launch_today) {
            this.launch_today = launch_today;
        }

        public int getLaunch_yesterday() {
            return launch_yesterday;
        }

        public void setLaunch_yesterday(int launch_yesterday) {
            this.launch_yesterday = launch_yesterday;
        }

        public int getInstall_all() {
            return install_all;
        }

        public void setInstall_all(int install_all) {
            this.install_all = install_all;
        }
    }
}
