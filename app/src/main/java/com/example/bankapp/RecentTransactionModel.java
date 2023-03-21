package com.example.bankapp;

public class RecentTransactionModel {
    String accname;
    String trancdate;
    String trancamount;
    String tranctype;

    public RecentTransactionModel(String accname, String trancdate, String trancamount, String tranctype) {
        this.accname = accname;
        this.trancdate = trancdate;
        this.trancamount = trancamount;
        this.tranctype = tranctype;
    }

    public String getAccname() {
        return accname;
    }

    public String getTrancdate() {
        return trancdate;
    }

    public String getTrancamount() {
        return trancamount;
    }

    public String getTranctype() {
        return tranctype;
    }

}


