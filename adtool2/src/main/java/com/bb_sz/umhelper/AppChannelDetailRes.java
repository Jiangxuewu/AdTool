package com.bb_sz.umhelper;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class AppChannelDetailRes {


    private String result;
    private int total;
    private List<StatsBean> stats;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<StatsBean> getStats() {
        return stats;
    }

    public void setStats(List<StatsBean> stats) {
        this.stats = stats;
    }

    public static class StatsBean {

        private String channel_id;
        private String channel_name;
        private int installation;
        private int active_user;
        private int pay_user;
        private double pay_num;
        private int total_install;
        private double total_install_rate;

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public int getInstallation() {
            return installation;
        }

        public void setInstallation(int installation) {
            this.installation = installation;
        }

        public int getActive_user() {
            return active_user;
        }

        public void setActive_user(int active_user) {
            this.active_user = active_user;
        }

        public int getPay_user() {
            return pay_user;
        }

        public void setPay_user(int pay_user) {
            this.pay_user = pay_user;
        }

        public double getPay_num() {
            return pay_num;
        }

        public void setPay_num(double pay_num) {
            this.pay_num = pay_num;
        }

        public int getTotal_install() {
            return total_install;
        }

        public void setTotal_install(int total_install) {
            this.total_install = total_install;
        }

        public double getTotal_install_rate() {
            return total_install_rate;
        }

        public void setTotal_install_rate(double total_install_rate) {
            this.total_install_rate = total_install_rate;
        }
    }
}
